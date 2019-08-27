package com.mainacad.hibernate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Integer id;
    private Integer itemId;
    private Integer amount;
    private Integer cartId;

    public Order(Integer itemId, Integer amount, Integer cartId) {
        this.itemId = itemId;
        this.amount = amount;
        this.cartId = cartId;
    }
}
