package com.tracker.finance.modules.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUser_UsernameOrderByDateDesc(String username);

    // New method for analytics
    List<Transaction> findByUser_UsernameAndDateBetween(String username, LocalDate startDate, LocalDate endDate);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.user WHERE t.id = :id")
    Optional<Transaction> findByIdWithUser(@Param("id") UUID id);

}