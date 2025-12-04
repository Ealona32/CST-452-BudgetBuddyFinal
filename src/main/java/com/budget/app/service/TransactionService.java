package com.budget.app.service;

import com.budget.app.model.Transaction;
import com.budget.app.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    // Small helper class I use for category analytics.
    // It basically holds "category name" + "total spent in that category".
    public static class CategoryTotal {
        private final String category;
        private final double total;

        public CategoryTotal(String category, double total) {
            this.category = category;
            this.total = total;
        }

        public String getCategory() {
            return category;
        }

        public double getTotal() {
            return total;
        }
    }

    // This is my connection to the transactions table.
    // I keep all the database calls inside the repository.
    private final TransactionRepository repo;

    // Constructor injection so Spring gives me the repo.
    public TransactionService(TransactionRepository repo) {
        this.repo = repo;
    }

    // ======================= BASIC CRUD HELPERS =======================

    // Get every transaction from the database,
    // ordered so the newest ones show up first.
    public List<Transaction> getAll() {
        return repo.findAllByOrderByDateDesc();
    }

    // Grab only the most recent "limit" number of transactions.
    // I mainly use this for the little recent activity list on the dashboard.
    public List<Transaction> getRecent(int limit) {
        return getAll().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Find one transaction by its id.
    // If it doesn’t exist, I just return null instead of blowing up.
    public Transaction getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // Save or update a transaction.
    // If the entity has an id, JPA treats it like an update;
    // if it doesn’t, it’s a new insert.
    public void save(Transaction t) {
        repo.save(t);
    }

    // Delete a transaction based on its id.
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ======================= OVERALL TOTALS =======================

    // Total income across all time.
    public double getTotalIncome() {
        return getAll().stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getType()))
                .mapToDouble(t -> t.getAmount() != null ? t.getAmount() : 0.0)
                .sum();
    }

    // Total expenses across all time.
    public double getTotalExpenses() {
        return getAll().stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
                .mapToDouble(t -> t.getAmount() != null ? t.getAmount() : 0.0)
                .sum();
    }

    // ======================= MONTHLY HELPERS =======================

    // Total income for a specific month (ex: December 2025).
    public double getMonthlyIncome(YearMonth month) {
        return getAll().stream()
                .filter(t -> t.getDate() != null &&
                        YearMonth.from(t.getDate()).equals(month) &&
                        "INCOME".equalsIgnoreCase(t.getType()))
                .mapToDouble(t -> t.getAmount() != null ? t.getAmount() : 0.0)
                .sum();
    }

    // Total expenses for a specific month.
    public double getMonthlyExpenses(YearMonth month) {
        return getAll().stream()
                .filter(t -> t.getDate() != null &&
                        YearMonth.from(t.getDate()).equals(month) &&
                        "EXPENSE".equalsIgnoreCase(t.getType()))
                .mapToDouble(t -> t.getAmount() != null ? t.getAmount() : 0.0)
                .sum();
    }

    // Build a ranked list of top expense categories for a given month.
    // I use this for my little "Top Categories" summary on the dashboard.
    public List<CategoryTotal> getTopExpenseCategories(YearMonth month, int limit) {
        Map<String, Double> totals = new HashMap<>();

        getAll().stream()
                .filter(t -> t.getDate() != null &&
                        YearMonth.from(t.getDate()).equals(month) &&
                        "EXPENSE".equalsIgnoreCase(t.getType()))
                .forEach(t -> {
                    double amt = t.getAmount() != null ? t.getAmount() : 0.0;
                    String cat = t.getCategory() != null ? t.getCategory() : "Other";

                    // Add the amount into the matching category bucket.
                    totals.merge(cat, amt, Double::sum);
                });

        // Convert the map into a list of CategoryTotal and sort it by total DESC.
        return totals.entrySet().stream()
                .map(e -> new CategoryTotal(e.getKey(), e.getValue()))
                .sorted((a, b) -> Double.compare(b.getTotal(), a.getTotal()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // ======================= FILTER BY TYPE =======================

    // This method supports my transactions page filter.
    // If type is null/blank, I just return everything.
    // Otherwise I only return transactions that match that type (INCOME/EXPENSE).
    public List<Transaction> getByType(String type) {
        if (type == null || type.isBlank()) {
            return getAll();
        }

        return getAll().stream()
                .filter(t -> t.getType() != null &&
                        t.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }
}
