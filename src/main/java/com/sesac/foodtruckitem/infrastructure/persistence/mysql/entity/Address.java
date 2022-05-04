package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    private String address;
    private String zipCode;
    public static Address of(String address, String zipCode) {
        return Address.builder()
                .address(address)
                .zipCode(zipCode)
                .build();
    }
}