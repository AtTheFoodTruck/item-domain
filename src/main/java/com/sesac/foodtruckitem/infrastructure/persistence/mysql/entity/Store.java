package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.*;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
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
    private String openTime; // 오픈 시간

    @Embedded
    private Address address;

    @Embedded
    private Images storeImage;

    @Embedded
    private BusinessInfo businessInfo; //사업자 정보
    private Long userId;
    private Long reviewId;
    private Long likeId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "map_id")
    private Map map;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    public void setCategory(Category category) {
        this.category = category;
        category.getStores().add(this);
    }

    public static Store createStore(String name, String phoneNum, Boolean isOpen, String notice,
                                    String openTime, Address address, Images images,
                                    BusinessInfo businessInfo, Map map, Long userId) {
        Store store = Store.builder()
                .name(name)
                .phoneNum(phoneNum)
                .isOpen(isOpen)
                .notice(notice)
                .openTime(openTime)
                .storeImage(images) // Images
                .address(address)   // Address
                .businessInfo(businessInfo) //BusinessInfo
                .map(map)
                .userId(userId)     // 점주 Id
                .build();

        return store;
    }

    public void changeStoreInfo(String notice, Images images, String openTime, Address address, String phoneNum, Map map) {
        this.notice = notice;
        this.phoneNum = phoneNum;
        this.storeImage = images;
        this.openTime = openTime;
        this.address = address;
        this.map = map;
    }

    public void addItem(Item item) {
        if (this.items == null)
            this.items = new ArrayList<>();
        this.items.add(item);
        item.setStore(this);
    }

    public void changeRatingAvg(Double ratingAvg) {
        this.avgRate = ratingAvg;
    }

    public int plusWaitingCount() {
        this.totalWaitingCount += 1;

        return this.totalWaitingCount;
    }
}
