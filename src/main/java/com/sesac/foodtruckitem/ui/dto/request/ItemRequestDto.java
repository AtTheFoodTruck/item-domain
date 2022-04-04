package com.sesac.foodtruckitem.ui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ItemRequestDto {

    /**
     * 메뉴 조회 request DTO
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/03
    **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetItemsDto {
        private Long store_id;
    }

    /**
     * 메뉴 등록 request DTO
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/03
    **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateItemDto {
        private Long store_id;
        private String item_name;
        private String description;
        private int price;
        private String item_img;
    }
}

