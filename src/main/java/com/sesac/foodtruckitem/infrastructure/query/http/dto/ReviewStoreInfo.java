package com.sesac.foodtruckitem.infrastructure.query.http.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
@Data @Builder
public class ReviewStoreInfo {

    private String storeName;
    private String imgUrl;
    private String imgName;

    public static ReviewStoreInfo of(Store store) {
        return ReviewStoreInfo.builder()
                .storeName(store.getName())
                .imgUrl(store.getStoreImage().getImgUrl())
                .imgName(store.getStoreImage().getImgName())
                .build();
    }
}
