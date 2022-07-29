package com.sesac.foodtruckitem.ui.controller;

import com.sesac.foodtruckitem.application.service.StoreService;
import com.sesac.foodtruckitem.exception.StoresException;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.StoreRepository;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.GetStoreInfoByUserId;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.GetStoreResponse;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.ReqReviewInfoDto;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.ResWaitingCount;
import com.sesac.foodtruckitem.ui.dto.Response;
import com.sesac.foodtruckitem.ui.dto.Result;
import com.sesac.foodtruckitem.ui.dto.response.StoreResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;
    private final StoreRepository storeRepository;

    @ApiOperation(value = "Order Domain에서 요청 - 리뷰 정보 조회")
    @GetMapping("/api/v1/store/reviews/{storeIds}")
    public ResponseEntity<Result> reviewStoreInfo(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                                  @PathVariable("storeIds") List<Long> storeIds) {
        log.info("test");

        List<StoreResponseDto.StoreInfoDto> storeAllById = storeService.findStoreAllById(storeIds);

        return ResponseEntity.ok(Result.createSuccessResult(storeAllById));
    }

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

    @ApiOperation(value = "User Domain에서 요청 - user정보 저장")
    @GetMapping("/api/v1/store/{userId}")
    public ResponseEntity<Result> getStoreInfoByUserId(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                                @PathVariable("userId") Long userId) {

        GetStoreInfoByUserId storeInfoByUserId = storeService.getStoreInfoByUserId(userId);

        return ResponseEntity.ok(Result.createSuccessResult(storeInfoByUserId));
    }

    @ApiOperation(value = "Order Domain에서 요청 - review정보 저장")
    @PostMapping("/api/v1/store/review")
    void saveStoreInfos(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                       @RequestBody ReqReviewInfoDto storeInfo){
        storeService.changeRatingInfo(storeInfo.getStoreId(), storeInfo.getAvgRating());
    }

    @ApiOperation(value = "Order Domain에서 요청 - review정보 저장")
    @PostMapping("/api/v1/store/waiting/{storeId}")
    ResWaitingCount saveWaitingCount(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                          @PathVariable("storeId") Long storeId) {
        return storeService.changeWaitingCount(storeId);
    }

    @ApiOperation(value = "Notification Domain에서 요청 - 가게정보조회")
    @GetMapping("/api/v1/store/{storeId}")
    public ResponseEntity<Result> getStore(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                      @PathVariable(value = "storeId") Long storeId) {
        GetStoreResponse storeInfoByStoreId = storeService.getStoreInfoByStoreId(storeId);

        return ResponseEntity.ok(Result.createSuccessResult(storeInfoByStoreId));
    }

}
