package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import com.sesac.foodtruckitem.ui.dto.request.StoreRequestDto;
import lombok.*;
import org.springframework.cache.support.NullValue;
import org.springframework.data.util.NullableUtils;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;
    private String name;
    private String phoneNum;
    private boolean isOpen;
    private String notice;
    private int totalWaitingCount;
    private Double avgRate;
    private LocalDateTime openTime; // 오픈 시간

    @Embedded
    private Address address;

    @Embedded
    private Images storeImage;

    @Embedded
    private BusinessInfo businessInfo; //사업자 정보

    // User
    private Long userId;

    // Category
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // Review
    private Long reviewId;

    // Like
    private Long likeId;

    // Item
    @OneToMany(mappedBy = "store")
    private List<Item> items = new ArrayList<>();

    // 연관관계 메서드 //
    public void setCategory(Category category) {
        this.category = category;
        category.getStores().add(this);
    }

    // 생성 메서드 //
    public static Store createStore(String name, String phoneNum, Boolean isOpen, String notice,
                                    LocalDateTime openTime, Address address, Images images, BusinessInfo businessInfo,
                                    Long userId) {
        Store store = Store.builder()
                .name(name)
                .phoneNum(phoneNum)
                .isOpen(isOpen)
                .notice(notice)
                .openTime(openTime)
                .storeImage(images) // Images
                .address(address)   // Address
                .businessInfo(businessInfo) //BusinessInfo
                .userId(userId)     // 점주 Id
                .build();

        return store;
    }

    // 수정 메서드 //
    public void changeStoreInfo(String notice, Images images, LocalDateTime openTime, Address address, String phoneNum) {
        this.notice = notice;
        this.storeImage = images;
        this.openTime = openTime;
        this.address = address;
        this.phoneNum = phoneNum;
    }

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
