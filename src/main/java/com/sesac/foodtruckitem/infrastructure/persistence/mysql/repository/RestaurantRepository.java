package com.sesac.foodtruckitem.infrastructure.persistence.mysql.repository;

import com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
