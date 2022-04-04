package com.sesac.foodtruckitem.ui.controller;

import com.sesac.foodtruckitem.application.service.StoreService;
import com.sesac.foodtruckitem.ui.dto.Helper;
import com.sesac.foodtruckitem.ui.dto.Response;
import com.sesac.foodtruckitem.ui.dto.api.BNoApiRequestDto;
import com.sesac.foodtruckitem.ui.dto.request.StoreRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Tag(name = "items", description = "아이템 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;
    private final Response response;
    private final Helper helper;

    /**
     * 가게 정보 등록 - 점주
     *
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-03
     **/
    @PostMapping("/items/v1/stores")
    public ResponseEntity<?> createStore(HttpServletRequest request,
                                         @RequestBody StoreRequestDto.CreateStoreDto createStoreDto,
                                         @Valid BindingResult results) {

        // validation check
        if (results.hasErrors()) {
            return response.invalidFields(helper.refineErrors(results));
        }

        return storeService.createStore(request, createStoreDto.getUserId(), createStoreDto);
    }

    /**
     * 사업자등록번호 상태조회 API
     *
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-04
     **/
    @PostMapping("/items/v1/managers/status")
    public ResponseEntity<?> businessValidateCheck(@RequestBody BNoApiRequestDto.BNoStatusDto bNoStatusDto,
                                                   @Valid BindingResult results) {
        // validation check
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        if (!storeService.businessValidateCheck(bNoStatusDto)) {
            return response.fail( "인증 실패", HttpStatus.BAD_REQUEST);
        }
        return response.success( "인증 성공");
    }
}
