package com.sesac.foodtruckitem.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ItemRequestDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetItemsDto {
        @JsonProperty("store_id")
        private Long storeId;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateItemDto {

        @JsonProperty("user_id")
        private Long userId;

        @NotBlank(message = "메뉴 이름은 필수입니다.")
        @JsonProperty("item_name")
        private String itemName;

        private String description;

        @NotNull(message = "메뉴 가격은 필수입니다.")
        private long price;

        @NotBlank(message = "메뉴 사진은 필수입니다.")
        @JsonProperty("item_img_url")
        private String itemImgUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateItemDto {

        @JsonProperty("user_id")
        private Long userId;

        @JsonProperty("item_id")
        private Long itemId;

        @JsonProperty("item_name")
        private String itemName;

        @JsonProperty("item_img_url")
        private String itemImgUrl;

        private String description;

        private long price;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteItemDto {

        @JsonProperty("user_id")
        private Long userId;

        @JsonProperty("item_id")
        private Long itemId;
    }
}

