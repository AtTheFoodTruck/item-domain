package com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Item;
import com.sesac.foodtruckitem.ui.dto.response.StoreResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.QItem.item;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public SliceImpl<StoreResponseDto.SearchItemDto> findItemList(Long storeId, Pageable pageable) {
        List<StoreResponseDto.SearchItemDto> content = queryFactory.select(
                        Projections.constructor(StoreResponseDto.SearchItemDto.class,
                                item.id,
                                item.name,
                                item.itemImg.imgUrl,
                                item.price
                        )
                )
                .from(item)
                .where(item.store.id.eq(storeId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    public Page<Item> findOwnerItemList(Long storeId, Pageable pageable) {
        Long count = queryFactory.select(item.countDistinct())
                .from(item)
                .where(item.store.id.eq(storeId))
                .fetchOne();

        List<Item> content = queryFactory.selectFrom(item)
                .where(item.store.id.eq(storeId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> count);
    }

}
