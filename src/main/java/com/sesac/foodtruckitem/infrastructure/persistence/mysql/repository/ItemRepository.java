package com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository;

import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Item;
import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByStoreOrderByCreatedDate(Store store);

    @Query("select i from Item i where i.store.id = :storeId")
    Slice<Item> findItemByStoreId(@Param("storeId") Long storeId, Pageable pageable);

}
