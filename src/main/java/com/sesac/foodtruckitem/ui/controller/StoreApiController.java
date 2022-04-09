package com.sesac.foodtruckitem.ui.controller;

import com.sesac.foodtruckitem.application.service.StoreService;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.StoreRepository;
import com.sesac.foodtruckitem.ui.dto.Helper;
import com.sesac.foodtruckitem.ui.dto.Response;
import com.sesac.foodtruckitem.ui.dto.Result;
import com.sesac.foodtruckitem.ui.dto.api.BNoApiRequestDto;
import com.sesac.foodtruckitem.ui.dto.request.PostStoreRequestDto;
import com.sesac.foodtruckitem.ui.dto.request.PostStoreRequestFormDto;
import com.sesac.foodtruckitem.ui.dto.response.StoreResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "items", description = "아이템 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class StoreApiController {

    private final StoreService storeService;
    private final StoreRepository storeRepository;
    private final Response response;
    private final Helper helper;

    /**
     * 가게 정보 등록 - 점주
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-03
     **/
    @PostMapping("/items/v1/stores")
    public ResponseEntity<?> createStore(HttpServletRequest request,
                                         @RequestBody PostStoreRequestFormDto postStoreRequestFormDto,
                                         @Valid BindingResult results) {

        log.info("Request Form : {} ", postStoreRequestFormDto);


        // validation check
        if (results.hasErrors()) {
            return response.invalidFields(helper.refineErrors(results));
        }

        Long userId = postStoreRequestFormDto.getUserId();

        log.info("userId" + userId );

        return storeService.createStore(request, userId, postStoreRequestFormDto.toPostStoreDto(userId));
    }

    /**
     * 가게 정보 수정 - 점주
     *
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-04
     **/
    @PatchMapping("/items/v1/stores")
    public ResponseEntity<?> updateStoreInfo(HttpServletRequest request,
                                        @RequestBody PostStoreRequestDto.UpdateStoreDto updateStoreDto,
                                        @Valid BindingResult results) {
        // validation check
        if (results.hasErrors()) {
            return response.invalidFields(helper.refineErrors(results));
        }

        return storeService.updateStoreInfo(request, updateStoreDto);
    }

    /**
     * 가게 정보 삭제 - 점주
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-05
    **/
    @DeleteMapping("/items/v1/stores")
    public ResponseEntity<?> deleteStoreInfo(@RequestBody PostStoreRequestDto.DeleteStoreDto deleteStoreDto) {
        return storeService.deleteStoreInfo(deleteStoreDto);
    }

    /**
     * 가게 정보 조회
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-05
    **/
    @GetMapping("/items/v1/stores")
    public ResponseEntity<?> storeInfo(@RequestBody PostStoreRequestDto.QueryStoreDto queryStoreDto,
                                       @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Long storeId = queryStoreDto.getStoreId();
        StoreResponseDto.SearchStoreResult searchStoreInfo = storeService.findStoreInfo(storeId, pageable);

        return response.success(searchStoreInfo, "가게 정보 조회 성공", HttpStatus.OK);
    }


    /**
     * 사업자등록번호 상태조회 API
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
            return response.fail("인증 실패", HttpStatus.BAD_REQUEST);
        }
        return response.success("인증 성공");
    }
}
