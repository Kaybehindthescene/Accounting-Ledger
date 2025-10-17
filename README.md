# 💰 Accounting Ledger Application  
*A Java CLI app for tracking deposits, payments, and generating financial reports.*

---

## 📑 Table of Contents  
- [Overview](#overview)  
- [Features](#features)  
- [How It Works](#how-it-works)  
- [File Format](#file-format)  
- [Screens & Menus](#screens--menus)  
- [My Favorite Part To Work On](#my-favorite-part-to-work-on)  
- [Author](#author)  
- [Technologies Used](#technologies-used)  
- [Purpose](#purpose)  


---

##  Overview  
The **Accounting Ledger Application** is a command-line Java program that helps users track their **financial transactions**, such as deposits (income) and payments (expenses), directly from the terminal.

All transactions are automatically saved to and read from a CSV file named `transactions.csv`. This allows users to record, view, and generate financial reports whenever they need them.  

This project demonstrates **Object-Oriented Programming (OOP)**, **file I/O**, and **data persistence** using Java.

---

##  Features  
 **Add Deposits** – Record new income with a positive amount.  
 **Make Payments** – Log expenses with negative amounts.  
 **View Ledger** – Display all transactions sorted by newest first.  
 **Reports Menu** – Generate detailed summaries such as:  
   - Month-to-Date  
   - Previous Month  
   - Year-to-Date  
   - Previous Year  
   - Search by Vendor  
   - Custom Date Range  
 **Balance Calculation** – Uses `BigDecimal` for precise totals.  
 **Easter Egg Bonus** – A hidden cheat code grants a secret $1000 deposit! 🎮  

---

##  How It Works  

Each transaction includes five key data points:  
date | time | description | vendor | amount

Example:  
2025-10-14|22:08:42|Deposit|Direct Payroll|1500.00
2025-10-14|12:56:39|Groceries|Walmart|-42.50


**How the program operates:**
1. When you run the program, it automatically loads all existing data from `transactions.csv`.  
2. Users can add new deposits or payments using the main menu.  
3. Each new transaction is appended to the CSV file.  
4. The ledger and reports sections read from the same file and apply filters or sorting.  
5. `BigDecimal` is used for all money calculations to prevent rounding errors.  
6. Date and time stamps are created automatically using `LocalDate` and `LocalTime`.  

---

##  File Format  

Your **transactions.csv** file stores all the data in this format:  
2025-10-10|08:45:12|Deposit|Zelle Transfer|250.00
2025-10-12|14:22:54|Payment|Amazon|-75.00
2025-10-14|22:08:42|Deposit|Direct Payroll|1500.00


Each line represents one transaction entry that can later be filtered or reported on.

---

##  Screens & Menus  

###  Main Menu  
====== ACCOUNTING LEDGER ======
D) Add Deposit
P) Make Payment
L) View Ledger
X) Exit
Choose an option:


###  Ledger Menu  
--- Ledger Menu ---
A) All (newest first)
D) Deposits only
P) Payments only
V) Search by vendor
B) Show balance
R) View Reports
H) Home


###  Reports Menu  
--- Reports Menu ---

1). Month to Date

2). Previous Month

3). Year to Date

4). Previous Year

5). Search by Vendor

6). Custom Date Range

7). Back to Home


---

## 💡 My Favorite Part To Work On  

My favorite part of this project was creating the hidden **Cheat Code Easter Egg**.  
I wanted to include something fun and unexpected in the Accounting Ledger that still showed logical and technical skill.  
When the user types the phrase **“up down left right right down up”** at the main menu, the program detects the sequence and automatically rewards them with a **$1000 Bonus deposit**.  

Here’s the core of the cheat code feature:

```java
// --- Cheat Code  -------------------------------------------------
// Triggers ONLY on the exact 7-word sequence:
// "up down left right right down up" (case-insensitive; spaces allowed)
private static boolean tryCheatCode(String rawInput, TransactionsFile file) throws java.io.IOException {
    if (rawInput == null) return false;

    // must be only letters and spaces (no arrows, no punctuation)
    String s = rawInput.trim();
    if (!s.matches("(?i)^[a-z\\s]+$")) return false;

    // split into words and normalize
    String[] parts = s.toUpperCase().split("\\s+");
    java.util.List<String> words = java.util.Arrays.asList(parts);

    // exact expected sequence
    java.util.List<String> EXPECTED = java.util.Arrays.asList(
            "UP", "DOWN", "LEFT", "RIGHT", "RIGHT", "DOWN", "UP"
    );

    if (!words.equals(EXPECTED)) return false;

    // award the bonus
    Transaction bonus = new Transaction(
            java.time.LocalDate.now(),
            java.time.LocalTime.now(),
            "Bonus",
            "Cheat Code",
            new java.math.BigDecimal("1000.00")
    );
    file.append(bonus);
    System.out.println(" Cheat activated! +$1000.00 added to your ledger.");
    return true;
}


##  Author  
**Created by:** *Caleb Shufford*  
**Program:** Year Up United — Software Development Track  
**Date:** October 2025  

---

##  Technologies Used  
- **Java 17+**  
- **Object-Oriented Programming (OOP)**  
- **File I/O (java.nio.file)**  
- **BigDecimal** for money precision  
- **LocalDate / LocalTime** for timestamps  
- **Collections / Lists** for managing transactions  

---

##  Purpose  
This project demonstrates:  
- File input/output handling  
- Real-world financial logic  
- Data persistence  
- Clean and modular code design  
- Command-line user interaction  

---


