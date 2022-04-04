package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;

@Builder
@Embeddable
@Getter
public class Images {

    private String imgName;
    private String imgUrl;

    public Images() {
    }

    public Images(String imgName, String imgUrl) {
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    // 생성 메서드 //
    public static Images createImages(String imgName, String imgUrl) {
        Images build = Images.builder()
                .imgName(imgName)
                .imgUrl(imgUrl)
                .build();

        return build;
    }
}
