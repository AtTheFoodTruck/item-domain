package com.sesac.foodtruckitem.application.service;

import com.sesac.foodtruckitem.application.vo.CreateUserDto;
import com.sesac.foodtruckitem.exception.DuplicateStoreNameException;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.*;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.CategoryRepository;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.StoreRepository;
import com.sesac.foodtruckitem.infrastructure.query.http.UserServiceClient;
import com.sesac.foodtruckitem.ui.dto.Response;
import com.sesac.foodtruckitem.ui.dto.api.BNoApiRequestDto;
import com.sesac.foodtruckitem.ui.dto.request.StoreRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final Response response;
    private final UserServiceClient userServiceClient;
    private final RestTemplate restTemplate;

    /**
     * 가게정보 등록 - 점주
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-03
     **/
    @Transactional
    public ResponseEntity<?> createStore(HttpServletRequest request,
                                         Long userId,
                                         StoreRequestDto.CreateStoreDto createStoreDto) {

        String authorization = request.getHeader("Authorization");

        // 1. user 정보 갖고오기, using feign client
        CreateUserDto createUserDto = userServiceClient.userInfo(authorization, userId);

        log.info("Return 받은 user 객체의 값 : {}", createUserDto);

        // 2. 이미 푸드트럭을 등록한 점주인지 체크
        int count = storeRepository.countByUserId(createStoreDto.getUserId());
        if (count > 0) {
            return response.fail("이미 등록된 푸드트럭 가게 정보가 존재합니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Category 정보 조회
        Category findCategory = categoryRepository.findByName(createStoreDto.getCategoryName()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 카테고리 정보가 없습니다 " + createStoreDto.getCategoryName())
        );

        // 2. user의 정보가 Manager인지
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authorities = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(","));

        if (!authorities.equals("ROLE_MANAGER")) {
            return response.fail("점주만 가게 정보 등록이 가능합니다.", HttpStatus.FORBIDDEN);
        }*/

        // 3. dto -> entity
        // Address 생성
        Address address = Address.createAddress(
                createStoreDto.getCity(),
                createStoreDto.getStreet(),
                createStoreDto.getZipCode(),
                createStoreDto.getLatitude(),
                createStoreDto.getLongitude()
        );

        // Images 생성
        Images images = Images.createImages(
                createStoreDto.getImgName(),
                createStoreDto.getImgUrl()
        );

        // BusinessInfo 생성
        BusinessInfo businessInfo = BusinessInfo.createBusinessInfo(
                createStoreDto.getBNo(),
                createStoreDto.getSDt(),
                createStoreDto.getPName()
        );

        // Store 생성
        Store store = Store.createStore(
                createStoreDto.getStoreName(),
                createStoreDto.getPhoneNum(),
                true,
                createStoreDto.getNotice(),
                createStoreDto.getOpenTime(),
                address,
                images,
                businessInfo,
                createUserDto.getUserId()
        );

        // 3. 가게 정보 등록
        Store savedStore = storeRepository.save(store);

        // 4. 카테고리 등록 및 연관관계 설정
        savedStore.setCategory(findCategory);

        return response.success(new StoreRequestDto.CreateStoreDto(savedStore), "가게 정보가 저장되었습니다.", HttpStatus.CREATED);
    }

    /**
     * 사업자등록번호 상태 조회
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022-04-04
    **/
    public boolean businessValidateCheck(BNoApiRequestDto.BNoStatusDto bNoStatusDto) {
        // UriComponents
        URI uriComponents = UriComponentsBuilder
                .fromHttpUrl("https://api.odcloud.kr/api/nts-businessman/v1/status")
                .queryParam("serviceKey", "JyZTPPmD5XHt0PIhYecvp1xIsQj%2B1kU%2Btw4P%2Be2UHoqKCIdQ2gM5aQvJCGDrWh4LRE9fv7YOZIlNuj2o0asNDA%3D%3D")
                .build(true)
                .encode()
                .toUri();

        // HttpEntity(body, header)
        BNoApiRequestDto.ApiReqStatusDto dto = BNoApiRequestDto.ApiReqStatusDto.builder()
                .bNo(bNoStatusDto.getBNo())
                .build();

        HttpEntity<BNoApiRequestDto.ApiReqStatusDto> entity = new HttpEntity<>(dto, new HttpHeaders());

        // Request API
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(uriComponents, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }

        // Json Parsing
        JSONParser jsonParser = new JSONParser();
        JSONObject body;
        try {
            body = (JSONObject) jsonParser.parse(response.getBody());
        } catch (ParseException e) {
            log.warn("Json Parsing Error: 사업자등록번호 상태조회 실패");
            return false;
        }

        // status 가 OK 거나, 등록된 사용자 ("match_cnt" = 1)
        return "OK".equals(body.get("status_code")) && body.containsKey("match_cnt");
    }
}
