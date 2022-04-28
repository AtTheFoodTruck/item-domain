//package com.sesac.foodtruckitem.application.service;
//
//import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Images;
//import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Item;
//import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
//import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.ItemRepository;
//import com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository.StoreRepository;
//import com.sesac.foodtruckitem.ui.dto.request.ItemRequestDto;
//import com.sesac.foodtruckitem.ui.dto.response.ItemResponseDto;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class ItemServiceTest {
//
//    @Autowired
//    private ItemService itemService;
//    @Autowired
//    private ItemRepository itemRepository;
//    @Autowired
//    private StoreRepository storeRepository;
//
//    @BeforeEach
//    public void saveStoreAndItem() {
//        // 가게, 메뉴 생성
//        Store store = getStore();
//        Item item = getItem();
//
//        // 가게에 메뉴 추가
//        store.addItem(item);
//
//        // 가게, 메뉴 저장
//        storeRepository.save(store);
//        itemRepository.save(item);
//    }
//
////    @Transactional
////    @Test
////    void createItem() {
////        /** given */
////        Store savedStore = storeRepository.findAll().get(0);
////        Item savedItem = itemRepository.findAll().get(0);
////
////        // Dto 생성
////        ItemRequestDto.CreateItemDto createItemDto = ItemRequestDto.CreateItemDto.builder()
////                .storeId(savedStore.getId())
////                .itemName(savedItem.getName())
////                .description(savedItem.getDescription())
////                .price(savedItem.getPrice())
////                .itemImgUrl(savedItem.getItemImg().getImgUrl())
////                .build();
////
////        /** when */
////        // Service
////        ItemResponseDto.CreateItemDto responseDto = itemService.createItem(createItemDto);
////
////        // 메뉴 조회
////        Item getItem = itemRepository.findAll().get(0);
////
////        /** then */
////        assertEquals(savedStore, getItem.getStore());
////        assertEquals(getItem, savedStore.getItems().get(0));
////    }
//
//
//
////    @Transactional
////    @Test
////    void updateItem() {
////        /** given */
////        Store savedStore = storeRepository.findAll().get(0);
////        Item savedItem = itemRepository.findAll().get(0);
////
////        // Dto 생성
////        ItemRequestDto.UpdateItemDto updateItemDto = ItemRequestDto.UpdateItemDto.builder()
////                .storeId(savedStore.getId())
////                .itemId(savedItem.getId())
////                .itemName("재은이네 떡볶이")  // 메뉴명 수정
////                .description(savedItem.getDescription())
////                .price(savedItem.getPrice())
////                .itemImgUrl(savedItem.getItemImg().getImgUrl())
////                .build();
////
////        /** when */
////        // Service
////        assertTrue(itemService.updateItem(updateItemDto));
////
////        // 메뉴 조회
////        Item updatedItem = itemRepository.findAll().get(0);
////
////
////        /** then */
////        Assertions.assertThat("재은이네 떡볶이").isEqualTo(updatedItem.getName());
//////        assertEquals("재은이네 떡볶이", updatedItem.getName());
////    }
//
//    @Transactional
//    @Test
//    void deleteItem() {
//        // 가게, 메뉴 조회
//        Store savedStore = storeRepository.findAll().get(0);
//        Item savedItem = itemRepository.findAll().get(0);
//
//        // 메뉴 삭제
//        ItemRequestDto.DeleteItemDto deleteItemDto = ItemRequestDto.DeleteItemDto.builder()
//                .storeId(savedStore.getId())
//                .itemId(savedItem.getId()).build();
//
//        // 삭제 성공 시, return true
//        assertTrue(itemService.deleteItem(deleteItemDto));
//    }
//
//    @Transactional
//    @Test
//        // (expected = EntityNotFoundException.class)
//    void deleteItemFail() {
//        // 가게, 메뉴 조회
//        Store savedStore = storeRepository.findAll().get(0);
//        Item savedItem = itemRepository.findAll().get(0);
//
//        // 메뉴 삭제
//        ItemRequestDto.DeleteItemDto deleteItemDto = ItemRequestDto.DeleteItemDto.builder()
//                .storeId(savedStore.getId() + 100L)
//                .itemId(savedItem.getId()).build();
//
//
//        assertThrows(EmptyResultDataAccessException.class, () -> {
//            itemService.deleteItem(deleteItemDto);
//        });
//    }
//
//    private Store getStore() {
//        return Store.builder()
////                .id(1L)
//                .name("효영이네 떡볶이")
//                .phoneNum("01046633422")
//                .isOpen(false)
//                .notice("테스트 가게입니다.")
//                .totalWaitingCount(0)
//                .avgRate(0.0)
//                .build();
//
////        ("1348639909").sDt("20070523").pNm("이한종")
//    }
//
//    private Item getItem() {
//        return Item.builder()
////                .id(1L)
//                .name("로제 떡볶이")
//                .description("매콤한 로제 떡볶이")
//                .price(13000)
//                .itemImg(new Images("로제 떡볶이", "https://recipe1.ezmember.co.kr/cache/recipe/2021/01/03/ff04026cccdfaf4a6781f3f6a3e76fa21.jpg"))
//                .build();
//    }
//}