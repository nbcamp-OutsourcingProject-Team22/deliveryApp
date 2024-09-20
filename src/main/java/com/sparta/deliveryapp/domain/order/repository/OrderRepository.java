package com.sparta.deliveryapp.domain.order.repository;

import com.sparta.deliveryapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
