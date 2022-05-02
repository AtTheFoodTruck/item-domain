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
            Category st = Category.createCategory("고기");
            Category dg = Category.createCategory("닭강정");
            Category hd = Category.createCategory("핫도그");
            Category ic = Category.createCategory("아이스크림");
            Category chi = Category.createCategory("중식");
            Category caf = Category.createCategory("디저트");
            Category susi = Category.createCategory("일식");
            Category etc = Category.createCategory("기타");
            Category kor = Category.createCategory("한식");
            Category fo = Category.createCategory("분식");

            em.persist(ch);
            em.persist(pi);
            em.persist(ha);
            em.persist(st);
            em.persist(dg);
            em.persist(hd);
            em.persist(ic);
            em.persist(chi);
            em.persist(caf);
            em.persist(susi);
            em.persist(etc);
            em.persist(kor);
            em.persist(fo);

            em.flush();
            em.clear();
        }

    }
}
