package com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sesac.foodtruckitem.ui.dto.response.StoreResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.QItem.item;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * 가게 조회 페이지에 보여질 메뉴 목록
     * @author
     * @version 1.0.0
     * 작성일 2022/04/14
     **/
    public SliceImpl<StoreResponseDto.SearchItemDto> findItemList(Long storeId, Pageable pageable) {
        List<StoreResponseDto.SearchItemDto> content = queryFactory.select(
                        Projections.constructor(StoreResponseDto.SearchItemDto.class,
                                item.id,
                                item.name,
                                item.itemImg,
                                item.price
                        )
                ).from(item)
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

}
