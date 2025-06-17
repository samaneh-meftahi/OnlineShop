package org.example.onlineshop.service;

import org.example.onlineshop.model.Notification;
import org.example.onlineshop.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(Notification notification){
        validateNotification(notification);
        return notificationRepository.save(notification);
    }

    public Optional<Notification> findById(Long id){
        return notificationRepository.findById(id);
    }

    public void removeNotification(Long id){
        Notification notification=findById(id).orElseThrow(()->new NoSuchElementException(""));
        notificationRepository.delete(notification);
    }

    public Notification updateNotification(Long id,Notification updateNotification){
        Optional<Notification> exitingNotification= findById(id);
        if(exitingNotification.isPresent()){
            Notification notification=exitingNotification.get();
            notification.setMessage(updateNotification.getMessage());
            notification.setType(updateNotification.getType());
            notification.setUsers(updateNotification.getUsers());
            notification.setRead(updateNotification.isRead());
            return notificationRepository.save(notification);
        }else {
            throw new NoSuchElementException("");
        }
    }

    public void validateNotification(Notification notification){
        if(notification.getMessage().isEmpty()||notification.getMessage().isBlank()){
            throw new IllegalArgumentException("message notification not be empty");
        }
        if(notification.getType().isBlank()||notification.getType().isEmpty()){
            throw new IllegalArgumentException("type notification not be empty");
        }
        if(notification.getUsers().isEmpty()){
            throw new IllegalArgumentException("");
        }
    }
}
