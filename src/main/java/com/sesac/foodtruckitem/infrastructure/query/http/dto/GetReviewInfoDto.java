package com.sesac.foodtruckitem.infrastructure.query.http.dto;

import lombok.Data;

@Data
public class GetReviewInfoDto {
    // 각 푸드트럭Id에 대한 평균 별점을 가져와야함
    private Long storeId;
    private Double avgRating;
}
