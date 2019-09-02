package com.mainacad.hibernateSkeleton.model;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "creation_time")
    private Long creationTime;

    @Column(name = "closed")
    private Boolean closed;

    @ManyToOne(targetEntity = User.class)
    private User user;

    public Cart(Long creationTime, Boolean closed, User user) {
        this.creationTime = creationTime;
        this.closed = closed;
        this.user = user;
    }
}
