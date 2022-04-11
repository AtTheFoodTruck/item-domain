package com.sesac.foodtruckitem.ui.controller;

import com.sesac.foodtruckitem.application.service.ItemService;
import com.sesac.foodtruckitem.ui.dto.Result;
import com.sesac.foodtruckitem.ui.dto.response.ItemResponseDto;
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
public class ItemController {

    private final ItemService itemService;

    /**
     * Item 정보 조회
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-09
    **/
    @GetMapping("/items/v1/item/{itemId}")
    ResponseEntity<Result> getItems(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
                                    @PathVariable("itemId") List<Long> itemId) {
        List<ItemResponseDto.GetItemsInfoDto> items = itemService.getItems(itemId);

        log.info("items : {} ", items);

        return ResponseEntity.ok(Result.createSuccessResult(items));
    }
}
