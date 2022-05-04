package com.sesac.foodtruckitem.infrastructure.query.http;

import com.sesac.foodtruckitem.infrastructure.query.http.dto.GetReviewInfoDto;
import com.sesac.foodtruckitem.ui.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FeignClient(name = "order-service") //apigateway에 등록된 ApplicationName
public interface OrderClient {

    /**
     * Order Domain에서 Review 정보 가져오기
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-09
     **/
    @GetMapping("/api/v1/reviews/{storeId}")
    Result<List<GetReviewInfoDto>> getReviewInfo(@RequestHeader(value="Authorization", required = true) String authorizationHeader,
                                                 @PathVariable("storeId") Iterable<Long> storeId);

    default Map<Long, Double> getReviewInfos(String authorization, Iterable<Long> storeIds) {
        if(!storeIds.iterator().hasNext()) return null;

        List<GetReviewInfoDto> reviewInfo = this.getReviewInfo(authorization, storeIds).getData();

        return reviewInfo.stream()
                .collect(
                        Collectors.toMap(
                                getReviewInfoDto -> getReviewInfoDto.getStoreId(),
                                getReviewInfoDto -> getReviewInfoDto.getAvgRating()
                        )
                );
    }
}
