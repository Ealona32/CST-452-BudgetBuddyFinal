package com.budget.app.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    // This is my primary key for each transaction row in the table.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic name/label of the transaction (ex: “Paycheck” or “Target run”).
    @Column(nullable = false)
    private String name;

    // Dollar amount for this transaction. I’m using Double to keep it simple.
    @Column(nullable = false)
    private Double amount;

    // This tells me if it’s money coming in or going out.
    // I’m using values like "INCOME" or "EXPENSE" in the app.
    @Column(nullable = false)
    private String type; // INCOME or EXPENSE

    // High–level category so I can group and analyze spending
    // (like “Groceries”, “Rent”, “Fun”, etc.).
    @Column(nullable = false)
    private String category;

    // Actual date of the transaction.
    // The DateTimeFormat lets Spring bind the value from my form correctly.
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    // Optional notes if the user wants to add a little extra detail.
    @Column(length = 255)
    private String note;

    // Empty constructor required by JPA. I just leave it simple.
    public Transaction() {}

    // ======================= GETTERS & SETTERS =======================
    // All of these are the standard getters and setters
    // so Spring and Thymeleaf can read/write the fields.

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public Double getAmount() { 
        return amount; 
    }

    public void setAmount(Double amount) { 
        this.amount = amount; 
    }

    public String getType() { 
        return type; 
    }

    public void setType(String type) { 
        this.type = type; 
    }

    public String getCategory() { 
        return category; 
    }

    public void setCategory(String category) { 
        this.category = category; 
    }

    public LocalDate getDate() { 
        return date; 
    }

    public void setDate(LocalDate date) { 
        this.date = date; 
    }

    public String getNote() { 
        return note; 
    }

    public void setNote(String note) { 
        this.note = note; 
    }
}
