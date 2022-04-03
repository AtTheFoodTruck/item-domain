package com.sesac.foodtruckitem.ui.controller;

import com.sesac.foodtruckitem.application.service.ItemService;
import com.sesac.foodtruckitem.ui.dto.Helper;
import com.sesac.foodtruckitem.ui.dto.Response;
import com.sesac.foodtruckitem.ui.dto.request.ItemRequestDto;
import com.sesac.foodtruckitem.ui.dto.response.ItemResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "items", description = "아이템 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
@RestController
public class ItemController {

    private final ItemService itemService;
    private final Response response;

    /**
     * 점주 입장) 메뉴 조회
     * : 메뉴 관리 페이지
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/03
    **/
    @GetMapping("/v1")
    public ResponseEntity<?> getItems(@Valid @RequestBody ItemRequestDto.GetItemsDto getItemsDto, BindingResult results) {
        // validation 검증
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        // item 조회
        List<ItemResponseDto.GetItemsDto> responseItemsDto = itemService.getItems(getItemsDto);

        return response.success(responseItemsDto);
    }

    /**
     * 점주 입장) 메뉴 등록
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/03
    **/
    public ResponseEntity<?> createItem(@Valid @RequestBody ItemRequestDto.CreateItemDto createItemDto,
                                        BindingResult results) {
        // validation 검증
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        // item 생성
        ItemResponseDto.CreateItemDto responseDto = itemService.createItem(createItemDto);

        return response.success(responseDto, "메뉴가 등록되었습니다." ,HttpStatus.CREATED);
    }

    /**
     * 점주 입장) 메뉴 수정
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/04
    **/
    public ResponseEntity<?> updateItem(@Valid @RequestBody ItemRequestDto.UpdateItemDto updateItemDto,
                                        BindingResult results) {
        // validation 검증
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        // item 수정
        itemService.updateItem(updateItemDto);

        return response.success("메뉴가 수정되었습니다.");
    }

}
