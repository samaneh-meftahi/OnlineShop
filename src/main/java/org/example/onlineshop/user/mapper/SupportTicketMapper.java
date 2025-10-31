package org.example.onlineshop.user.mapper;

import org.example.onlineshop.user.dto.SupportTicketsRequestDTO;
import org.example.onlineshop.user.dto.SupportTicketsResponseDTO;
import org.example.onlineshop.user.model.SupportTicket;
import org.example.onlineshop.user.model.User;

public class SupportTicketMapper {
    public static SupportTicketsResponseDTO toDto(SupportTicket supportTicket){
        SupportTicketsResponseDTO dto = new SupportTicketsResponseDTO();
        dto.setId(supportTicket.getId());
        dto.setMessage(supportTicket.getMessage());
        dto.setUserId(supportTicket.getUser().getId());
        dto.setStatus(supportTicket.getStatus());
        dto.setSubject(supportTicket.getSubject());
        dto.setCreatedAt(supportTicket.getCreatedAt());
        return dto;
    }

    public static SupportTicket toEntity(SupportTicketsRequestDTO dto){
        SupportTicket supportTicket = new SupportTicket();
        supportTicket.setMessage(dto.getMessage());
        supportTicket.setStatus(dto.getStatus());
        supportTicket.setSubject(dto.getSubject());
        supportTicket.setCreatedAt(dto.getCreatedAt());

        User user = new User();
        user.setId(dto.getUserId());
        supportTicket.setUser(user);

        return supportTicket;
    }
}

