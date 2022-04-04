package com.sesac.foodtruckitem.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

        @NotBlank(message = "가게명을 입력해주세요")
        private String storeName;

        @NotBlank(message = "가게 전화번호를 입력해주세요")
        private String phoneNum;

        @NotBlank(message = "공지를 입력해주세요")
        private String notice;

        @NotBlank(message = "카테고리를 입력해주세요")
        private String categoryName;

        // JsonFormat을 이용해 직렬화
        @NotBlank(message = "영업시간을 입력해주세요")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:MM:ss", timezone = "Asia/Seoul")
        private LocalDateTime openTime;//영업 시간

        private String imgName;

        private String imgUrl;

        @NotBlank(message = "영업장소를 입력해주세요")
        private String city;

        @NotNull(message = "영업장소를 입력해주세요")
        private String street;

        @NotNull(message = "우편번호를 입력해주세요")
        private String zipCode;

        private Double latitude;

        private Double longitude;

        @NotBlank(message = "사업자 등록번호를 입력해주세요")
        private String bNo; // 사업자 등록 번호

        @NotBlank(message = "개업일을 입력해주세요")
        private String sDt; // 개업일

        @NotBlank(message = "대표자 성명을 입력해주세요")
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
