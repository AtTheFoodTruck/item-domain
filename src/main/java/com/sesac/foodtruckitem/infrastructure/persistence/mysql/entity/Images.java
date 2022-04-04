package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Images {

    private String img_name;
    private String img_url;
}
