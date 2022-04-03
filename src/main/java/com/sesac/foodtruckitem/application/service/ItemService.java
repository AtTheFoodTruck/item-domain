package com.sesac.foodtruckitem.application.service;


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

    public List<ItemResponseDto.GetItemsDto> getItems(ItemRequestDto.GetItemsDto itemsDto) {
        Store store = storeRepository.findById(itemsDto.getStore_id()).orElseThrow();  // "해당하는 가게를 찾을 수 없습니다."

        List<Item> items = itemRepository.findAllByStoreOrderByCreatedDate(store);

        List<ItemResponseDto.GetItemsDto> responseItems = new ArrayList<>();
        for (Item item : items) {
            responseItems.add(ItemResponseDto.GetItemsDto.builder()
                    .item(item)
                    .build());
        }

        return responseItems;
    }
}
