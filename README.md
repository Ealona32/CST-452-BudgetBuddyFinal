# BudgetBuddy – CST-452 Final Project

BudgetBuddy is a simple budget tracking web app I built for my CST-452 Capstone.  
The goal is to give users a clean dashboard where they can register, log in, and
keep track of income and expenses with charts and totals.

---

## Tech Stack

- Java 17  
- Spring Boot 3  
- Spring Data JPA  
- Thymeleaf  
- MySQL  
- Chart.js  
- HTML / CSS

---

## Main Features

- **User accounts**
  - Register with name, email, and password
  - Log in and log out
  - Session-based greeting: “Welcome, \<name\>”

- **Dashboard**
  - Total income, total expenses, and overall balance
  - Savings rate percentage (based on balance vs income)
  - Donut chart: spending by category for the current month
  - Line chart: recent cash flow using recent transactions
  - Recent transactions table (latest 5)

- **Transactions**
  - Add new transactions (income or expense)
  - Edit existing transactions
  - Delete transactions
  - Filter list by type: ALL / INCOME / EXPENSE
  - Categories like Bills, Rent, Food, Shopping, Travel, etc.

---

## Project Structure

- `src/main/java/com/budget/app`
  - `controller/`
    - `AuthController` – handles login, register, and logout
    - `BudgetController` – dashboard + transactions views
  - `model/`
    - `User` – user entity for login
    - `Transaction` – stores each income/expense row
  - `repository/`
    - `UserRepository` – Spring Data JPA for users
    - `TransactionRepository` – Spring Data JPA for transactions
  - `service/`
    - `TransactionService` – business logic for totals, monthly stats, and filters
  - `BudgetBuddyApplication` – main Spring Boot application class

- `src/main/resources/templates`
  - `login.html` – login page
  - `register.html` – registration page
  - `dashboard.html` – main dashboard with charts + summaries
  - `transactions.html` – full transaction list and filters
  - `transaction_form.html` – add/edit transaction form

- `src/main/resources/static/css`
  - `style.css` – custom styles for the “BudgetBuddy” look

---

## How to Run the Project

### 1. Requirements

- Java 17+
- Maven
- MySQL running locally

### 2. Create the database

In MySQL, create a database (name can be `budgetbuddy`):

```sql
CREATE DATABASE budgetbuddy;
