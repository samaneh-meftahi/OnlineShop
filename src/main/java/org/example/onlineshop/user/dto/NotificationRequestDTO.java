package org.example.onlineshop.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotificationRequestDTO {
    private String message;
    private String type;
    private List<Long> userIds;
}
