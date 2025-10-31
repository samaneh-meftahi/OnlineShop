package org.example.onlineshop.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SupportMessageResponseDTO {
    private Long id;
    private Long supportTicketId;
    private Long senderId;
    private String message;
    private LocalDateTime createdAt;

}
