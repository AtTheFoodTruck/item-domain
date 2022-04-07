package com.sesac.foodtruckitem.infrastructure.query.http;

import com.sesac.foodtruckitem.application.vo.CreateUserDto;
import com.sesac.foodtruckitem.infrastructure.query.http.dto.StoreInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service") //apigateway에 등록된 ApplicationName
public interface UserClient {

    @GetMapping("/users/info/{userId}")
    CreateUserDto userInfo(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                           @PathVariable("userId") Long userId);

    // 점주에 가게정보 업데이트 메서드
    @PostMapping("/users/stores")
    void saveStoreInfo(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                              @RequestBody StoreInfo storeInfo);
}
