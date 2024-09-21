package com.sparta.deliveryapp.entity;

import com.sparta.deliveryapp.domain.menu.dto.MenuRequest;
import com.sparta.deliveryapp.domain.store.model.StoreRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
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

    public static Menu menuWithStore (MenuRequest menuRequest, Store store) {
        return new Menu(
                null,
                menuRequest.getMenuName(),
                menuRequest.getMenuPrice(),
                menuRequest.getMenuDescription(),
                store
        );
    }
}