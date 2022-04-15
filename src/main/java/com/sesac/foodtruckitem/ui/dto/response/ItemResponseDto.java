package com.sesac.foodtruckitem.ui.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Item;
import lombok.*;

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

        private long price;

        @JsonProperty("item_img")
        private String itemImg;

        @Builder
        public GetItemsDto(Item item) {
            this.itemId = item.getId();
            this.itemName = item.getName();
            this.description = item.getDescription();
            this.price = item.getPrice();
            this.itemImg = item.getItemImg().getStoreImgUrl();
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

        private long price;

        @JsonProperty("item_img")
        private String itemImg;

        @Builder
        public CreateItemDto(Item item) {
            this.itemId = item.getId();
            this.itemName = item.getName();
            this.description = item.getDescription();
            this.price = item.getPrice();
            this.itemImg = item.getItemImg().getStoreImgUrl();
        }
    }

    /**
     * Item 목록 조회 응답 DTO - Feign Client
     *
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-09
     **/
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class GetItemsInfoDto {
        private Long itemId;        // 아이템 ID
        private String itemName;    // 아이템 이름
        private long itemPrice;     // 아이템 가격
        private String itemImgUrl;  // 아이템 이미지 주소

        public static GetItemsInfoDto of(Item item) {
            GetItemsInfoDto getItemsInfoDto = new GetItemsInfoDto();
            getItemsInfoDto.itemId = item.getId();
            getItemsInfoDto.itemName = item.getName();
            getItemsInfoDto.itemPrice = item.getPrice();
            getItemsInfoDto.itemImgUrl = item.getItemImg().getStoreImgUrl();

            return getItemsInfoDto;
        }
    }

    @Data @NoArgsConstructor
    public static class _Page {
        private int startPage;
        private int endPage;

        public _Page(int startPage, int endPage) {
            this.startPage = startPage;
            this.endPage = endPage;
        }
    }


}