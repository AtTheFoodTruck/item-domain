package com.sesac.foodtruckitem.ui.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

public class RequestCartDto {

    /**
     * 장바구니에 아이템 담기 요청 폼 DTO
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-07
     **/
    @Data
    public class RequestItem {
        private Long itemId;    // 상품ID
        private int price;     // 상품 가격
        private int count;     // 상품 수량
        private Long storeId;   // 가게ID
        private Long userId;    // 유저ID
    }

    /**
     * 장바구니에 상품 담기 DTO
     *
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-07
     **/
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PostCartDto {
        private Long cartItemId;    // cartItem ID
        private Long storeId;       // 가게 ID
        private Long itemId;        // 상품 ID
//        private String itemName;    // 상품 Name
        private int unitPrice;      // 상품 단위 가격
        private int count;          // 상품 수량

        // cartItem_id, item_id, price, count
        public static PostCartDto of(Long cartItemId, Long storeId, Long itemId,
                                     int unitPrice, int count) {
            return PostCartDto.builder()
                    .cartItemId(cartItemId)
                    .storeId(storeId)
                    .itemId(itemId)
//                    .itemName(itemName)
                    .unitPrice(unitPrice)
                    .count(count)
                    .build();
        }
    }

}
