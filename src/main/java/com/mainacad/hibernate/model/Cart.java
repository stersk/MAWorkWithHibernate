package com.mainacad.hibernate.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    private Integer id;
    private Long creationTime;
    private Boolean closed;
    private Integer userId;

    public Cart(Long creationTime, Boolean closed, Integer userId) {
        this.creationTime = creationTime;
        this.closed = closed;
        this.userId = userId;
    }
}
