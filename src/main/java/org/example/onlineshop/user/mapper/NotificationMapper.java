package org.example.onlineshop.user.mapper;

import org.example.onlineshop.user.dto.NotificationRequestDTO;
import org.example.onlineshop.user.dto.NotificationResponseDTO;
import org.example.onlineshop.user.model.Notification;
import org.example.onlineshop.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationMapper {

    public static Notification toEntity(NotificationRequestDTO dto, List<User> users) {
        Notification notification = new Notification();
        notification.setMessage(dto.getMessage());
        notification.setType(dto.getType());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUsers(users);
        return notification;
    }

    public static NotificationResponseDTO toDto(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setUserIds(
                notification.getUsers().stream()
                        .map(User::getId)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
