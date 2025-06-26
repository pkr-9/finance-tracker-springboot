package com.tracker.finance.modules.budget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {

    List<Budget> findByUser_Username(String username);

    @Query("SELECT b FROM Budget b WHERE b.user.username = :username AND b.month = :monthStart")
    List<Budget> findByUserAndMonth(String username, LocalDate monthStart);

    Optional<Budget> findByUser_UsernameAndCategoryAndMonth(String username, String category, LocalDate month);
}