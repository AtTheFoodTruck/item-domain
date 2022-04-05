package com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository;

import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit() {
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
    }
}
