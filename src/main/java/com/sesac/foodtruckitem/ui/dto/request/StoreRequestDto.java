package com.sesac.foodtruckitem.ui.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StoreRequestDto {

    /**
     * 가게 정보 등록 DTO
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-03
    **/
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateStoreDto {

        private Long userId;
        private String storeName;
        private String phoneNum;
        private String notice;
        private String categoryName;
        private String imgName;
        private String imgUrl;
        private String city;
        private String street;
        private String zipCode;
        private Double latitude;
        private Double longitude;
        private String bNo; // 사업자 등록 번호
        private String sDt; // 개업일
        private String pName; // 대표자 성명
//        private Category category;
//        private Images images;
//        private Address address;
//        private BusinessInfo businessInfo;

        // entity -> dto
        public CreateStoreDto(Store store) {
            this.userId = store.getUserId();
            this.storeName = store.getName();
            this.phoneNum = store.getPhoneNum();
            this.notice = store.getNotice();
            this.city = store.getAddress().getCity();
            this.street = store.getAddress().getStreet();
            this.zipCode = store.getAddress().getZipCode();
            this.latitude = store.getAddress().getLatitude();
            this.longitude = store.getAddress().getLongitude();
            this.categoryName =store.getCategory().getName();
            this.imgName = store.getStoreImage().getImgName();
            this.imgUrl = store.getStoreImage().getImgUrl();
        }
    }
}
