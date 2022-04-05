package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;

@Builder
@Getter
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipCode;
    private Double latitude;
    private Double longitude;

    public Address() {
    }

    public Address(String city, String street, String zipCode, Double latitude, Double longitude) {
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // 생성 메서드 //
    public static Address createAddress(String city, String street, String zipCode, Double latitude, Double longitude) {
        Address address = Address.builder()
                .city(city)
                .street(street)
                .zipCode(zipCode)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        return address;
    }
}