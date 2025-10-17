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

##  My Favorite Part To Work On  
> My favorite part of this project was designing the **Reports Menu**.  
> It was rewarding to use Java’s `LocalDate` class to calculate date ranges such as “Month-to-Date” or “Previous Year.”  
> I also enjoyed learning how to filter transactions dynamically and compute subtotals for each report section.

---

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


