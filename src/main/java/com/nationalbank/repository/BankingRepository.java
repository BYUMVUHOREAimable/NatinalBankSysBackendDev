package com.nationalbank.repository;

import com.nationalbank.model.Banking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankingRepository extends JpaRepository<Banking, Long> {
    boolean existsByCustomerId(Long customerId);
}