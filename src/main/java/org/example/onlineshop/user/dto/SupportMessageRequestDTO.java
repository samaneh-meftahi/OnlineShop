package org.example.onlineshop.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportMessageRequestDTO {
    private Long supportTicketId;
    private Long senderId;
    private String message;
}
