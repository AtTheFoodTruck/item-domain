package com.sesac.foodtruckitem.ui.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class BNoApiRequestDto {

    @Schema(description = "사업자 등록번호")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BNoStatusDto {

        @Schema(description = "사업자 등록번호")
        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        @Length(min = 10, max = 10, message = "사업자 등록번호는 10자리 입니다.")  // 10 자리
        @JsonProperty("b_no")
        private String bNo;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApiReqStatusDto {

        @JsonProperty("b_no")
        private List<String> bNo = new ArrayList<>();

        @Builder
        public ApiReqStatusDto(String bNo) {
            this.bNo.add(bNo);
        }
    }
}
