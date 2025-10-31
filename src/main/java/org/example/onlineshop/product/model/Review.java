package org.example.onlineshop.product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.onlineshop.product.model.Product;
import org.example.onlineshop.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Setter
    @Getter
    private int rating;

    @Setter
    @Getter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_approved")
    private boolean isApproved;

    @Setter
    @Getter
    private String comment;

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
