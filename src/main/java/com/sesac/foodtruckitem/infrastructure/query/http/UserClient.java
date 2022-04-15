package com.sesac.foodtruckitem.infrastructure.query.http;

import com.sesac.foodtruckitem.infrastructure.query.http.dto.CreateUserDto;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.StoreInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service") //apigateway에 등록된 ApplicationName
public interface UserClient {

    /**
     * User Domain에서 user정보 받아오기
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-09
    **/
    @GetMapping("/api/v1/info/{userId}")
    CreateUserDto userInfo(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                           @PathVariable("userId") Long userId);

    /**
     * 가게 정보 생성 시 User Domain에 storeId 전달
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-09
    **/
    @PostMapping("/api/v1/stores")
    void saveStoreInfo(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                              @RequestBody StoreInfo storeInfo);
}
