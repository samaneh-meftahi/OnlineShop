package org.example.onlineshop.repository;

import org.example.onlineshop.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponsRepository extends JpaRepository<Coupon,Long> {
}
