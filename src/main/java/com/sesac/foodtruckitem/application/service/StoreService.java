package com.sesac.foodtruckitem.application.service;

import com.sesac.foodtruckitem.infrastructure.query.http.dto.CreateUserDto;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.*;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.CategoryRepository;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.ItemRepository;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.StoreRepository;
import com.sesac.foodtruckitem.infrastructure.query.http.UserClient;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.StoreInfo;
import com.sesac.foodtruckitem.ui.dto.Response;
import com.sesac.foodtruckitem.ui.dto.api.BNoApiRequestDto;
import com.sesac.foodtruckitem.ui.dto.request.PostStoreDto;
import com.sesac.foodtruckitem.ui.dto.response.SearchItemDto;
import com.sesac.foodtruckitem.ui.dto.response.StoreResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final Response response;
    private final UserClient userClient;
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
                                         PostStoreDto postStoreDto) {

        String authorization = request.getHeader("Authorization");

        // 1. user 정보 갖고오기, using feign client
        CreateUserDto createUserDto = userClient.userInfo(authorization, userId);

        log.info("Return 받은 user 객체의 값 : {}", createUserDto);

        // 2. 이미 푸드트럭을 등록한 점주인지 체크
        int count = storeRepository.countByUserId(createUserDto.getUserId());
        if (count > 0) {
            return response.fail("이미 등록된 푸드트럭 가게 정보가 존재합니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Category 정보 조회
        Category findCategory = categoryRepository.findByName(postStoreDto.getCategoryName()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 카테고리 정보가 없습니다 " + postStoreDto.getCategoryName())
        );

        // 3. Address 생성
        Address address = Address.of(postStoreDto.getAddress().getAddress(), postStoreDto.getAddress().getZipCode());

        // 4. Images 생성
        Images images = Images.of(postStoreDto.getImages().getImgName(), postStoreDto.getImages().getImgUrl());

        // 5. BusinessInfo 생성
        BusinessInfo businessInfo = BusinessInfo.of(postStoreDto.getBNo(), postStoreDto.getSDt(), postStoreDto.getPName());

        // 6. Map 생성
        Map map = Map.of(postStoreDto.getMap().getLatitude(), postStoreDto.getMap().getLongitude());

        // Store 생성
        Store store = Store.createStore(
                postStoreDto.getStoreName(),
                postStoreDto.getPhoneNum(),
                true,
                postStoreDto.getNotice(),
                postStoreDto.getOpenTime(),
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
                                             PostStoreDto.UpdateStoreDto updateStoreDto) {

        // 수정 정보 생성 - 공지사항, 사진, 영업시간, 영업장소, 전화번호

        // 가게 정보 조회
        Store findStore = storeRepository.findById(updateStoreDto.getStoreId()).orElseThrow(
                () -> new IllegalArgumentException("등록된 가게 정보가 없습니다 : ")
        );

        //TODO 수정자와 푸드트럭 점주 일치 여부 체크

        // 3. Address 생성
        Address address = Address.of(updateStoreDto.getAddress(), updateStoreDto.getZipCode());

        // 4. Images 생성
        Images images = Images.of(updateStoreDto.getImgName(), updateStoreDto.getImgUrl());

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
     *
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-05
     **/
    @Transactional
    public ResponseEntity<?> deleteStoreInfo(PostStoreDto.DeleteStoreDto deleteStoreDto) {
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
        // 가게 정보 조회 - notice, openTime, address, phoneNum
        Store findStore = storeRepository.findById(storeId).orElseThrow(
                () -> new IllegalArgumentException("조회할 가게 정보가 존재하지 않습니다.")
        );

        // 메뉴 정보 조회 - itemImg, itemUrl, itemName, price
        Slice<Item> findItem = itemRepository.findItemByStoreId(storeId, pageable);

        List<Item> items = findItem.getContent();

        List<SearchItemDto> searchItemDtos = new ArrayList<>();
        items.stream().forEach(item -> {
            searchItemDtos.add(SearchItemDto.of(item));
        });

        // 가게 정보 DTO로 만든 후 item add
        StoreResponseDto.SearchStoreResult storeDto = StoreResponseDto.SearchStoreResult.of(findStore, searchItemDtos);
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


}
