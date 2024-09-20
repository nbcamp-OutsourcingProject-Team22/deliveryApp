package com.sparta.deliveryapp.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.deliveryapp.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu,Long> {
}
