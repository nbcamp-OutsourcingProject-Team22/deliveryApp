package com.sparta.deliveryapp.entity;

import com.sparta.deliveryapp.domain.common.Timestamped;
import com.sparta.deliveryapp.domain.order.OrderStatusEnum;
import com.sparta.deliveryapp.domain.order.validator.OrderValid;
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

    // 검증 부분
    // 주문자와 로그인 고객의 동일성 검증
    public void isOrderer(Member member){
        OrderValid.isOrderer(this.member,member);
    }

    // 주문 상태가 입력과 동일하면 예외
    public void isStatusSame(OrderStatusEnum orderStatusEnum){
        OrderValid.isStatusSame(this.status,orderStatusEnum);
    }

    // 주문 상태가 입력과 다르면 예외
    public void isStatusDifferent(OrderStatusEnum orderStatusEnum){
        OrderValid.isStatusDifferent(this.status,orderStatusEnum);
    }

}
