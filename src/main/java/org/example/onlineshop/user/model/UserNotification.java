package org.example.onlineshop.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.onlineshop.user.model.Notification;
import org.example.onlineshop.user.model.User;

@Entity
@Table(name = "notification_user")
@Getter
@Setter
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_read")
    private boolean isRead;
}

