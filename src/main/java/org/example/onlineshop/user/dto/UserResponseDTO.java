package org.example.onlineshop.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private boolean isActive;
    private boolean twoFactor;
    private LocalDateTime createdAt;
}
