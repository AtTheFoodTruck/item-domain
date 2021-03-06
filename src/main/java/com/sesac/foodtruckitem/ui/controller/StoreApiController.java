package com.sesac.foodtruckitem.ui.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sesac.foodtruckitem.application.service.StoreService;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.StoreRepository;
import com.sesac.foodtruckitem.ui.dto.Helper;
import com.sesac.foodtruckitem.ui.dto.Response;
import com.sesac.foodtruckitem.ui.dto.SearchStoreResultDto;
import com.sesac.foodtruckitem.ui.dto.api.BNoApiRequestDto;
import com.sesac.foodtruckitem.ui.dto.request.PostStoreRequestDto;
import com.sesac.foodtruckitem.ui.dto.request.PostStoreRequestFormDto;
import com.sesac.foodtruckitem.ui.dto.request.SearchStoreCondition;
import com.sesac.foodtruckitem.ui.dto.response.ItemResponseDto;
import com.sesac.foodtruckitem.ui.dto.response.StoreResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "items", description = "아이템 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class StoreApiController {

    private final StoreService storeService;
    private final StoreRepository storeRepository;
    private final Response response;
    private final Helper helper;

    @Operation(summary = "고객) 가게 정보 조회")
    @GetMapping("/items/v1/customer/stores/{store_id}")
    public ResponseEntity<?> storeInfo(@PathVariable(value = "store_id") Long storeId,
                                       @PageableDefault(page = 0, size = 10) Pageable pageable) {
        log.info("가게 정보 조회 ");
        StoreResponseDto.SearchStoreResult searchStoreInfo = storeService.findStoreInfo(storeId, pageable);

        return response.success(searchStoreInfo, "가게 정보 조회 성공", HttpStatus.OK);
    }

    @Operation(summary = "점주) 가게 정보 등록")
    @PostMapping("/items/v1/owner/stores")
    public ResponseEntity<?> createStore(HttpServletRequest request,
                                         @RequestBody PostStoreRequestFormDto postStoreRequestFormDto,
                                         @Valid BindingResult results) {

        // validation check
        if (results.hasErrors()) {
            return response.invalidFields(helper.refineErrors(results));
        }

        Long userId = postStoreRequestFormDto.getUserId();

        log.info("userId" + userId );

        return storeService.createStore(request, userId, postStoreRequestFormDto.toPostStoreDto(userId));
    }

    @Operation(summary = "점주) 가게 정보 수정")
    @PatchMapping("/items/v1/owner/stores")
    public ResponseEntity<?> updateStoreInfo(HttpServletRequest request,
                                        @RequestBody PostStoreRequestDto.UpdateStoreDto updateStoreDto,
                                        @Valid BindingResult results) {
        // validation check
        if (results.hasErrors()) {
            return response.invalidFields(helper.refineErrors(results));
        }

        return storeService.updateStoreInfo(request, updateStoreDto);
    }

    @Operation(summary = "점주) 가게 정보 삭제")
    @DeleteMapping("/items/v1/owner/stores")
    public ResponseEntity<?> deleteStoreInfo(@RequestBody PostStoreRequestDto.DeleteStoreDto deleteStoreDto) {
        return storeService.deleteStoreInfo(deleteStoreDto);
    }

    @Operation(summary = "점주) 사업자등록번호 상태 조회 API")
    @PostMapping("/items/v1/owner/status")
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

    @Operation(summary = "고객) 메인페이지")
    @GetMapping("/items/v1/main")
    public ResponseEntity<?> storeMainByRating(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        Page<SearchStoreResultDto.MainStoreResultDto> storeMainDto = storeService.getStoreByRating(pageable);

        ResStoreMainDto resStoreMainDto = new ResStoreMainDto(storeMainDto.getContent(), storeMainDto.getNumber(), storeMainDto.getTotalPages());

        return response.success(resStoreMainDto);
    }

    @Data @NoArgsConstructor
    public static class ResStoreMainDto {
        private List<SearchStoreResultDto.MainStoreResultDto> storeList;
        private _Pages page;

        public ResStoreMainDto(List<SearchStoreResultDto.MainStoreResultDto> storeList, int startPage, long totalPage) {
            this.storeList = storeList;
            page = new _Pages(startPage, totalPage);
        }
    }

    @Data @NoArgsConstructor
    public static class _Pages {
        private int startPage;
        private long totalPage;

        public _Pages(int startPage, long totalPage) {
            this.startPage = startPage;
            this.totalPage = totalPage;
        }
    }
    @ApiOperation(value = "가게명 중복 체크")
    @PostMapping("/items/v1/validation/name")
    public ResponseEntity<?> validateDuplicateStoreName(@RequestBody RequestValidateStoreName name) {
        return storeService.validateDuplicateEmail(name.getStoreName());
    }

    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    @Data
    public static class RequestValidateStoreName {
        private String storeName;
    }

    @Operation(summary = "고겍) 현재 위치 기반 가게 정보 검색")
    @PostMapping("/items/v1/search/stores")
    public ResponseEntity<?> searchStore(HttpServletRequest request,
                                         @RequestBody SearchStoreCondition condition,
                                         @PageableDefault(page = 0, size = 100) Pageable pageable) {
        Page<SearchStoreResultDto> searchStoreResultDtos = storeService.searchStore(request, condition, pageable);

        SearchStoreResponse searchStoreResponse =
                new SearchStoreResponse(searchStoreResultDtos.getContent(), searchStoreResultDtos.getNumber(), searchStoreResultDtos.getTotalPages());

        return response.success(searchStoreResponse);
    }

    @Data
    @NoArgsConstructor
    static class SearchStoreResponse {
        private List<StoreDto> stores;
        private ItemResponseDto._Page page;
//        private boolean hasNext;

        @Data
        @AllArgsConstructor
        static class StoreDto {
            private Long storeId;
            private String storeImgUrl;
            private String storeName;
            private String distance;
            private Double avgRating;
            private String address;
        }

        public SearchStoreResponse(List<SearchStoreResultDto> content, int startPage, int totalPage) {
            this.stores = content.stream()
                    .map(result ->
                            new StoreDto(result.getStoreId(),
                                    result.getStoreImgUrl(),
                                    result.getStoreName(),
                                    result.convertDistanceToString(),
                                    result.getAvgRating(),
                                    result.getAddress()
                            ))
                    .collect(Collectors.toList());
            this.page = new ItemResponseDto._Page(startPage, totalPage);
        }
    }

}
