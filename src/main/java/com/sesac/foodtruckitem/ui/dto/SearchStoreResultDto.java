package com.sesac.foodtruckitem.ui.dto;

import lombok.Data;

import java.text.DecimalFormat;

@Data
public class SearchStoreResultDto {
    private Long storeId;
    private String storeName;
    private Double distanceMeter;
    private Double avgRating;

    public SearchStoreResultDto(Long storeId, String storeName, Double distanceMeter) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.distanceMeter = distanceMeter;
    }

    public String convertDistanceToString() {
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
