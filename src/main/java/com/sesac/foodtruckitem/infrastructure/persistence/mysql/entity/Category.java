package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;


@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Store> stores = new ArrayList<>();

    // 생성 메서드 //
    public static Category createCategory(String name) {
        Category category = Category.builder()
                .name(name)
                .build();

        return category;
    }
}
