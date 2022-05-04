package com.sesac.foodtruckitem.infrastructure.query.http.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

//@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class ReqReviewInfoDto {
    private Long storeId;
    private Double avgRating;
}
