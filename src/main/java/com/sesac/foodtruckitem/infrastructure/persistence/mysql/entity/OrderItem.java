package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderItem_id")
    private Long id;        // OrderItem_id
    private Long itemId;    // Item_id
    private int price;      // 주문 가격
    private int count;      // 주문 수량

    // Order
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    /** 생성 메서드 **/
    public static void of(Long cartItemId, int unitPrice, int count) {

    }

    /** 주문상품 전체 가격조회 **/
}
