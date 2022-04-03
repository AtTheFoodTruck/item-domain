package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import com.sesac.foodtruckitem.ui.dto.request.ItemRequestDto;
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
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    private String name;
    private String description;
    private int price;

    @Embedded
    private Images itemImg;

    // Store
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // CartItem
    @OneToMany(mappedBy = "item")
    private List<CartItem> cartItems = new ArrayList<>();

    /**
     * 메뉴 수정
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/04
    **/
    public void updateItemInfo(ItemRequestDto.UpdateItemDto itemInfo) {
        // item name
        if (itemInfo.getItemName() != null) {
            this.name = itemInfo.getItemName();
            this.itemImg.setImgName(itemInfo.getItemName());
        }
        // item description
        if (itemInfo.getDescription() != null) {
            this.description = itemInfo.getItemName();
        }
        // item price
        if (itemInfo.getPrice() != null) {
            this.price = itemInfo.getPrice();
        }
        // item url
        if (itemInfo.getItemName() != null) {
            this.itemImg.setImgUrl(itemInfo.getItemName());
        }
    }
}
