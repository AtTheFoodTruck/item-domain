package com.sesac.foodtruckitem.application.service;

import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.CartItem;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.CartItemRepository;
import com.sesac.foodtruckitem.infrastructure.query.http.UserClient;
import com.sesac.foodtruckitem.ui.dto.Response;
import com.sesac.foodtruckitem.ui.dto.request.RequestCartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final Response response;
    private final UserClient userClient;

    /**
     * 장바구니에 아이템 담기
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-07
    **/
    @Transactional
    public void addItemToCart(RequestCartDto.PostCartDto cartItemDto, Long storeId, Long userId) {
        
        // CartItem Entity생성
        CartItem.of(cartItemDto.getCartItemId(), cartItemDto.getUnitPrice(), cartItemDto.getCount());

    }
}
