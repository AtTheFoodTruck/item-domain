package com.sesac.foodtruckitem.ui.controller;

import com.sesac.foodtruckitem.application.service.StoreService;
import com.sesac.foodtruckitem.exception.StoresException;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.StoreRepository;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.ReqReviewInfoDto;
import com.sesac.foodtruckitem.ui.dto.Result;
import com.sesac.foodtruckitem.ui.dto.response.ItemResponseDto;
import com.sesac.foodtruckitem.ui.dto.response.StoreResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.StoreException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;
    private final StoreRepository storeRepository;

    /**
     * 가게 정보 조회 다중
     * using by 리뷰 목록 조회(가게입장), 주문 내역 조회
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-08
     **/
    @ApiOperation(value = "Order Domain에서 요청 - 리뷰 정보 조회")
    @GetMapping("/api/v1/store/reviews/{storeIds}")
    public ResponseEntity<Result> reviewStoreInfo(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                                  @PathVariable("storeIds") List<Long> storeIds) {
        log.info("test");

        List<StoreResponseDto.StoreInfoDto> storeAllById = storeService.findStoreAllById(storeIds);

        return ResponseEntity.ok(Result.createSuccessResult(storeAllById));
    }

    /**
     * 가게 정보 조회 단건
     * using by 장바구니 목록 조회
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-09
    **/
    @ApiOperation(value = "Item Domain에서 요청 - user정보 저장")
    @GetMapping("/api/v1/store/cart/{storeId}")
    public ResponseEntity<Result> getStore(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                            @PathVariable("storeId") String storeId) {
        Store findStore = storeRepository.findById(Long.valueOf((storeId))).orElseThrow(
                () -> new EmptyResultDataAccessException("푸드트럭 정보가 존재하지 않습니다.", 1)
        );

        StoreResponseDto.StoreInfoDto storeDto = StoreResponseDto.StoreInfoDto.of(findStore);

        return ResponseEntity.ok(Result.createSuccessResult(storeDto));
    }

    /**
     * 가게 정보 조회 - storeId, storeName
     * using by 주문 조회 페이지(점주)
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/04/11
     **/
    @ApiOperation(value = "Item Domain에서 요청 - user정보 저장")
    @GetMapping("/api/v1/store/{userId}")
    ResponseEntity<Result> getStoreInfoByUserId(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                                @PathVariable("userId") Long userId) {
        Store store = storeRepository.findByUserId(userId).orElseThrow(
                () -> new StoresException(userId + "의 매장은 존재하지 않습니다.")
        );

        StoreResponseDto.GetStoreInfoByUserId storeInfo = StoreResponseDto.GetStoreInfoByUserId.of(store);

        return ResponseEntity.ok(Result.createSuccessResult(storeInfo));
    }

    /**
     * 리뷰 정보 저장
     * using by 리뷰 등록
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/04/11
     **/
    @ApiOperation(value = "Item Domain에서 요청 - review정보 저장")
    @PostMapping("/api/v1/store/review")
    void saveStoreInfos(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                       @RequestBody ReqReviewInfoDto storeInfo){
        Store findStore = storeRepository.findById(storeInfo.getStoreId()).orElseThrow(
                () -> new StoresException("가게 정보가 존재하지 않습니다")
        );

        findStore.changeRatingAvg(storeInfo.getAvgRating());

    }
}
