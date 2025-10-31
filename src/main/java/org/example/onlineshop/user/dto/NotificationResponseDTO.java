package org.example.onlineshop.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class NotificationResponseDTO {
    private Long id;
    private String message;
    private String type;
    private LocalDateTime createdAt;
    private List<Long> userIds;
}
