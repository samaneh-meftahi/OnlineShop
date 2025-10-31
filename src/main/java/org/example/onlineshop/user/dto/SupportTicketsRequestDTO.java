package org.example.onlineshop.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class SupportTicketsRequestDTO {
    private Long userId;
    private String subject;
    private String message;
    private String status;
    private LocalDateTime createdAt;

}
