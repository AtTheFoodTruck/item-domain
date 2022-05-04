package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import com.sesac.foodtruckitem.ui.dto.request.ItemRequestDto;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    private String name;
    private String description;
    private long price;

    @Embedded
    private Images itemImg;

    // Store
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public void updateItemInfo(ItemRequestDto.UpdateItemDto itemInfo) {

        // item name
        if (StringUtils.hasText(itemInfo.getItemName())) {
            this.name = itemInfo.getItemName();
            this.itemImg.setImgUrl(itemInfo.getItemImgUrl());
        }
        // item description
        if (StringUtils.hasText(itemInfo.getDescription())) {
            this.description = itemInfo.getItemName();
        }
        // item price
        this.price = itemInfo.getPrice();
        // item url
        if (StringUtils.hasText(itemInfo.getItemName())) {
            this.itemImg.setImgUrl(itemInfo.getItemName());
        }
    }
}
