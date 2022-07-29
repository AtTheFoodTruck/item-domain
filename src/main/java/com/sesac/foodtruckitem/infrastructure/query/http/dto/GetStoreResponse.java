package com.sesac.foodtruckitem.infrastructure.query.http.dto;

import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import lombok.Data;

@Data
public class GetStoreResponse {
    private Long storeId;
    private String storeName;
    private String phoneNum;

    public GetStoreResponse(Store store) {
        this.storeId = store.getId();
        this.storeName = store.getName();
        this.phoneNum = store.getPhoneNum();
    }
}
