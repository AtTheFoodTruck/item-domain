package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import com.sesac.foodtruckitem.ui.dto.request.ItemRequestDto;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;                // 
    private String name;            // 메뉴명 
    private String description;     // 설명
    private int price;              // 가격

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
        if (StringUtils.hasText(itemInfo.getItemName())) {
            this.name = itemInfo.getItemName();
            this.itemImg.setImgUrl(itemInfo.getItemImg());
        }
        // item description
        if (StringUtils.hasText(itemInfo.getDescription())) {
            this.description = itemInfo.getItemName();
        }
        // item price
        if (itemInfo.getPrice() != null) {
            this.price = itemInfo.getPrice();
        }
        // item url
        if (StringUtils.hasText(itemInfo.getItemName())) {
            this.itemImg.setImgUrl(itemInfo.getItemName());
        }
    }
}
