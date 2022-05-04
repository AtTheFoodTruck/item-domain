package com.sesac.foodtruckitem.ui.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sesac.foodtruckitem.application.service.ItemService;
import com.sesac.foodtruckitem.ui.dto.Helper;
import com.sesac.foodtruckitem.ui.dto.Response;
import com.sesac.foodtruckitem.ui.dto.request.ItemRequestDto;
import com.sesac.foodtruckitem.ui.dto.response.ItemResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "items", description = "아이템 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class ItemApiController {

    private final ItemService itemService;
    private final Response response;

    @Operation(summary = "점주) 메뉴 관리 페이지 조회")
    @GetMapping("/items/v1/owner/item/{user_id}")
    public ResponseEntity<?> getItems(//@Valid @RequestBody ItemRequestDto.GetItemsDto getItemsDto, BindingResult results,
                                      @PathVariable("user_id") Long userId,
                                      @PageableDefault(page = 0, size = 10)Pageable pageable) {
        Page<ItemResponseDto.GetItemsDto> responseItemsPageDto = itemService.getOwnerItemsInfo(userId, pageable);
        ResponseItemDto responseItemDto = new ResponseItemDto(responseItemsPageDto.getContent(), responseItemsPageDto.getNumber(), responseItemsPageDto.getTotalPages());

        return response.success(responseItemDto);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ResponseItemDto {
        private List<ItemResponseDto.GetItemsDto> itemsDto;
        private ItemResponseDto._Page page;

        public ResponseItemDto(List<ItemResponseDto.GetItemsDto> itemsDto, int startPage, int totalPage) {
            this.itemsDto = itemsDto;
            this.page = new ItemResponseDto._Page(startPage, totalPage);
        }
    }

    @Operation(summary = "고객) 메뉴 등록")
    @PostMapping("/items/v1/owner/item")
    public ResponseEntity<?> createItem(@Valid @RequestBody ItemRequestDto.CreateItemDto createItemDto,
                                        BindingResult results) {
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        ItemResponseDto.CreateItemDto responseDto = itemService.createItem(createItemDto);

        return response.success(responseDto,"메뉴가 등록되었습니다." ,HttpStatus.CREATED);
    }

    @Operation(summary = "고객) 메뉴 수정")
    @PatchMapping("/items/v1/owner/item")
    public ResponseEntity<?> updateItem(@Valid @RequestBody ItemRequestDto.UpdateItemDto updateItemDto,
                                        BindingResult results) {
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        if (!itemService.updateItem(updateItemDto))
            return response.fail("메뉴를 수정할 수 없습니다.", HttpStatus.BAD_REQUEST);

        return response.success("메뉴가 수정되었습니다.");
    }

    @Operation(summary = "고객) 메뉴 삭제")
    @DeleteMapping("/items/v1/owner/item")
    public ResponseEntity<?> deleteItem(@RequestBody ItemRequestDto.DeleteItemDto deleteItemDto,
                                        BindingResult results) {
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        if (!itemService.deleteItem(deleteItemDto))
            return response.fail("메뉴를 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST);

        return response.success("메뉴가 삭제되었습니다.");
    }
}
