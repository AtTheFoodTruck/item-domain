package com.sesac.foodtruckitem.ui.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ItemResponseDto {


    /**
     * 메뉴 조회 response DTO
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/03
    **/
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetItemsDto {

        @JsonProperty("item_id")
        private Long itemId;

        @JsonProperty("item_name")
        private String itemName;

        private String description;

        private int price;

        @JsonProperty("item_img")
        private String itemImg;

        @Builder
        public GetItemsDto(Item item) {
            this.itemId = item.getId();
            this.itemName = item.getName();
            this.description = item.getDescription();
            this.price = item.getPrice();
            this.itemImg = item.getItemImg().getImg_url();
        }
    }



    /**
     * 메뉴 등록 response DTO
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/03
     **/
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateItemDto {

        @JsonProperty("item_id")
        private Long itemId;

        @JsonProperty("item_name")
        private String itemName;

        private String description;

        private int price;

        @JsonProperty("item_img")
        private String itemImg;

        @Builder
        public CreateItemDto(Item item) {
            this.itemId = item.getId();
            this.itemName = item.getName();
            this.description = item.getDescription();
            this.price = item.getPrice();
            this.itemImg = item.getItemImg().getImg_url();
        }
    }
}