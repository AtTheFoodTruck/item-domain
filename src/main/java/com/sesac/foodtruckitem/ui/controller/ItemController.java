package com.sesac.foodtruckitem.ui.controller;

import com.sesac.foodtruckitem.application.service.ItemService;
import com.sesac.foodtruckitem.application.service.StoreService;
import com.sesac.foodtruckitem.ui.dto.Result;
import com.sesac.foodtruckitem.ui.dto.response.ItemResponseDto;
import com.sesac.foodtruckitem.ui.dto.response.StoreResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;
    private final StoreService storeService;

    @ApiOperation(value = "Order Domain에서 요청 - Item 정보 조회")
    @GetMapping("/api/v1/item/{itemId}")
    ResponseEntity<Result> getItems(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                    @PathVariable("itemId") List<Long> itemId) {
        List<ItemResponseDto.GetItemsInfoDto> items = itemService.getItems(itemId);

        log.info("items : {} ", items);

        return ResponseEntity.ok(Result.createSuccessResult(items));
    }

    @GetMapping("/api/v1/store/info/{storeId}")
    public ResponseEntity<Result> getStoreMaps(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                               @PathVariable("storeId") List<Long> storeId) {

        List<StoreResponseDto.StoreInfoDto> storeAllById = storeService.findStoreAllById(storeId);

        return ResponseEntity.ok(Result.createSuccessResult(storeAllById));
    }

}
