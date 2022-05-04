package com.sesac.foodtruckitem.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.*;
import lombok.*;

import java.time.LocalDateTime;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostStoreRequestDto {
    private Long userId;
    private String storeName;
    private String phoneNum;
    private String notice;
    private String categoryName;
    private String openTime;//영업 시간
    private _PostStoreImages images;
    private _PostStoreAddress address;
    private _PostStoreMap map;
    private _PostStoreBusinessInfo businessInfo;
    @Getter @NoArgsConstructor @Builder
    public static class _PostStoreAddress {
        private String address;
        private String zipCode;

        @Builder
        public _PostStoreAddress(String address, String zipCode) {
            this.address = address;
            this.zipCode = zipCode;
        }

        public static _PostStoreAddress of(Address address) {
            return _PostStoreAddress.builder()
                    .address(address.getAddress())
                    .zipCode(address.getZipCode())
                    .build();
        }
    }

    @Getter @NoArgsConstructor @Builder
    public static class _PostStoreMap {
        private Double latitude;
        private Double longitude;

        @Builder
        public _PostStoreMap(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public static _PostStoreMap of(Map map) {
            return _PostStoreMap.builder()
                    .latitude(map.getLatitude())
                    .longitude(map.getLongitude())
                    .build();
        }
    }

    @Getter @NoArgsConstructor @Builder @AllArgsConstructor
    public static class _PostStoreImages {
        private String imgName;
        private String imgUrl;

        public static _PostStoreImages of(String imgName, String imgUrl) {
            return _PostStoreImages.builder()
                    .imgName(imgName)
                    .imgUrl(imgUrl)
                    .build();
        }
    }

    @Getter @NoArgsConstructor @Builder @AllArgsConstructor
    public static class _PostStoreBusinessInfo {
        private String bNo;
        private String sDt;
        private String pName;

        public static _PostStoreBusinessInfo of(String bNo, String sDt, String pName) {
            return _PostStoreBusinessInfo.builder()
                    .bNo(bNo)
                    .sDt(sDt)
                    .pName(pName)
                    .build();
        }
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateStoreDto {
        private Long storeId;
        private String notice;
        private String imgName;
        private String storeImgUrl;
        private String openTime;
        private String address;
        private String zipCode;
        private Double latitude;
        private Double longitude;
        private String phoneNum;
    }
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    public static class DeleteStoreDto {
        private Long storeId;
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    public static class QueryStoreDto {
        private Long storeId;
    }

}
