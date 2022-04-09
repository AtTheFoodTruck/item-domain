package com.sesac.foodtruckitem.ui.controller;

import com.sesac.foodtruckitem.application.service.StoreService;
import com.sesac.foodtruckitem.ui.dto.Result;
import com.sesac.foodtruckitem.ui.dto.response.StoreResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;

    /**
     * 가게 정보 조회 - StoreClient
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-08
     **/
    @GetMapping("/items/v1/store/reviews/{storeId}")
    public ResponseEntity<Result> reviewStoreInfo(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                                  @PathVariable("storeId") Iterable<Long> storeIds) {

        List<StoreResponseDto.StoreInfoDto> storeAllById = storeService.findStoreAllById(storeIds);

        return ResponseEntity.ok(Result.createSuccessResult(storeAllById));
    }
}
