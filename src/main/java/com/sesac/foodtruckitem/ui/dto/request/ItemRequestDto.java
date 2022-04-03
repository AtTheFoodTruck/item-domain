package com.sesac.foodtruckitem.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
        @NotBlank(message = "가게 아이디는 필수입니다.")
        @JsonProperty("store_id")
        private Long storeId;
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
        @NotBlank(message = "가게 아이디는 필수입니다.")
        @JsonProperty("store_id")
        private Long storeId;

        @NotBlank(message = "메뉴 이름은 필수입니다.")
        @JsonProperty("item_name")
        private String itemName;

        @NotBlank(message = "메뉴 설명은 필수입니다.")
        private String description;

        @NotBlank(message = "메뉴 가격은 필수입니다.")
        private int price;

        @NotBlank(message = "메뉴 사진은 필수입니다.")
        @JsonProperty("item_img")
        private String itemImg;
    }


    /**
     * 메뉴 수정 request DTO
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/03
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateItemDto {
        @NotBlank(message = "가게 아이디는 필수입니다.")
        @JsonProperty("store_id")
        private Long storeId;

        @NotBlank(message = "메뉴 아이디는 필수입니다.")
        @JsonProperty("item_id")
        private Long itemId;

        @JsonProperty("item_name")
        private String itemName;

        private String description;

        private Integer price;

        @JsonProperty("item_img")
        private String itemImg;
    }

    /**
     * 메뉴 삭제 request DTO
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/03
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteItemDto {
        @NotBlank(message = "가게 아이디는 필수입니다.")
        @JsonProperty("store_id")
        private Long storeId;

        @NotBlank(message = "메뉴 아이디는 필수입니다.")
        @JsonProperty("item_id")
        private Long itemId;
    }
}

