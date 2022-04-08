package com.sesac.foodtruckitem.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 가게 정보 등록 form data
 * @author jaemin
 * @version 1.0.0
 * 작성일 2022-04-06
**/
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostStoreRequest {
    private Long storeId;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime openTime;//영업 시간

    private String imgName;

    private String imgUrl;

    @NotBlank(message = "영업장소를 입력해주세요")
    private String address;

    @NotNull(message = "우편번호를 입력해주세요")
    private String zipCode;

    private Double latitude;

    private Double longitude;

    @JsonProperty(value = "b_no")
    @NotBlank(message = "사업자 등록번호를 입력해주세요")
    private String bNo; // 사업자 등록 번호

    @JsonProperty(value = "s_dt")
    @NotBlank(message = "개업일을 입력해주세요")
    private String sDt; // 개업일

    @JsonProperty(value = "p_name")
    @NotBlank(message = "대표자 성명을 입력해주세요")
    private String pName; // 대표자 성명

    /**
     * 가게 정보 등록 Dto
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-06
     **/
    public PostStoreDto toPostStoreDto(Long userId) {
        PostStoreDto._PostStoreAddress postStoreAddress =
                PostStoreDto._PostStoreAddress.builder().address(this.address).zipCode(this.zipCode).build();

        PostStoreDto._PostStoreMap postStoreMap =
                PostStoreDto._PostStoreMap.builder().latitude(this.latitude).longitude(this.longitude).build();

        PostStoreDto._PostStoreImages postStoreImages =
                PostStoreDto._PostStoreImages.builder().imgName(this.imgName).imgUrl(this.imgUrl).build();

        PostStoreDto._PostStoreBusinessInfo postStoreBusinessInfo=
                PostStoreDto._PostStoreBusinessInfo.builder().bNo(this.bNo).sDt(this.sDt).pName(this.pName).build();

        return PostStoreDto.builder()
                .userId(userId)
                .storeName(this.storeName)
                .phoneNum(this.phoneNum)
                .notice(this.notice)
                .categoryName(this.categoryName)
                .openTime(this.openTime)
                .address(postStoreAddress)
                .map(postStoreMap)
                .images(postStoreImages)
                .businessInfo(postStoreBusinessInfo)
                .build();
    }
}
