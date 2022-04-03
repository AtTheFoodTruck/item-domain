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
import java.util.Objects;


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
        Store store = storeRepository.findById(getItemsDto.getStoreId()).orElseThrow();  // "해당하는 가게를 찾을 수 없습니다."

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
        Store store = storeRepository.findById(createItemDto.getStoreId()).orElseThrow();  // "해당하는 가게를 찾을 수 없습니다."

        // Item 생성
        Item item = Item.builder()
                .store(store)
                .name(createItemDto.getItemName())
                .description(createItemDto.getDescription())
                .price(createItemDto.getPrice())
                .itemImg(new Images(createItemDto.getItemName(), createItemDto.getItemImg()))
                .build();

        // Item 저장
        Item savedItem = itemRepository.save(item);

        return ItemResponseDto.CreateItemDto.builder().item(savedItem).build();
    }

    @Transactional
    public boolean updateItem(ItemRequestDto.UpdateItemDto updateItemDto) {
        // 가게 정보 조회
        Store store = storeRepository.findById(updateItemDto.getStoreId()).orElseThrow();  // "해당하는 가게를 찾을 수 없습니다."

        // 가게와 수정하려는 메뉴의 가게가 같은지 체크
        if (!Objects.equals(store.getId(), updateItemDto.getStoreId())) {
            return false;
        }

        // Item 조회
        Item item = itemRepository.findById(updateItemDto.getItemId()).orElseThrow();  // "해당하는 메뉴를 찾을 수 없습니다."

        // Item 수정
        item.updateItemInfo(updateItemDto);

        return true;
    }
}
