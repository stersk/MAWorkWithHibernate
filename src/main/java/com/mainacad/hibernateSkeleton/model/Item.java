package com.mainacad.hibernateSkeleton.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "items")
public class Item {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_code")
    private String itemCode;

    @Column(name = "item_name")
    private String name;

    @Column(name = "price")
    private Integer price;

    public Item(String itemCode, String name, Integer price) {
        this.itemCode = itemCode;
        this.name = name;
        this.price = price;
    }
}
