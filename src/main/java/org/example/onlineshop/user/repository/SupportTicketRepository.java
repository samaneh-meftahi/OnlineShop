package org.example.onlineshop.user.repository;

import org.example.onlineshop.user.model.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket,Long> {
    List<SupportTicket> findByUser_Id(Long userId);
}
