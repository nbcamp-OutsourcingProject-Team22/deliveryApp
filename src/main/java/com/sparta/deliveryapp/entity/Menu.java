package com.sparta.deliveryapp.entity;

import com.sparta.deliveryapp.domain.menu.dto.MenuRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer menuPrice;

    @Column(nullable = false)
    private String menuDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    private boolean isDeleted = false; // 삭제 상태를 나타내는 필드 추가

    public static Menu menuWithStore (MenuRequest menuRequest, Store store) {
        return new Menu(
                null,
                menuRequest.getMenuName(),
                menuRequest.getMenuPrice(),
                menuRequest.getMenuDescription(),
                store,
                false
        );
    }

    public void update(String menuName, Integer menuPrice, String menuDescription) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuDescription = menuDescription;
    }
}