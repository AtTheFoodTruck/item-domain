package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;

@Builder
@Getter
@Embeddable
public class BusinessInfo {

    private String bNo; // 사업자 등록 번호
    private String sDt; // 개업일
    private String pName; // 대표자 성명

    public BusinessInfo() {
    }

    public BusinessInfo(String bNo, String sDt, String pName) {
        this.bNo = bNo;
        this.sDt = sDt;
        this.pName = pName;
    }

    // 생성 메서드 //
    public static BusinessInfo createBusinessInfo(String bNo, String sDt, String pName) {

        BusinessInfo businessInfo = BusinessInfo.builder()
                .bNo(bNo)
                .sDt(sDt)
                .pName(pName)
                .build();

        return businessInfo;
    }
}
