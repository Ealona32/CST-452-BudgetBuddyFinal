package com.budget.app.controller;

import com.budget.app.model.Transaction;
import com.budget.app.service.TransactionService;
import com.budget.app.service.TransactionService.CategoryTotal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Controller
public class BudgetController {

    // This is my main service for anything related to transactions and totals.
    // I call it "service" because it basically sits in the middle
    // between my controller and the database.
    private final TransactionService service;

    // Spring injects the TransactionService here using constructor injection.
    public BudgetController(TransactionService service) {
        this.service = service;
    }

    // ======================= DASHBOARD (HOME) =======================

    // This is my main dashboard page: /
    // When the user first logs in, this is what they see.
    @GetMapping("/")
    public String dashboard(Model model) {

        // Pull overall income and expenses across all time.
        double income = service.getTotalIncome();
        double expenses = service.getTotalExpenses();

        // Simple overall balance = income - expenses.
        double balance = income - expenses;

        // Get the current date and figure out which month we’re in.
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(today);

        // I build a friendly label like "December 2025" for the UI.
        String monthLabel = currentMonth.getMonth()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + currentMonth.getYear();

        // Monthly numbers just for the current month (this is more detailed).
        double monthlyIncome = service.getMonthlyIncome(currentMonth);
        double monthlyExpenses = service.getMonthlyExpenses(currentMonth);
        double monthlyBalance = monthlyIncome - monthlyExpenses;

        // Get the top expense categories for the current month
        // so I can show a simple breakdown (like a mini analytics section).
        List<CategoryTotal> topCategories = service.getTopExpenseCategories(currentMonth, 3);

        // Add all the overall totals to the model so my dashboard.html can display cards.
        model.addAttribute("income", income);
        model.addAttribute("expenses", expenses);
        model.addAttribute("balance", balance);

        // I also show a small list of the most recent transactions (like a feed).
        model.addAttribute("recent", service.getRecent(5));

        // All my monthly analytics values
        model.addAttribute("monthLabel", monthLabel);
        model.addAttribute("monthlyIncome", monthlyIncome);
        model.addAttribute("monthlyExpenses", monthlyExpenses);
        model.addAttribute("monthlyBalance", monthlyBalance);
        model.addAttribute("topCategories", topCategories);

        // This returns my main dashboard Thymeleaf page.
        return "dashboard";
    }

    // ======================= TRANSACTIONS LIST =======================

    // This shows the full transactions page with optional filtering by type.
    @GetMapping("/transactions")
    public String listTransactions(
            @RequestParam(name = "type", required = false) String type,
            Model model) {

        // If "type" is null or empty, the service returns all transactions.
        // Otherwise, it filters by type (like INCOME or EXPENSE).
        var transactions = service.getByType(type);

        // I use this to highlight which filter is currently active in the UI.
        String selectedType = (type == null || type.isBlank()) ? "ALL" : type.toUpperCase();

        model.addAttribute("transactions", transactions);
        model.addAttribute("selectedType", selectedType);

        // I also re-use the overall totals here, so the transactions page
        // can still show summary cards at the top.
        double income = service.getTotalIncome();
        double expenses = service.getTotalExpenses();
        double balance = income - expenses;

        model.addAttribute("totalIncome", income);
        model.addAttribute("totalExpenses", expenses);
        model.addAttribute("totalBalance", balance);

        // This maps to transactions.html
        return "transactions";
    }

    // ======================= NEW TRANSACTION FORM =======================

    // Shows the empty form for adding a new transaction.
    @GetMapping("/transactions/new")
    public String newTransaction(Model model) {
        // I pass an empty Transaction object so the form can bind to it.
        model.addAttribute("transaction", new Transaction());

        // I also pass a title so I can reuse the same form for add vs edit.
        model.addAttribute("formTitle", "Add Transaction");
        return "transaction_form";
    }

    // Handles both creating a new transaction and saving edits.
    @PostMapping("/transactions")
    public String saveTransaction(@ModelAttribute("transaction") Transaction transaction) {

        // I let the service handle the actual save logic.
        // If the transaction has an ID, it’s an edit; if not, it’s a new one.
        service.save(transaction);

        // After saving, I just send the user back to the transactions list.
        return "redirect:/transactions";
    }

    // ======================= EDIT TRANSACTION =======================

    // This loads an existing transaction into the form for editing.
    @GetMapping("/transactions/{id}/edit")
    public String editTransaction(@PathVariable Long id, Model model) {

        // Ask the service for the transaction by ID.
        Transaction t = service.getById(id);

        // If we don’t find it, I just send them back to the list page.
        if (t == null) {
            return "redirect:/transactions";
        }

        // Otherwise, I put that transaction into the model so the form
        // can pre-populate with its values.
        model.addAttribute("transaction", t);

        // Update the title so the form shows "Edit" instead of "Add".
        model.addAttribute("formTitle", "Edit Transaction");

        return "transaction_form";
    }

    // ======================= DELETE TRANSACTION =======================

    // Simple delete endpoint – called when the user removes a transaction.
    @PostMapping("/transactions/{id}/delete")
    public String deleteTransaction(@PathVariable Long id) {

        // Let the service handle the delete and then go back to the list.
        service.delete(id);
        return "redirect:/transactions";
    }

}
