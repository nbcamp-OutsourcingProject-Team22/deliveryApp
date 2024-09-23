package com.sparta.deliveryapp.domain.menu.repository;

import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByStore(Store store);
    Optional<Menu> findByStore(Store store);
}
