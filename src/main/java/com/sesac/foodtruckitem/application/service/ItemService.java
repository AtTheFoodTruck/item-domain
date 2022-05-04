package com.sesac.foodtruckitem.application.service;


import com.sesac.foodtruckitem.exception.StoresException;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Images;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Item;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.ItemRepository;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.ItemRepositoryCustom;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.StoreRepository;
import com.sesac.foodtruckitem.ui.dto.request.ItemRequestDto;
import com.sesac.foodtruckitem.ui.dto.response.ItemResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemRepositoryCustom itemRepositoryCustom;
    private final StoreRepository storeRepository;

    public Page<ItemResponseDto.GetItemsDto> getOwnerItemsInfo(Long userId, Pageable pageable) {
        Store store = storeRepository.findByUserId(userId)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당하는 가게를 찾을 수 없습니다.", 1));

        Page<Item> items = itemRepositoryCustom.findOwnerItemList(store.getId(), pageable);

        List<ItemResponseDto.GetItemsDto> responseItemsDto = new ArrayList<>();
        for (Item item : items.getContent()) {
            responseItemsDto.add(ItemResponseDto.GetItemsDto.builder()
                    .item(item)
                    .build());
        }

        return PageableExecutionUtils.getPage(responseItemsDto, pageable, () -> items.getTotalElements());
    }

    @Transactional
    public ItemResponseDto.CreateItemDto createItem(ItemRequestDto.CreateItemDto createItemDto) {

        Store store = storeRepository.findByUserId(createItemDto.getUserId())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당하는 가게를 찾을 수 없습니다.", 1));

        if (!store.getUserId().equals(createItemDto.getUserId())) {
            throw new StoresException("유저 정보가 일치하지 않습니다.");
        }

        Item item = Item.builder()
                .name(createItemDto.getItemName().replaceAll(" ", ""))
                .description(createItemDto.getDescription())
                .price(createItemDto.getPrice())
                .itemImg(new Images(createItemDto.getItemName(), createItemDto.getItemImgUrl()))
                .build();

        Item savedItem = itemRepository.save(item);

        store.addItem(item);
        storeRepository.save(store);

        return ItemResponseDto.CreateItemDto.builder().item(savedItem).build();
    }

    @Transactional
    public boolean updateItem(ItemRequestDto.UpdateItemDto updateItemDto) {
        Store store = storeRepository.findByUserId(updateItemDto.getUserId())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당하는 가게를 찾을 수 없습니다.", 1));

        if (!store.getUserId().equals(updateItemDto.getUserId())) {
            throw new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
        }


        Item item = itemRepository.findById(updateItemDto.getItemId())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당하는 메뉴를 찾을 수 없습니다.", 1));

        item.updateItemInfo(updateItemDto);

        return true;
    }

    @Transactional
    public boolean deleteItem(ItemRequestDto.DeleteItemDto deleteItemDto) {
        itemRepository.findById(deleteItemDto.getItemId())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당하는 메뉴를 찾을 수 없습니다.", 1));

        itemRepository.deleteById(deleteItemDto.getItemId());

        return true;
    }

    public List<ItemResponseDto.GetItemsInfoDto> getItems(List<Long> itemIds) {
        List<Item> items = itemRepository.findAllById(itemIds);

        return items.stream()
                .map(item -> ItemResponseDto.GetItemsInfoDto.of(item))
                .collect(Collectors.toList());
    }
}
