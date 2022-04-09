package com.sesac.foodtruckitem.ui.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Address;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Images;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Item;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class StoreResponseDto {



    /**
     * 가게 정보 조회 - 가게
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-06
    **/
    @Getter @Builder @AllArgsConstructor
    @ToString
    public static class SearchStoreResult {
        // 가게 정보
        private Long storeId;
        private String storeName;
        private int totalWaitingCount;
        private String notice;
        //Desirialize
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:MM:ss", timezone = "Asia/Seoul")
        private LocalDateTime openTime;
        private Address address;
        private Images images;

        // 메뉴
        private List<SearchItemDto> searchItemResults;

        public static SearchStoreResult of(Store store, List<SearchItemDto> searchItemDto) {

            SearchStoreResult searchStoreResult = SearchStoreResult.builder()
                    .storeId(store.getId())
                    .storeName(store.getName())
                    .totalWaitingCount(store.getTotalWaitingCount())
                    .notice(store.getNotice())
                    .openTime(store.getOpenTime())
                    .address(store.getAddress())
                    .images(store.getStoreImage())
                    .searchItemResults(searchItemDto)
                    .build();

            return searchStoreResult;
        }
    }

    /**
     * 가게정보 조회 - 메뉴DTO
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-09
    **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchItemDto {
        private Long itemId;
        private String itemName;
        private Images images;
        private int itemPrice;

        public static SearchItemDto of(Item item) {
            SearchItemDto itemDto = new SearchItemDto();
            itemDto.itemId = item.getId();
            itemDto.itemName = item.getName();
            itemDto.images = item.getItemImg();
            itemDto.itemPrice = item.getPrice();

            return itemDto;
        }
    }

    /**
     * order도메인에 전달할 가게 정보
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-08
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class StoreInfoDto {
        private Long storeId;
        private String storeName;
        private String imgUrl;

        public static StoreInfoDto of(Store store) {
            StoreInfoDto storeInfoDto = new StoreInfoDto();
            storeInfoDto.storeId = store.getId();
            storeInfoDto.storeName = store.getName();
            storeInfoDto.imgUrl = store.getStoreImage().getImgUrl();

            return storeInfoDto;
        }
    }
}
