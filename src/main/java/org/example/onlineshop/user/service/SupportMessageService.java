package org.example.onlineshop.user.service;

import lombok.extern.slf4j.Slf4j;
import org.example.onlineshop.user.dto.SupportMessageRequestDTO;
import org.example.onlineshop.user.dto.SupportMessageResponseDTO;
import org.example.onlineshop.common.exception.InvalidRequestException;
import org.example.onlineshop.common.exception.ResourceNotFoundException;
import org.example.onlineshop.user.mapper.SupportMessageMapper;
import org.example.onlineshop.user.model.SupportMessage;
import org.example.onlineshop.user.model.SupportTicket;
import org.example.onlineshop.user.model.User;
import org.example.onlineshop.user.repository.SupportMessageRepository;
import org.example.onlineshop.user.repository.SupportTicketRepository;
import org.example.onlineshop.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SupportMessageService {

    private final SupportMessageRepository supportMessageRepository;
    private final UserRepository userRepository;
    private final SupportTicketRepository supportTicketRepository;

    public SupportMessageService(SupportMessageRepository supportMessageRepository, UserRepository userRepository, SupportTicketRepository supportTicketRepository) {
        this.supportMessageRepository = supportMessageRepository;
        this.userRepository = userRepository;
        this.supportTicketRepository = supportTicketRepository;
    }

    public SupportMessageResponseDTO addMessage(SupportMessageRequestDTO message) {
        validateMessage(message);

        User sender = userRepository.findById(message.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender with id " + message.getSenderId() + " not found"));

        SupportTicket ticket = supportTicketRepository.findById(message.getSupportTicketId())
                .orElseThrow(() -> new ResourceNotFoundException("Support ticket with id " + message.getSupportTicketId() + " not found"));

        SupportMessage supportMessage = new SupportMessage();
        supportMessage.setMessage(message.getMessage());
        supportMessage.setSender(sender);
        supportMessage.setSupportTicket(ticket);

        SupportMessage saved = supportMessageRepository.save(supportMessage);
        log.info("Support message created with id: {}", saved.getId());
        return SupportMessageMapper.toDto(saved);
    }

    public Optional<SupportMessage> findById(Long id) {
        return supportMessageRepository.findById(id);
    }

    public List<SupportMessageResponseDTO> findMessagesByTicketId(Long ticketId) {
        List<SupportMessage> messages = supportMessageRepository.findBySupportTicket_Id(ticketId);
        return messages.stream().map(SupportMessageMapper::toDto).collect(Collectors.toList());
    }

    public SupportMessageResponseDTO updateMessage(Long id, SupportMessageRequestDTO updatedMessage) {
        validateMessage(updatedMessage);

        SupportMessage supportMessage = supportMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Support message with id " + id + " not found"));

        User sender = userRepository.findById(updatedMessage.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender with id " + updatedMessage.getSenderId() + " not found"));

        supportMessage.setMessage(updatedMessage.getMessage());
        supportMessage.setSender(sender);

        supportMessageRepository.save(supportMessage);
        log.info("Support message updated with id: {}", id);

        return SupportMessageMapper.toDto(supportMessage);
    }

    public void deleteMessage(Long id) {
        SupportMessage message = supportMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Support message with id " + id + " not found"));
        supportMessageRepository.delete(message);
        log.info("Support message deleted with id: {}", id);
    }

    private void validateMessage(SupportMessageRequestDTO message) {
        if (message.getMessage() == null || message.getMessage().isBlank()) {
            throw new InvalidRequestException("Message content must not be empty");
        }
        if (message.getSenderId() == null) {
            throw new InvalidRequestException("Message sender must be specified");
        }
        if (message.getSupportTicketId() == null) {
            throw new InvalidRequestException("Message must be associated with a support ticket");
        }
    }
}
