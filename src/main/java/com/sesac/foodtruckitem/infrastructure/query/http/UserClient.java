package com.sesac.foodtruckitem.infrastructure.query.http;

import com.sesac.foodtruckitem.infrastructure.query.http.dto.CreateUserDto;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.StoreInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service") //apigateway에 등록된 ApplicationName
public interface UserClient {

    @GetMapping("/api/v1/users/{userId}")
    CreateUserDto userInfo(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                           @PathVariable("userId") Long userId);

    @PostMapping("/api/v1/stores")
    void saveStoreInfo(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                              @RequestBody StoreInfo storeInfo);
}
