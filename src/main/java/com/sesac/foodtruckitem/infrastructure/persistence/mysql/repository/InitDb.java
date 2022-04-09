package com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository;

import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Category;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Images;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Item;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;
    private final StoreRepository storeRepository;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final StoreRepository storeRepository;

        public void dbInit1() {
            Category ch = Category.createCategory("치킨");
            Category pi = Category.createCategory("피자");
            Category ha = Category.createCategory("햄버거");
            Category st = Category.createCategory("스테이크");
            Category dg = Category.createCategory("닭강정");
            Category hd = Category.createCategory("핫도그");
            Category ic = Category.createCategory("아이스크림");

            em.persist(ch);
            em.persist(pi);
            em.persist(ha);
            em.persist(st);
            em.persist(dg);
            em.persist(hd);
            em.persist(ic);
        }

        public void dbInit2() {
            Images 후라이드_치킨_사진이름 = Images.of("후라이드 치킨 사진이름", "후라이드 치킨 이미지 링크는~~~");
            Images 양념_치킨_사진이름 = Images.of("양념 치킨 사진이름", "양념 치킨 이미지 링크는~~~");
            Images 슈프림_치킨_사진이름 = Images.of("슈프림 치킨 사진이름", "슈프림 치킨 이미지 링크는~~~");

            Store store = Store.builder().id(1L).build();
            Store savedStore = storeRepository.save(store);

            Item 후라이드 = Item.builder()
                    .name("후라이드 치킨")
                    .description("후라이드 치킨 설명 블라블라")
                    .price(20000)
                    .itemImg(후라이드_치킨_사진이름)
                    .store(savedStore)
                    .build();
            Item 양념 = Item.builder()
                    .name("양념 치킨")
                    .description("양념 치킨 설명 블라블라")
                    .price(20000)
                    .itemImg(양념_치킨_사진이름)
                    .store(savedStore)
                    .build();
            Item 슈프림 = Item.builder()
                    .name("슈프림 치킨")
                    .description("슈프림 치킨 설명 블라블라")
                    .price(20000)
                    .itemImg(슈프림_치킨_사진이름)
                    .store(savedStore)
                    .build();

            em.persist(후라이드);
            em.persist(양념);
            em.persist(슈프림);
        }
    }
}
