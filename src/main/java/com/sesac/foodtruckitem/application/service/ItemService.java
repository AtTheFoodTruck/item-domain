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
import org.springframework.dao.EmptyResultDataAccessException;
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
        Store store = storeRepository.findById(getItemsDto.getStoreId())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당하는 가게를 찾을 수 없습니다.", 1));

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

        Long userId = createItemDto.getUserId();

        // 가게 정보 조회
        Store store = storeRepository.findById(createItemDto.getStoreId())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당하는 가게를 찾을 수 없습니다.", 1));

        // 점주 일치 여부 파악
        if (!store.getUserId().equals(userId)) {
            throw new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
        }

        // Item 생성
        Item item = Item.builder()
                .name(createItemDto.getItemName())
                .description(createItemDto.getDescription())
                .price(createItemDto.getPrice())
                .itemImg(new Images(createItemDto.getItemName(), createItemDto.getItemImgUrl()))
                .build();

        // Item 저장
        Item savedItem = itemRepository.save(item);

        // 가게에 메뉴 저장
        store.addItem(item);
        storeRepository.save(store);

        return ItemResponseDto.CreateItemDto.builder().item(savedItem).build();
    }

    @Transactional
    public boolean updateItem(ItemRequestDto.UpdateItemDto updateItemDto) {
        // 가게 정보 조회
        Store store = storeRepository.findById(updateItemDto.getStoreId())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당하는 가게를 찾을 수 없습니다.", 1));

        if (!store.getUserId().equals(updateItemDto.getUserId())) {
            throw new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
        }


        // Item 조회
        Item item = itemRepository.findById(updateItemDto.getItemId())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당하는 메뉴를 찾을 수 없습니다.", 1));

        // Item 수정
        item.updateItemInfo(updateItemDto);

        return true;
    }

    @Transactional
    public boolean deleteItem(ItemRequestDto.DeleteItemDto deleteItemDto) {
        // 가게 정보 조회
        storeRepository.findById(deleteItemDto.getStoreId())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당하는 가게를 찾을 수 없습니다.", 1));

        // Item 삭제
        itemRepository.deleteById(deleteItemDto.getItemId());

        return true;
    }
}
