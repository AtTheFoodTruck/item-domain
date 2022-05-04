package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Builder
@Getter
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class BusinessInfo {

    private String bNo; // 사업자 등록 번호
    private String sDt; // 개업일
    private String pName; // 대표자 성명

    public static BusinessInfo of(String bNo, String sDt, String pName) {
        return BusinessInfo.builder()
                .bNo(bNo)
                .sDt(sDt)
                .pName(pName)
                .build();
    }
}
