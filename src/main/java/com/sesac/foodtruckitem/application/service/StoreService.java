package com.sesac.foodtruckitem.application.service;

import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Map;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.*;
import com.sesac.foodtruckitem.infrastructure.query.http.OrderClient;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.CreateUserDto;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.*;
import com.sesac.foodtruckitem.infrastructure.query.http.UserClient;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.StoreInfo;
import com.sesac.foodtruckitem.ui.dto.Response;
import com.sesac.foodtruckitem.ui.dto.SearchStoreResultDto;
import com.sesac.foodtruckitem.ui.dto.api.BNoApiRequestDto;
import com.sesac.foodtruckitem.ui.dto.request.PostStoreRequestDto;
import com.sesac.foodtruckitem.ui.dto.request.SearchStoreCondition;
import com.sesac.foodtruckitem.ui.dto.response.StoreResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepositoryCustom itemRepositoryCustom;
    private final StoreRepositoryCustom storeRepositoryCustom;
    private final Response response;
    private final UserClient userClient;
    private final OrderClient orderClient;
    private final RestTemplate restTemplate;

    /**
     * 가게정보 등록 - 점주
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-03
     **/
    @Transactional
    public ResponseEntity<?> createStore(HttpServletRequest request,
                                         Long userId,
                                         PostStoreRequestDto postStoreRequestDto) {

        String authorization = request.getHeader("Authorization");

        // 1. user 정보 갖고오기, using feign client
        CreateUserDto createUserDto = userClient.userInfo(authorization, userId);

        if (createUserDto.getUserId()==null) {
            return response.fail("사용자 정보를 불러오는데 실패했습니다.", HttpStatus.BAD_REQUEST);
        }

        log.info("Return 받은 user 객체의 값 : {}", createUserDto);

        // 2. 이미 푸드트럭을 등록한 점주인지 체크
        int count = storeRepository.countByUserId(createUserDto.getUserId());
        if (count > 0) {
            return response.fail("이미 등록된 푸드트럭 가게 정보가 존재합니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Category 정보 조회
        Category findCategory = categoryRepository.findByName(postStoreRequestDto.getCategoryName()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 카테고리 정보가 없습니다 " + postStoreRequestDto.getCategoryName())
        );

        // 3. Address 생성
        Address address = Address.of(postStoreRequestDto.getAddress().getAddress(), postStoreRequestDto.getAddress().getZipCode());

        // 4. Images 생성
        Images images = Images.of(postStoreRequestDto.getImages().getImgName(), postStoreRequestDto.getImages().getImgUrl());

        // 5. BusinessInfo 생성
        BusinessInfo businessInfo = BusinessInfo.of(postStoreRequestDto.getBusinessInfo().getBNo(), postStoreRequestDto.getBusinessInfo().getSDt(), postStoreRequestDto.getBusinessInfo().getPName());

        // 6. Map 생성
        Map map = Map.of(postStoreRequestDto.getMap().getLatitude(), postStoreRequestDto.getMap().getLongitude());

        // Store 생성
        Store store = Store.createStore(
                postStoreRequestDto.getStoreName().replaceAll(" ",""),
                postStoreRequestDto.getPhoneNum(),
                true,
                postStoreRequestDto.getNotice(),
                postStoreRequestDto.getOpenTime(),
                address,
                images,
                businessInfo,
                map,
                createUserDto.getUserId()
        );

        // 3. 가게 정보 등록
        Store savedStore = storeRepository.save(store);

        // 4. 카테고리 등록 및 연관관계 설정
        savedStore.setCategory(findCategory);

        StoreInfo storeInfo = new StoreInfo(userId, savedStore.getId());

        // 5. User Client에 storeId 저장
        userClient.saveStoreInfo(authorization, storeInfo);

        return response.success("가게 정보가 저장되었습니다.");
    }

    /**
     * 가게 정보 수정
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-04
     **/
    @Transactional
    public ResponseEntity<?> updateStoreInfo(HttpServletRequest request,
                                             PostStoreRequestDto.UpdateStoreDto updateStoreDto) {

        // 수정 정보 생성 - 공지사항, 사진, 영업시간, 영업장소, 전화번호

        // 가게 정보 조회
        Store findStore = storeRepository.findById(updateStoreDto.getStoreId()).orElseThrow(
                () -> new IllegalArgumentException("등록된 가게 정보가 없습니다 : ")
        );

        //TODO 수정자와 푸드트럭 점주 일치 여부 체크

        // 3. Address 생성
        Address address = Address.of(updateStoreDto.getAddress(), updateStoreDto.getZipCode());

        // 4. Images 생성
        Images images = Images.of(updateStoreDto.getImgName(), updateStoreDto.getStoreImgUrl());

        // 5. Map 생성
        Map map = Map.of(updateStoreDto.getLatitude(), updateStoreDto.getLongitude());

        // 가게 정보 업데이트
        findStore.changeStoreInfo(
                updateStoreDto.getNotice(),
                images,
                updateStoreDto.getOpenTime(),
                address,
                updateStoreDto.getPhoneNum()
                );

        return response.success("가게 정보가 수정되었습니다.");
    }

    /**
     * 가게 정보 삭제
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-05
     **/
    @Transactional
    public ResponseEntity<?> deleteStoreInfo(PostStoreRequestDto.DeleteStoreDto deleteStoreDto) {
        Store findStore = storeRepository.findById(deleteStoreDto.getStoreId()).orElseThrow(
                () -> new IllegalArgumentException("삭제할 가게 정보가 존재하지 않습니다.")
        );

        storeRepository.delete(findStore);

        return response.success("가게 정보가 삭제되었습니다.");
    }

    /**
     * 가게 정보 조회
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-05
    **/
    public StoreResponseDto.SearchStoreResult findStoreInfo(Long storeId, Pageable pageable) {
        // 가게 정보 조회 - notice, openTime, address, phoneNum, waitingCount(추가해야됨)
        Store findStore = storeRepository.findById(storeId).orElseThrow(
                () -> new IllegalArgumentException("조회할 가게 정보가 존재하지 않습니다.")
        );

        // 메뉴 정보 조회 - itemImg, itemUrl, itemName, price
        SliceImpl<StoreResponseDto.SearchItemDto> findItem = itemRepositoryCustom.findItemList(storeId, pageable);

        // 가게 정보 DTO로 만든 후 item add
        StoreResponseDto.SearchStoreResult storeDto = StoreResponseDto.SearchStoreResult.of(findStore, findItem.getContent(), findItem.hasNext());
        log.info("storeDto : {} ", storeDto);

        return storeDto;
    }

    /**
     * 사업자등록번호 상태 조회
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022-04-04
    **/
    public boolean businessValidateCheck(BNoApiRequestDto.BNoStatusDto bNoStatusDto) {
        // UriComponents
        URI uriComponents = UriComponentsBuilder
                .fromHttpUrl("https://api.odcloud.kr/api/nts-businessman/v1/status")
                .queryParam("serviceKey", "JyZTPPmD5XHt0PIhYecvp1xIsQj%2B1kU%2Btw4P%2Be2UHoqKCIdQ2gM5aQvJCGDrWh4LRE9fv7YOZIlNuj2o0asNDA%3D%3D")
                .build(true)
                .encode()
                .toUri();

        // HttpEntity(body, header)
        BNoApiRequestDto.ApiReqStatusDto dto = BNoApiRequestDto.ApiReqStatusDto.builder()
                .bNo(bNoStatusDto.getBNo())
                .build();

        HttpEntity<BNoApiRequestDto.ApiReqStatusDto> entity = new HttpEntity<>(dto, new HttpHeaders());

        // Request API
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(uriComponents, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }

        // Json Parsing
        JSONParser jsonParser = new JSONParser();
        JSONObject body;
        try {
            body = (JSONObject) jsonParser.parse(response.getBody());
        } catch (ParseException e) {
            log.warn("Json Parsing Error: 사업자등록번호 상태조회 실패");
            return false;
        }

        // status 가 OK 거나, 등록된 사용자 ("match_cnt" = 1)
        return "OK".equals(body.get("status_code")) && body.containsKey("match_cnt");
    }


    /**
     * 가게 정보 조회 다중
     * using by 리뷰 목록 조회(가게입장), 주문 내역 조회
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/04/14
    **/
    public List<StoreResponseDto.StoreInfoDto> findStoreAllById(Iterable<Long> storeIds) {
        return storeRepository.findAllById(storeIds)
                .stream()
                .map(store -> StoreResponseDto.StoreInfoDto.of(store))
                .collect(Collectors.toList());
    }

    /**
     * 위치 기반 검색
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/04/14
    **/
    public Page<SearchStoreResultDto> searchStore(HttpServletRequest request, SearchStoreCondition condition, Pageable pageable) {
        String authorization = request.getHeader("Authorization");

        // 검색 결과 조회
//        SliceImpl<SearchStoreResultDto> searchStorePage = storeRepositoryCustom.findSearchStorePage(condition, pageable);
        Page<SearchStoreResultDto> searchStorePage = storeRepositoryCustom.findSearchStorePage(condition, pageable);

        // 별점 평균 주입
        Set<Long> storeIds = new HashSet<>();
//        searchStorePage.forEach(searchStoreResultDto -> storeIds.add(searchStoreResultDto.getStoreId()));
        searchStorePage.getContent().forEach(searchStoreResultDto -> storeIds.add(searchStoreResultDto.getStoreId()));

        // <storeId, avgRating>
        java.util.Map<Long, Double> reviewInfos = orderClient.getReviewInfos(authorization, storeIds);

        for (SearchStoreResultDto searchStoreResultDto : searchStorePage.getContent()) {
            searchStoreResultDto.changeAvgRating(reviewInfos.get(searchStoreResultDto.getStoreId()));
        }

//        return searchStorePage;
        return PageableExecutionUtils.getPage(searchStorePage.getContent(), pageable, () -> searchStorePage.getTotalPages());
    }
}
