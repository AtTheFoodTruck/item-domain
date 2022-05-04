package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class Images {

    private String imgName;

    @Column(length= 100000000)
    private String imgUrl;

    public static Images of(String imgName, String imgUrl) {
        return Images.builder()
                .imgName(imgName)
                .imgUrl(imgUrl)
                .build();
    }
}
