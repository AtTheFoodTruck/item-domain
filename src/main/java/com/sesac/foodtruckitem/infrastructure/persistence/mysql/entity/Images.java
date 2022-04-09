package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.*;

import javax.persistence.Embeddable;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class Images {

    private String imgName;
    private String imgUrl;

    // 생성 메서드 //
    public static Images of(String imgName, String imgUrl) {
        return Images.builder()
                .imgName(imgName)
                .imgUrl(imgUrl)
                .build();
    }
}
