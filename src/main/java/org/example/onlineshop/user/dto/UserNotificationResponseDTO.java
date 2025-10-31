package org.example.onlineshop.user.dto;

import lombok.Data;

@Data
public class UserNotificationResponseDTO {
    private Long id;
    private Long notificationId;
    private Long userId;
    private boolean isRead;
}
