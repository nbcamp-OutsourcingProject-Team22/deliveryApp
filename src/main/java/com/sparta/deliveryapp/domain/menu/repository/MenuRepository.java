package com.sparta.deliveryapp.domain.menu.repository;

import com.sparta.deliveryapp.entity.Menu;
import com.sparta.deliveryapp.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Menu findByStore(Store store); // 메뉴 entity에 있는 스토어아이디로 메뉴를 가져오기 위해서
}
