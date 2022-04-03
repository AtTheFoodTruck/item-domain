package com.sesac.foodtruckitem.application.service;


import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Images;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Item;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.ItemRepository;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.StoreRepository;
import com.sesac.foodtruckitem.ui.dto.request.ItemRequestDto;
import com.sesac.foodtruckitem.ui.dto.response.ItemResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final StoreRepository storeRepository;

    /**
     * 해당 가게의 메뉴 리스트 조회
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/04
    **/
    @Transactional
    public List<ItemResponseDto.GetItemsDto> getItems(ItemRequestDto.GetItemsDto getItemsDto) {
        // 가게 정보 조회
        Store store = storeRepository.findById(getItemsDto.getStore_id()).orElseThrow();  // "해당하는 가게를 찾을 수 없습니다."

        // 해당 가게 메뉴 리스트 조회
        List<Item> items = itemRepository.findAllByStoreOrderByCreatedDate(store);

        // Dto 변환
        List<ItemResponseDto.GetItemsDto> responseItemsDto = new ArrayList<>();
        for (Item item : items) {
            responseItemsDto.add(ItemResponseDto.GetItemsDto.builder()
                    .item(item)
                    .build());
        }

        return responseItemsDto;
    }

    /**
     * 해당 가게의 메뉴 등록
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/04
    **/
    @Transactional
    public ItemResponseDto.CreateItemDto createItem(ItemRequestDto.CreateItemDto createItemDto) {
        // 가게 정보 조회
        Store store = storeRepository.findById(createItemDto.getStore_id()).orElseThrow();  // "해당하는 가게를 찾을 수 없습니다."

        // Item 생성
        Item item = Item.builder()
                .store(store)
                .name(createItemDto.getItem_name())
                .description(createItemDto.getDescription())
                .price(createItemDto.getPrice())
                .itemImg(new Images(createItemDto.getItem_name(), createItemDto.getItem_img()))
                .build();

        // Item 저장
        Item savedItem = itemRepository.save(item);

        return ItemResponseDto.CreateItemDto.builder().item(savedItem).build();
    }
}
