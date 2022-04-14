package com.sesac.foodtruckitem.ui.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class SearchStoreCondition {
    @NotNull(message = "필수 값입니다.")
    private double latitude;
    @NotNull(message = "필수 값입니다.")
    private double longitude;
    private String storeName;
}
