package org.example.onlineshop.repository;

import org.example.onlineshop.model.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportMessageRepository extends JpaRepository<SupportMessage,Long> {
}
