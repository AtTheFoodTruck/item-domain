package com.sesac.foodtruckitem.ui.dto;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;

@Slf4j
@Data
@ToString
public class SearchStoreResultDto {
    private Long storeId;
    private String storeImgUrl;
    private String storeName;
    private Double distanceMeter;
    private Double avgRating;

    public SearchStoreResultDto(Long storeId, String storeImgUrl, String storeName, Double distanceMeter) {
        this.storeId = storeId;
        this.storeImgUrl = storeImgUrl;
        this.storeName = storeName;
        this.distanceMeter = distanceMeter;
    }

    public String convertDistanceToString() {
        log.info("distanceMeter : " + distanceMeter);
        // km 로 표시
        if (distanceMeter >= 1000) {
            double km = distanceMeter * 0.001;
            String format = new DecimalFormat("0.0").format(km);

            return format + "km";
        }

        return new DecimalFormat("0").format(distanceMeter) + "m";
    }

    public void changeAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }
}
