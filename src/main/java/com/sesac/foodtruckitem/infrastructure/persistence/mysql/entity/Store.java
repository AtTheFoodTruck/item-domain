package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import com.sesac.foodtruckitem.ui.dto.request.StoreRequestDto;
import lombok.*;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;
    private String name;            // 가게명
    private String phoneNum;        // 가게 번호
    private boolean isOpen;         // 오픈 여부
    private String notice;          // 가게 공지사항
    private int totalWaitingCount;  //평균 대기 시간
    private Double avgRate;         // 평균 별점
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


//
//    // dto -> entity
//    public Store toEntity(StoreRequestDto.CreateStoreDto createStoreDto) {
//        return Store.builder()
//                .name(createStoreDto.getStoreName())
//                .phoneNum(createStoreDto.getPhoneNum())
//                .isOpen(true)
//                .notice(createStoreDto.getNotice())
//                .category(createStoreDto.getCategory())
//                .storeImage(createStoreDto.getImages())
//                .address(createStoreDto.getAddress())
//                .businessInfo(createStoreDto.getBusinessInfo())
//                .build();
//    }


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
