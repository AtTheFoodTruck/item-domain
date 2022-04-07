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


    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StoreDto {
        //가게 정보
        private Long storeId;
        private String storeName;
        private int totalWaitingCount;
        private String notice;
        // Desirialize해야됨
        private LocalDateTime openTime;
        private Address address;
        private Images storeIamges;
//        private String city;
//        private String street;
//        private String zipcode;
//        private String latitude;
//        private String longitude;

        // 메뉴
        private Long itemId;
        private String itemName;
        private Images images;
//        private String itemImgName;
//        private String itemImgUrl;
//        private String itemName;
        private int itemPrice;

    }

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
     * 가게 정보 조회 - 메뉴
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-06
     **/
    @Getter @Builder @AllArgsConstructor
    public static class SearchItemResult {
        // 메뉴 정보
        private Long itemId;
        private String itemName;
        private String imageName;
        private String imageUrl;
        private int itemPrice;

//        private List<SearchItemDto> searchItemDtos;

        public static SearchItemResult of(Item item) {
            return SearchItemResult.builder()
                    .itemId(item.getId())
                    .itemName(item.getName())
                    .imageName(item.getItemImg().getImgName())
                    .imageUrl(item.getItemImg().getImgUrl())
                    .build();
        }
    }
}
