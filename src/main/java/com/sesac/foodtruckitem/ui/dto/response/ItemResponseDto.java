package com.sesac.foodtruckitem.ui.dto.response;

import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Item;
import lombok.*;

public class ItemResponseDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetItemsDto {
        private Long itemId;
        private String itemName;
        private String description;
        private long price;
        private String itemImg;

        @Builder
        public GetItemsDto(Item item) {
            this.itemId = item.getId();
            this.itemName = item.getName();
            this.description = item.getDescription();
            this.price = item.getPrice();
            this.itemImg = item.getItemImg().getImgUrl();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateItemDto {
        private Long itemId;
        private String itemName;
        private String description;
        private long price;
        private String itemImg;

        @Builder
        public CreateItemDto(Item item) {
            this.itemId = item.getId();
            this.itemName = item.getName();
            this.description = item.getDescription();
            this.price = item.getPrice();
            this.itemImg = item.getItemImg().getImgUrl();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class GetItemsInfoDto {
        private Long itemId;
        private String itemName;
        private long itemPrice;
        private String itemImgUrl;

        public static GetItemsInfoDto of(Item item) {
            GetItemsInfoDto getItemsInfoDto = new GetItemsInfoDto();
            getItemsInfoDto.itemId = item.getId();
            getItemsInfoDto.itemName = item.getName();
            getItemsInfoDto.itemPrice = item.getPrice();
            getItemsInfoDto.itemImgUrl = item.getItemImg().getImgUrl();

            return getItemsInfoDto;
        }
    }

    @Data @NoArgsConstructor
    public static class _Page {
        private int startPage;
        private int totalPage;

        public _Page(int startPage, int totalPage) {
            this.startPage = startPage;
            this.totalPage = totalPage;
        }
    }


}