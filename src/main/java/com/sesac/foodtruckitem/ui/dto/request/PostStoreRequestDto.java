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
    private LocalDateTime openTime;//영업 시간
    private _PostStoreImages images;
    private _PostStoreAddress address;
    private _PostStoreMap map;
    private _PostStoreBusinessInfo businessInfo;
//    private String businessNumber; // 사업자 등록 번호
//    private String startDate; // 개업일
//    private String partnerName; // 대표자 성명

    /**
     * 가게 정보 저장에 필요한 주소 정보를 DTO로 변환
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-06
    **/
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

    /**
     * 가게 정보 저장에 필요한 Map 정보를 DTO로 변환
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-06
     **/
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

    /**
     * 가게 정보 저장에 필요한 Image 정보를 DTO로 변환
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-06
     **/
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

    /**
     * 가게 정보 저장에 필요한 BusinessInfo 정보를 DTO로 변환
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-06
     **/
    @Getter @NoArgsConstructor @Builder @AllArgsConstructor
    public static class _PostStoreBusinessInfo {
        private String bNo;
        private String sDt;
        private String pName; // 사업자 등록 번호

        public static _PostStoreBusinessInfo of(String bNo, String sDt, String pName) {
            return _PostStoreBusinessInfo.builder()
                    .bNo(bNo)
                    .sDt(sDt)
                    .pName(pName)
                    .build();
        }
    }

    /**
     * 가게 정보 수정 DTO
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-04
     **/
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateStoreDto {
        // 가게 ID
        private Long storeId;

        // 공지사항
        private String notice;

        // 사진
        private String imgName;
        private String storeImgUrl;

        // 영업시간
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:MM:ss", timezone = "Asia/Seoul")
        private LocalDateTime openTime;

        // 영업 장소
        private String address;
        private String zipCode;
        private Double latitude;
        private Double longitude;

        // 전화번호
        private String phoneNum;
    }

    /**
     * 가게 삭제 DTO
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-05
    **/
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    public static class DeleteStoreDto {
        // 가게 ID
        private Long storeId;
    }

    /**
     * 가게 정보 조회 DTO
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-05
     **/
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    public static class QueryStoreDto {
        // 가게 ID
        private Long storeId;
    }

}
