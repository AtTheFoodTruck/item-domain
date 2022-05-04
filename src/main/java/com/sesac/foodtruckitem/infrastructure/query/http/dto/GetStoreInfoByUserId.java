package com.sesac.foodtruckitem.infrastructure.query.http.dto;

import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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