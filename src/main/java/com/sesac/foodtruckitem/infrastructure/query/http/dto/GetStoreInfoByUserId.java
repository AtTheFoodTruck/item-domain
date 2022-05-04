package com.sesac.foodtruckitem.infrastructure.query.http.dto;

import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * order 도메인에 전달할 가게 정보
 * 주문 조회 페이지(점주)
 * @author jjaen
 * @version 1.0.0
 * 작성일 2022/04/11
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetStoreInfoByUserId {
    private Long storeId;
    private String storeName;

    public static GetStoreInfoByUserId of(Store store) {
        GetStoreInfoByUserId getStoreInfoByUserId = new GetStoreInfoByUserId();
        getStoreInfoByUserId.storeId = store.getId();
        getStoreInfoByUserId.storeName = store.getName();

        return getStoreInfoByUserId;
    }

}