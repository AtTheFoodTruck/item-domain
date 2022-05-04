package com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository;

import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Store;
import com.sesac.foodtruckitem.ui.dto.SearchStoreResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.el.Expression;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface StoreRepository extends JpaRepository<Store, Long> {
    int countByUserId(Long userId);

    Optional<Store> findByUserId(Long userId);

    int countByName(String storeName);

//    Page<SearchStoreResultDto> findSearchStorePage();


}

