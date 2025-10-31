package org.example.onlineshop.user.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "users")
public class User {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String phone;

    @Setter
    @Getter
    private String password;

    @Setter
    @Getter
    private String address;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {

    }

    @Column(name = "is_admin")
    private boolean isAdmin;

    @Column(name = "is_active")
    private boolean isActive;

    @Getter
    @Column(name = "two_factor")
    private boolean twoFactor;

    @Setter
    @Getter
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private Long cartId;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
