package com.budget.app.repository;

import com.budget.app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// This is my repository interface for the Transaction entity.
// I let Spring Data JPA handle all the basic CRUD (save, findById, delete, etc.)
// so I don’t have to write SQL by hand.
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Custom query method that Spring builds for me based on the name.
    // This will return all transactions and automatically sort them
    // by the date field in DESCENDING order (newest at the top).
    //
    // I use this when I want to show a clean, recent-first list
    // on my dashboard or transactions page.
    List<Transaction> findAllByOrderByDateDesc();
}
