package org.example.onlineshop.user.repository;

import org.example.onlineshop.user.model.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportMessageRepository extends JpaRepository<SupportMessage,Long> {
    List<SupportMessage> findBySupportTicket_Id(Long ticketId);
}
