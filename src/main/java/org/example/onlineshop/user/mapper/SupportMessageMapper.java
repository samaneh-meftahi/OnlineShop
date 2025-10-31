package org.example.onlineshop.user.mapper;

import org.example.onlineshop.user.dto.SupportMessageRequestDTO;
import org.example.onlineshop.user.dto.SupportMessageResponseDTO;
import org.example.onlineshop.user.model.SupportMessage;
import org.example.onlineshop.user.model.SupportTicket;
import org.example.onlineshop.user.model.User;

public class SupportMessageMapper {
    public static SupportMessageResponseDTO toDto(SupportMessage supportMessage){
        SupportMessageResponseDTO dto = new SupportMessageResponseDTO();
        dto.setId(supportMessage.getId());
        dto.setMessage(supportMessage.getMessage());
        dto.setSenderId(supportMessage.getSender().getId());
        dto.setSupportTicketId(supportMessage.getSupportTicket().getId());
        dto.setCreatedAt(supportMessage.getCreatedAt());
        return dto;
    }

    public static SupportMessage toEntity(SupportMessageRequestDTO dto){
        SupportMessage supportMessage = new SupportMessage();
        supportMessage.setMessage(dto.getMessage());

        User user = new User();
        user.setId(dto.getSenderId());
        supportMessage.setSender(user);

        SupportTicket supportTicket = new SupportTicket();
        supportTicket.setId(dto.getSupportTicketId());
        supportMessage.setSupportTicket(supportTicket);

        return supportMessage;
    }
}
