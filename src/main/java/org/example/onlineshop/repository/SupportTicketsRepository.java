package org.example.onlineshop.repository;

import org.example.onlineshop.model.SupportTickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportTicketsRepository extends JpaRepository<SupportTickets,Long> {
}
