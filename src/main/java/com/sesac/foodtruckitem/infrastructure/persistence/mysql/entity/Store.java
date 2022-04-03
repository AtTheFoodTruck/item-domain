package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.*;

import javax.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
public class Store extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;
    private String name;
    private String phoneNum;
    private boolean isOpen;
    private int totalWaitingCount;
    private String notice;
    private Double avgRate;
    private String bNo; // 사업자등록번호(10)
    private String pNm; // 사업자명
    private String sDt; // 개업일

    @Embedded
    private Address address;

    @Embedded
    private Image storeImage;

    // User
    private Long userId;

    // Review
    private Long reviewId;

    // Category
    private Long categoryId;

    // Like
    private Long likeId;

    // ItemList
    @OneToMany(mappedBy = "store")
    private List<Item> items = new ArrayList<>();
//    // User
//    @OneToOne(fetch = LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    // Review
//    @OneToMany(mappedBy = "store")
//    private List<Review> reviews = new ArrayList<>();
//
//    // Category
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "category_id")
//    private Category category;
//
//    // Like
//    @OneToMany(mappedBy = "store")
//    private List<Like> likes = new ArrayList<>();
}
