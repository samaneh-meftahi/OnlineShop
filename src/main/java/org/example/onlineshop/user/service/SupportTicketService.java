package org.example.onlineshop.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.onlineshop.user.dto.SupportTicketsRequestDTO;
import org.example.onlineshop.user.dto.SupportTicketsResponseDTO;
import org.example.onlineshop.common.exception.InvalidRequestException;
import org.example.onlineshop.common.exception.ResourceNotFoundException;
import org.example.onlineshop.user.mapper.SupportTicketMapper;
import org.example.onlineshop.user.model.SupportTicket;
import org.example.onlineshop.user.model.User;
import org.example.onlineshop.user.repository.SupportTicketRepository;
import org.example.onlineshop.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;

    public SupportTicketsResponseDTO createTicket(SupportTicketsRequestDTO dto) {
        log.info("Creating support ticket for userId={}", dto.getUserId());

        validateTicket(dto);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> {
                    log.error("User not found: id={}", dto.getUserId());
                    return new ResourceNotFoundException("User with id: " + dto.getUserId() + " not found");
                });

        SupportTicket ticket = new SupportTicket();
        ticket.setSubject(dto.getSubject());
        ticket.setMessage(dto.getMessage());
        ticket.setStatus("OPEN");
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUser(user);

        supportTicketRepository.save(ticket);
        log.info("Support ticket created: id={}, userId={}", ticket.getId(), user.getId());

        return SupportTicketMapper.toDto(ticket);
    }

    public Optional<SupportTicket> findById(Long id) {
        log.debug("Fetching support ticket with id={}", id);
        return supportTicketRepository.findById(id);
    }

    public List<SupportTicketsResponseDTO> findAll() {
        log.debug("Fetching all support tickets");
        return supportTicketRepository.findAll()
                .stream()
                .map(SupportTicketMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<SupportTicketsResponseDTO> findByUserId(Long userId) {
        log.debug("Fetching support tickets for userId={}", userId);
        return supportTicketRepository.findByUser_Id(userId)
                .stream()
                .map(SupportTicketMapper::toDto)
                .collect(Collectors.toList());
    }

    public SupportTicketsResponseDTO updateTicket(Long id, SupportTicketsRequestDTO dto) {
        log.info("Updating support ticket: id={}", id);

        validateTicket(dto);

        SupportTicket ticket = supportTicketRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Support ticket not found: id={}", id);
                    return new ResourceNotFoundException("Support ticket with id " + id + " not found");
                });

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> {
                    log.error("User not found: id={}", dto.getUserId());
                    return new ResourceNotFoundException("User with id: " + dto.getUserId() + " not found");
                });

        ticket.setSubject(dto.getSubject());
        ticket.setMessage(dto.getMessage());
        ticket.setStatus(dto.getStatus());
        ticket.setUser(user);

        supportTicketRepository.save(ticket);
        log.info("Support ticket updated: id={}", ticket.getId());

        return SupportTicketMapper.toDto(ticket);
    }

    public void deleteTicket(Long id) {
        log.warn("Deleting support ticket: id={}", id);

        SupportTicket ticket = supportTicketRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Support ticket not found: id={}", id);
                    return new ResourceNotFoundException("Support ticket with id " + id + " not found");
                });

        supportTicketRepository.delete(ticket);
        log.info("Support ticket deleted: id={}", id);
    }

    private void validateTicket(SupportTicketsRequestDTO ticket) {
        if (ticket.getSubject() == null || ticket.getSubject().isBlank()) {
            log.warn("Ticket subject is invalid: {}", ticket.getSubject());
            throw new InvalidRequestException("Ticket subject must not be empty");
        }
        if (ticket.getMessage() == null || ticket.getMessage().isBlank()) {
            log.warn("Ticket message is invalid");
            throw new InvalidRequestException("Ticket message must not be empty");
        }
        if (ticket.getUserId() == null) {
            log.warn("Ticket userId is null");
            throw new InvalidRequestException("Ticket must be associated with a user");
        }
        if (ticket.getStatus() == null || ticket.getStatus().isBlank()) {
            log.warn("Ticket status is invalid: {}", ticket.getStatus());
            throw new InvalidRequestException("Ticket status must not be empty");
        }
    }
}
