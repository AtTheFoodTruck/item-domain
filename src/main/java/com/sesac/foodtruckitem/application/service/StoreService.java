package com.sesac.foodtruckitem.application.service;

import com.sesac.foodtruckitem.exception.StoresException;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Map;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.*;
import com.sesac.foodtruckitem.infrastructure.query.http.OrderClient;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.CreateUserDto;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.*;
import com.sesac.foodtruckitem.infrastructure.query.http.UserClient;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.GetStoreInfoByUserId;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.ResWaitingCount;
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

    @Transactional
    public ResponseEntity<?> createStore(HttpServletRequest request,
                                         Long userId,
                                         PostStoreRequestDto postStoreRequestDto) {

        String authorization = request.getHeader("Authorization");

        CreateUserDto createUserDto = userClient.userInfo(authorization, userId);

        if (createUserDto.getUserId()==null) {
            return response.fail("사용자 정보를 불러오는데 실패했습니다.", HttpStatus.BAD_REQUEST);
        }

        int count = storeRepository.countByUserId(createUserDto.getUserId());
        if (count > 0) {
            return response.fail("이미 등록된 푸드트럭 가게 정보가 존재합니다.", HttpStatus.BAD_REQUEST);
        }

        Category findCategory = categoryRepository.findByName(postStoreRequestDto.getCategoryName()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 카테고리 정보가 없습니다 " + postStoreRequestDto.getCategoryName())
        );

        Address address = Address.of(postStoreRequestDto.getAddress().getAddress(), postStoreRequestDto.getAddress().getZipCode());
        Images images = Images.of(postStoreRequestDto.getImages().getImgName(), postStoreRequestDto.getImages().getImgUrl());
        BusinessInfo businessInfo = BusinessInfo.of(postStoreRequestDto.getBusinessInfo().getBNo(), postStoreRequestDto.getBusinessInfo().getSDt(), postStoreRequestDto.getBusinessInfo().getPName());
        Map map = Map.of(postStoreRequestDto.getMap().getLatitude(), postStoreRequestDto.getMap().getLongitude());
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

        Store savedStore = storeRepository.save(store);

        savedStore.setCategory(findCategory);

        StoreInfo storeInfo = new StoreInfo(userId, savedStore.getId());

        userClient.saveStoreInfo(authorization, storeInfo);

        return response.success("가게 정보가 저장되었습니다.");
    }

    @Transactional
    public ResponseEntity<?> updateStoreInfo(HttpServletRequest request,
                                             PostStoreRequestDto.UpdateStoreDto updateStoreDto) {

        Store findStore = storeRepository.findById(updateStoreDto.getStoreId()).orElseThrow(
                () -> new IllegalArgumentException("등록된 가게 정보가 없습니다 : ")
        );

        Address address = Address.of(updateStoreDto.getAddress(), updateStoreDto.getZipCode());
        Images images = Images.of(updateStoreDto.getImgName(), updateStoreDto.getStoreImgUrl());
        Map map = Map.of(updateStoreDto.getLatitude(), updateStoreDto.getLongitude());
        findStore.changeStoreInfo(
                updateStoreDto.getNotice(),
                images,
                updateStoreDto.getOpenTime(),
                address,
                updateStoreDto.getPhoneNum(),
                map
                );

        return response.success("가게 정보가 수정되었습니다.");
    }

    @Transactional
    public ResponseEntity<?> deleteStoreInfo(PostStoreRequestDto.DeleteStoreDto deleteStoreDto) {
        Store findStore = storeRepository.findById(deleteStoreDto.getStoreId()).orElseThrow(
                () -> new IllegalArgumentException("삭제할 가게 정보가 존재하지 않습니다.")
        );

        storeRepository.delete(findStore);

        return response.success("가게 정보가 삭제되었습니다.");
    }

    public StoreResponseDto.SearchStoreResult findStoreInfo(Long storeId, Pageable pageable) {
        Store findStore = storeRepository.findById(storeId).orElseThrow(
                () -> new IllegalArgumentException("조회할 가게 정보가 존재하지 않습니다.")
        );

        SliceImpl<StoreResponseDto.SearchItemDto> findItem = itemRepositoryCustom.findItemList(storeId, pageable);

        StoreResponseDto.SearchStoreResult storeDto = StoreResponseDto.SearchStoreResult.of(findStore, findItem.getContent(), findItem.hasNext());

        return storeDto;
    }

    public boolean businessValidateCheck(BNoApiRequestDto.BNoStatusDto bNoStatusDto) {
        URI uriComponents = UriComponentsBuilder
                .fromHttpUrl("https://api.odcloud.kr/api/nts-businessman/v1/status")
                .queryParam("serviceKey", "JyZTPPmD5XHt0PIhYecvp1xIsQj%2B1kU%2Btw4P%2Be2UHoqKCIdQ2gM5aQvJCGDrWh4LRE9fv7YOZIlNuj2o0asNDA%3D%3D")
                .build(true)
                .encode()
                .toUri();

        BNoApiRequestDto.ApiReqStatusDto dto = BNoApiRequestDto.ApiReqStatusDto.builder()
                .bNo(bNoStatusDto.getBNo())
                .build();

        HttpEntity<BNoApiRequestDto.ApiReqStatusDto> entity = new HttpEntity<>(dto, new HttpHeaders());

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

        return "OK".equals(body.get("status_code")) && body.containsKey("match_cnt");
    }


    public List<StoreResponseDto.StoreInfoDto> findStoreAllById(Iterable<Long> storeIds) {
        return storeRepository.findAllById(storeIds)
                .stream()
                .map(store -> StoreResponseDto.StoreInfoDto.of(store))
                .collect(Collectors.toList());
    }

    public Page<SearchStoreResultDto.MainStoreResultDto> getStoreByRating(Pageable pageable) {
        // 별점 평균을 구하기

        // 평균구한걸 repo param으로 전달 orderby용도
        Page<SearchStoreResultDto.MainStoreResultDto> storeMain = storeRepositoryCustom.findStoreMain(pageable);

        // 평균 반 올림해서
        storeMain.getContent().stream()
                .forEach(mainStoreResultDto -> SearchStoreResultDto.MainStoreResultDto.changeRateFormat(mainStoreResultDto));

        return PageableExecutionUtils.getPage(storeMain.getContent(), pageable, () -> storeMain.getTotalElements());
    }

    public Page<SearchStoreResultDto> searchStore(HttpServletRequest request, SearchStoreCondition condition, Pageable pageable) {
        String authorization = request.getHeader("Authorization");

        Page<SearchStoreResultDto> searchStorePage = storeRepositoryCustom.findSearchStorePage(condition, pageable);

        Set<Long> storeIds = new HashSet<>();
        searchStorePage.getContent().forEach(searchStoreResultDto -> storeIds.add(searchStoreResultDto.getStoreId()));

        java.util.Map<Long, Double> reviewInfos = orderClient.getReviewInfos(authorization, storeIds);

        for (SearchStoreResultDto searchStoreResultDto : searchStorePage.getContent()) {
            searchStoreResultDto.changeAvgRating(reviewInfos.get(searchStoreResultDto.getStoreId()));
        }

        return PageableExecutionUtils.getPage(searchStorePage.getContent(), pageable, () -> searchStorePage.getTotalElements());
    }

    @Transactional
    public void changeRatingInfo(Long storeId, Double avgRating) {
        Store findStore = storeRepository.findById(storeId).orElseThrow(
                () -> new StoresException("가게 정보를 찾을 수 없습니다")
        );

        findStore.changeRatingAvg(avgRating);
    }

    @Transactional
    public ResWaitingCount changeWaitingCount(Long storeId) {
        Store findStore = storeRepository.findById(storeId).orElseThrow(
                () -> new StoresException("가게 정보를 찾을 수 없습니다")
        );

        return new ResWaitingCount(findStore.plusWaitingCount());
    }

    public GetStoreInfoByUserId getStoreInfoByUserId(Long userId) {
        Store store = storeRepository.findByUserId(userId).orElseThrow(
                () -> new StoresException(userId + "의 매장은 존재하지 않습니다.")
        );

        GetStoreInfoByUserId storeInfo = GetStoreInfoByUserId.of(store);

        return storeInfo;
    }

    public ResponseEntity<?> validateDuplicateEmail(String storeName) {
        int count = storeRepository.countByName(storeName);

        if (count > 1) {
            return response.fail("이미 푸드트럭 상호명이 존재합니다", HttpStatus.BAD_REQUEST);
        }

        return response.success("사용 가능한 푸드트럭 상호명입니다.");
    }
}
