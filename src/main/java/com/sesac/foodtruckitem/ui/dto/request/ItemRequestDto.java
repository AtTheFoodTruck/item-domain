package com.sesac.foodtruckitem.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

        @JsonProperty("user_id")
        private Long userId;

//        @JsonProperty("store_id")
//        private Long storeId;

        @NotBlank(message = "메뉴 이름은 필수입니다.")
        @JsonProperty("item_name")
        private String itemName;

//        @NotBlank(message = "메뉴 설명은 필수입니다.")
        private String description;

        @NotNull(message = "메뉴 가격은 필수입니다.")
        private long price;

        @NotBlank(message = "메뉴 사진은 필수입니다.")
        @JsonProperty("item_img_url")
        private String itemImgUrl;
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

        @JsonProperty("user_id")
        private Long userId;

//        @JsonProperty("store_id")
//        private Long storeId;

        @JsonProperty("item_id")
        private Long itemId;

        @JsonProperty("item_name")
        private String itemName;

        @JsonProperty("item_img_url")
        private String itemImgUrl;

        private String description;

        private long price;

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

        @JsonProperty("user_id")
        private Long userId;

        @JsonProperty("item_id")
        private Long itemId;
    }
}

