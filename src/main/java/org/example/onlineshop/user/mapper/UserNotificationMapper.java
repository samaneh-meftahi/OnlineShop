package org.example.onlineshop.user.mapper;

import org.example.onlineshop.user.dto.UserNotificationResponseDTO;
import org.example.onlineshop.user.model.UserNotification;

public class UserNotificationMapper {

    public static UserNotificationResponseDTO toDto(UserNotification userNotification) {
        UserNotificationResponseDTO dto = new UserNotificationResponseDTO();
        dto.setId(userNotification.getId());
        dto.setNotificationId(userNotification.getNotification().getId());
        dto.setUserId(userNotification.getUser().getId());
        dto.setRead(userNotification.isRead());
        return dto;
    }
}
