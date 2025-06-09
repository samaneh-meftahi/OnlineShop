package org.example.onlineshop.model;

import jakarta.persistence.*;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id",nullable = false )
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false )
    private Product product;

    private int quantity;
}
