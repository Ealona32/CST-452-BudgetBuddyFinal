package com.budget.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BudgetBuddyApplication {

    // This is my main starter class.
    //Spring Boot uses this to boot up the whole app.
    public static void main(String[] args) {
        SpringApplication.run(BudgetBuddyApplication.class, args);
    }
}
