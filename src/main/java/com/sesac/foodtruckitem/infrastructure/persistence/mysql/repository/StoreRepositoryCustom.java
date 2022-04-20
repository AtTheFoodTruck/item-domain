package com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sesac.foodtruckitem.ui.dto.SearchStoreResultDto;
import com.sesac.foodtruckitem.ui.dto.request.SearchStoreCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
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
    public SliceImpl<SearchStoreResultDto> findSearchStorePage(SearchStoreCondition condition, Pageable pageable) {
        // 사용자 위도 경도
        NumberExpression<Double> haversineDistance = getHaversinDistance(condition.getLatitude(), condition.getLongitude());
        NumberPath<Double> distanceAlias = Expressions.numberPath(Double.class, "distance");

        List<SearchStoreResultDto> content = queryFactory.select(
                        Projections.constructor(SearchStoreResultDto.class,
                                store.id,
                                store.storeImage.storeImgUrl,
                                store.name,
                                haversineDistance.as(distanceAlias))
                )
                .from(store)
                .join(store.map)
//                .where(
////                        storeNameContains(condition.getName()).or(item.store.name.contains(condition.getName()))
//                        storeNameContains(condition.getName())
//                )
                .orderBy(distanceAlias.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
//                .distinct()
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression storeNameContains(String storeName) {
        return storeName == null ? null : store.name.contains(storeName);
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

}