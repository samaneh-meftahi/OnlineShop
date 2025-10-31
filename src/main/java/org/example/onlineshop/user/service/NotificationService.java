package org.example.onlineshop.user.service;

import lombok.extern.slf4j.Slf4j;
import org.example.onlineshop.user.dto.NotificationRequestDTO;
import org.example.onlineshop.user.dto.NotificationResponseDTO;
import org.example.onlineshop.user.mapper.NotificationMapper;
import org.example.onlineshop.user.model.Notification;
import org.example.onlineshop.user.model.User;
import org.example.onlineshop.user.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public NotificationService(NotificationRepository notificationRepository, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    public NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO) {
        List<User> users = userService.findUsersByIds(requestDTO.getUserIds());
        Notification notification = NotificationMapper.toEntity(requestDTO, users);

        validateNotification(notification);
        Notification saved = notificationRepository.save(notification);

        log.info("Notification created with ID {}", saved.getId());
        return NotificationMapper.toDto(saved);
    }

    public NotificationResponseDTO updateNotification(Long id, NotificationRequestDTO updatedDTO) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notification with id " + id + " not found"));

        List<User> users = userService.findUsersByIds(updatedDTO.getUserIds());

        notification.setMessage(updatedDTO.getMessage());
        notification.setType(updatedDTO.getType());
        notification.setUsers(users);

        validateNotification(notification);

        Notification updated = notificationRepository.save(notification);
        log.info("Notification with ID {} updated", id);
        return NotificationMapper.toDto(updated);
    }

    public void removeNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notification with id " + id + " not found"));

        notificationRepository.delete(notification);
        log.info("Notification with ID {} removed", id);
    }

    public Notification findById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notification with id " + id + " not found"));
    }

    private void validateNotification(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification must not be null");
        }
        if (notification.getMessage() == null || notification.getMessage().isBlank()) {
            throw new IllegalArgumentException("Message is required");
        }
        if (notification.getType() == null || notification.getType().isBlank()) {
            throw new IllegalArgumentException("Type is required");
        }
        if (notification.getUsers() == null || notification.getUsers().isEmpty()) {
            throw new IllegalArgumentException("Notification must be assigned to at least one user");
        }
    }
}
