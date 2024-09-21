package com.sparta.deliveryapp.entity;

import com.sparta.deliveryapp.domain.common.Timestamped;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "`order`")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`order`")
public class Order extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatusEnum status;


    public Order(Member member, Store store, Menu menu, OrderStatusEnum status){
        this.member = member;
        this.store = store;
        this.menu = menu;
        this.status = status;
    }

    public void changeStatus(OrderStatusEnum status){
        this.status = status;
    }

}
