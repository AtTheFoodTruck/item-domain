package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItem_id")
    private Long id;
    private int count;
    private int totalPrice;

    // Item
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item items;

    // Cart
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    /** 생성 메서드 **/
    public static void of(Long cartItemId, int unitPrice, int count) {
        CartItem cartItem = new CartItem();
        cartItem.
    }

    /** 주문상품 전체 가격조회 **/

}
