package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.*;
import org.springframework.cache.support.NullValue;
import org.springframework.data.util.NullableUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
@ToString
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
    private Images storeImg;

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

    /**
     * 가게에 메뉴 추가
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/04
    **/
    public void addItem(Item item) {
        if (this.items == null)
            this.items = new ArrayList<>();
        this.items.add(item);
        item.setStore(this);
    }
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
