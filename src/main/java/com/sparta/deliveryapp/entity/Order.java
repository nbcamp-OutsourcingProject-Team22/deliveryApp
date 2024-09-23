package com.sparta.deliveryapp.entity;

import com.sparta.deliveryapp.domain.common.Timestamped;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.*;



@Getter
@Entity
@NoArgsConstructor
@Table(name = "`order`")
public class Order extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
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
