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

    /**
     * 해당 가게의 메뉴 리스트 조회
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/04/04
    **/
    public Page<ItemResponseDto.GetItemsDto> getOwnerItemsInfo(ItemRequestDto.GetItemsDto getItemsDto, Pageable pageable) {
        // 가게 정보 조회
        Store store = storeRepository.findById(getItemsDto.getStoreId())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당하는 가게를 찾을 수 없습니다.", 1));

        // 해당 가게 메뉴 리스트 조회
//        List<Item> items = itemRepository.findAllByStoreOrderByCreatedDate(store);
        Page<Item> items = itemRepositoryCustom.findOwnerItemList(store.getId(), pageable);

        // Dto 변환
        List<ItemResponseDto.GetItemsDto> responseItemsDto = new ArrayList<>();
        for (Item item : items.getContent()) {
            responseItemsDto.add(ItemResponseDto.GetItemsDto.builder()
                    .item(item)
                    .build());
        }

        return PageableExecutionUtils.getPage(responseItemsDto, pageable, () -> items.getTotalPages());
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
            throw new StoresException("유저 정보가 일치하지 않습니다.");
        }

        // Item 생성
        Item item = Item.builder()
                .name(createItemDto.getItemName().replaceAll(" ", ""))
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

        // Item 조회
        Item item = itemRepository.findById(deleteItemDto.getItemId())
                .orElseThrow(() -> new EmptyResultDataAccessException("해당하는 메뉴를 찾을 수 없습니다.", 1));

        // Item 삭제
        itemRepository.deleteById(deleteItemDto.getItemId());

        return true;
    }

    /**
     * Item 정보 조회 - Feign clien 통신
     *
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-04-09
     **/
    public List<ItemResponseDto.GetItemsInfoDto> getItems(List<Long> itemIds) {
        List<Item> items = itemRepository.findAllById(itemIds);

        return items.stream()
                .map(item -> ItemResponseDto.GetItemsInfoDto.of(item))
                .collect(Collectors.toList());
    }
}
