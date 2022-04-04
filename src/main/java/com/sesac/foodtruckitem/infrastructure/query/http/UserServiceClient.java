package com.sesac.foodtruckitem.infrastructure.query.http;

import com.sesac.foodtruckitem.application.vo.CreateUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "user-service") //apigateway에 등록된 ApplicationName
public interface UserServiceClient {

    @GetMapping("/users/info/{userId}")
    CreateUserDto userInfo(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                           @PathVariable Long userId);
}
