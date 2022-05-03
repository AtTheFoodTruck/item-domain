package com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import com.sesac.foodtruckitem.ui.dto.SearchStoreResultDto;
import com.sesac.foodtruckitem.ui.dto.request.SearchStoreCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.dsl.MathExpressions.*;
import static com.querydsl.core.types.dsl.MathExpressions.radians;
import static com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.QStore.store;
import static com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.QItem.item;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 사용자 위치 기반 푸드트럭 조회
     *
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022/04/14
     **/
    public Page<SearchStoreResultDto> findSearchStorePage(SearchStoreCondition condition, Pageable pageable) {
        // 사용자 위도 경도
        NumberExpression<Double> haversineDistance = getHaversinDistance(condition.getLatitude(), condition.getLongitude());
        NumberPath<Double> distanceAlias = Expressions.numberPath(Double.class, "distance");

        log.info("현재 두 지점 사이의 거리는 : ? " + haversineDistance);
        log.info("현재 두 지점 사이의 거리는 NumberPath: ? " + distanceAlias);

        // 카운트 쿼리
        Long count = queryFactory.select(store.countDistinct())
                .from(store)
                .join(store.map)
                .join(store.items, item)
                .where(
                        storeNameContains(condition.getName()).or(itemNameContains(condition.getName()))
                )
                .fetchOne();

        // 데이터 쿼리
        List<SearchStoreResultDto> content = queryFactory.select(
                        Projections.constructor(SearchStoreResultDto.class,
                                store.id,
                                store.storeImage.imgUrl,
                                store.name,
                                haversineDistance.as(distanceAlias),
                                store.address.address
                        )
                )
                .from(store)
                .join(store.map)
                .join(store.items, item)
                .where(
                        storeNameContains(condition.getName()).or(itemNameContains(condition.getName()))
                )
                .orderBy(distanceAlias.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

//        boolean hasNext = false;
//        if (content.size() > pageable.getPageSize()) {
//            content.remove(pageable.getPageSize());
//            hasNext = true;
//        }
//
//        return new SliceImpl<>(content, pageable, hasNext);

        return PageableExecutionUtils.getPage(content, pageable, () -> count);
    }

    private BooleanExpression storeNameContains(String storeName) {
        return storeName == null ? null : store.name.contains(storeName);
    }

    private BooleanExpression itemNameContains(String itemName) {
        return itemName == null ? null : item.name.contains(itemName);
    }

    private NumberExpression<Double> getHaversinDistance(double SearchLatitude, double SearchLongitude) {
        Expression<Double> latitude = Expressions.constant(SearchLatitude);
        Expression<Double> longitude = Expressions.constant(SearchLongitude);

        int earthRadius = 6371;

        NumberExpression<Double> haversineDistance = acos(
                cos(radians(latitude))
                        .multiply(cos(radians(store.map.latitude)))
                        .multiply(
                                cos(radians(store.map.longitude)
                                        .subtract(radians(longitude)))
                        )
                        .add(
                                sin(radians(latitude))
                                        .multiply(sin(radians(store.map.latitude)))
                        )
        )
                .multiply(Expressions.constant(earthRadius * 1000));
        return haversineDistance;
    }

    public Page<SearchStoreResultDto.MainStoreResultDto> findStoreMain(Pageable pageable) {

        // 카운트 쿼리
        Long count = queryFactory.select(store.countDistinct())
                .from(store)
                .limit(100)
                .fetchOne();

        // 데이터 쿼리
        List<SearchStoreResultDto.MainStoreResultDto> content = queryFactory.select(
                        Projections.constructor(SearchStoreResultDto.MainStoreResultDto.class,
                                store.id,
                                store.storeImage.imgUrl,
                                store.name,
                                store.category.name,
                                store.avgRate
                    )
                )
                .from(store)
                .orderBy(store.avgRate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> count);
    }
}
