package com.pluralsight;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/*
 * AccountingLedger.java
 * This class serves as the main controller for your accounting ledger application.
 * It handles user input, menu navigation, and interacts with the Transaction
 * and TransactionsFile classes to record and display  data.
 *
 * Features include:
 *   - Adding deposits and payments
 *   - Viewing all transactions
 *   - Searching by vendor
 *   - Viewing balances and reports
 */

public class AccountingLedger {
    // --------------------------------------
    // The entry point of the program.
// Displays the main menu and waits for user input.
// Calls other methods based on the user's choice.

    static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Path filePath = Path.of("transactions.csv");
        TransactionsFile file = new TransactionsFile(filePath);

        while (true) {
            System.out.println("\n====== ACCOUNTING LEDGeR ======");
            System.out.println("D) ADD Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) View Ledger");
            System.out.println("X) Exit");
            System.out.println("Choose an option:");

            String choice = scanner.nextLine().trim().toUpperCase();
            if (tryCheatCode(choice, file)) {
                continue;
            }

            if (choice.equals("D")) {
                addDeposit(scanner, file);
            } else if (choice.equals("P")) {
                makePayment(scanner, file);
            } else if (choice.equals("L")) {
                viewLedger(file);
            } else if (choice.equals("X")) {
                System.out.println("Goodbye!!!!!!");
                break;
            } else {
                System.out.println("Invalid choice! Please try Again!!!");
            }
        }

    }

    // -------------------------------------------------------------
    // Handles adding a new deposit transaction.
    // Prompts the user for a description, vendor, and amount.
    // Then creates a Transaction object and saves it to the CSV file.
    // -------------------------------------------------------------

    private static void addDeposit(Scanner scanner, TransactionsFile file) throws IOException {
        System.out.println("\n--- Add Deposit ---");

        System.out.println("Enter Description: ");
        String description = scanner.nextLine();

        System.out.println("Enter vendor");
        String vendor = scanner.nextLine();

        System.out.println("Enter Amount:");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        Transaction deposit = new Transaction(
                LocalDate.now(),
                LocalTime.now(),
                description,
                vendor,
                amount
        );

        file.append(deposit);
        System.out.println("Deposit added successfully");
    }

    // -------------------------------------------------------
    // Handles adding a payment (expense).
    // Accepts a positive number but automatically negates it
    // so it becomes a withdrawal in the ledger.
    // Then saves the payment transaction to the CSV file.
    // -------------------------------------------------------

    private static void makePayment(Scanner scanner, TransactionsFile file) throws IOException {
        System.out.println("\n--- Make Payment ---");

        System.out.println("Enter Description: ");
        String description = scanner.nextLine();

        System.out.println("Enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Enter amount: ");
        String amountInput = scanner.nextLine().trim();
        BigDecimal amount = new BigDecimal(amountInput);


        //if its positive flip the sign to negative
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            amount = amount.negate();
        }

        Transaction payment = new Transaction(
                java.time.LocalDate.now(),
                java.time.LocalTime.now(),
                description,
                vendor,
                amount
        );

        file.append(payment);
        System.out.println("Payment recorded successfully");
    }

    // -------------------------------------------------------------
    // Displays the Ledger submenu, where the user can:
    //   - View all transactions
    //   - Filter by deposits or payments
    //   - Search transactions by vendor
    //  - See balance or open Reports
    // The method reads all transactions from the file, sorts or filters them,
    // and displays results based on user input.
    // -------------------------------------------------------------

    private static void viewLedger(TransactionsFile file) throws java.io.IOException {
        var all = file.loadAll();  // Load every transaction from file

        System.out.println("\n--- Ledger Menu ---");
        System.out.println("A) All (newest first)");
        System.out.println("D) Deposits only");
        System.out.println("P) Payments only");
        System.out.println("B) Show balance");
        System.out.println("R) View Reports");
        System.out.println("H) Home");
        System.out.print("Choose: ");

        Scanner sc = new java.util.Scanner(System.in);
        String choice = sc.nextLine().trim().toUpperCase();

        if (choice.equals("A")) {
            for (Transaction t : newestFirst(all)) System.out.println(t.toCsvLine());
        } else if (choice.equals("D")) {
            for (Transaction t : newestFirst(depositsOnly(all))) System.out.println(t.toCsvLine());
        } else if (choice.equals("P")) {
            for (Transaction t : newestFirst(paymentsOnly(all))) System.out.println(t.toCsvLine());
        } else if (choice.equals("B")) {
            System.out.println("Balance: " + balanceOf(all));
        } else if (choice.equals("R")) {
            viewReports(file);   // <— new screen (loops until user presses 0)
        } else {
            System.out.println("Back to Home.");
        }
    }

    // -------------------------------------------------------------
    // Sorts transactions so the newest entries appear first.
    // It compares both the date and time of each transaction.
    // -------------------------------------------------------------
    private static List<Transaction> newestFirst(List<Transaction> input) {
        List<Transaction> list = new ArrayList<>(input);
        list.sort((a, b) -> {
            int byDate = b.getDate().compareTo(a.getDate());
            if (byDate != 0) return byDate;
            return b.getTime().compareTo(a.getTime());
        });
        return list;
    }

    // Calculates the total balance by adding all transaction amounts.
    // Deposits increase the total; payments (negative amounts) decrease it.
    // -------------------------------------------------------------
    private static java.math.BigDecimal balanceOf(List<Transaction> input) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Transaction t : input) sum = sum.add(t.getAmount());
        return sum;
    }
    // -------------------------------------------------------------
    //  positive amounts only
    // Filters the list of transactions to include only positive amounts.
    // Used to show all deposits or income transactions.
    // -------------------------------------------------------------
    private static List<Transaction> depositsOnly(List<Transaction> input) {
        List<Transaction> out = new ArrayList<Transaction>();
        for (Transaction t : input) {
            if (t.getAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                out.add(t);
            }
        }
        return out;
    }


    //  negative amounts only
    // Filters the list of transactions to include only negative amounts.
    // Used to show all outgoing payments or expenses.
    // -------------------------------------------------------------
    private static List<Transaction> paymentsOnly(List<Transaction> input) {
        List<Transaction> result = new ArrayList<Transaction>();
        for (Transaction t : input) {
            if (t.getAmount().compareTo(java.math.BigDecimal.ZERO) < 0) {
                result.add(t);
            }
        }
        return result;
    }

   /* private static void runningBalance(List<Transaction> input) {
        var list = newestFirst(input);
        var running = BigDecimal.ZERO;

        //so the program computes correctly need the list in oldest - newest order
        java.util.Collections.reverse(list);

        for (var t : list) {
            running = running.add(t.getAmount());
            System.out.println(t.toCsvLine() + " | balance=$" + running);
        }
    }*/


    // Opens the Reports menu, which provides date-based financial summaries.
    // Options include:
    //   1) Month to Date
    //   2) Previous Month
    //   3) Year to Date
    //   4) Previous Year
    //   5) Search by Vendor
    //   6) Custom Date Range
    // Each report filters transactions using betweenDates() and prints totals.
    private static void viewReports(TransactionsFile file) throws java.io.IOException {
        Scanner sc = new Scanner(System.in);
        List<Transaction> all = file.loadAll();

        while (true) {
            System.out.println("\n--- Reports ---");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Date Range");
            System.out.println("0) Back");
            System.out.print("Choose: ");

            String reportChoice = sc.nextLine().trim();

            if (reportChoice.equals("0")) {
                System.out.println("Back to Ledger.");
                break;
            } else if (reportChoice.equals("1")) {           // Month To Date
                LocalDate start = LocalDate.now().withDayOfMonth(1);
                LocalDate end   = LocalDate.now();
                List<Transaction> list = betweenDates(all, start, end);
                for (Transaction t : list) System.out.println(t.toCsvLine());
                System.out.println("\nSubtotal (MTD): " + balanceOf(list));

            } else if (reportChoice.equals("2")) {            // Previous Month
                LocalDate firstThis = LocalDate.now().withDayOfMonth(1);
                LocalDate start = firstThis.minusMonths(1);
                LocalDate end   = firstThis.minusDays(1);
                List<Transaction> list = betweenDates(all, start, end);
                for (Transaction t : list) System.out.println(t.toCsvLine());
                System.out.println("\nSubtotal (Prev Mo): " + balanceOf(list));

            } else if (reportChoice.equals("3")) {            // Year To Date
                LocalDate start = LocalDate.now().withDayOfYear(1);
                LocalDate end   = LocalDate.now();
                List<Transaction> list = betweenDates(all, start, end);
                for (Transaction t : list) System.out.println(t.toCsvLine());
                System.out.println("\nSubtotal (YTD): " + balanceOf(list));

            } else if (reportChoice.equals("4")) {            // Previous Year
                LocalDate now = LocalDate.now();
                LocalDate start = now.withDayOfYear(1).minusYears(1);
                LocalDate end   = now.withDayOfYear(1).minusDays(1);
                List<Transaction> list = betweenDates(all, start, end);
                for (Transaction t : list) System.out.println(t.toCsvLine());
                System.out.println("\nSubtotal (Prev Yr): " + balanceOf(list));

            } else if (reportChoice.equals("5")) {  // Search by Vendor
                System.out.print("Vendor: ");
                String q = sc.nextLine().trim();

                if (q.isEmpty()) {
                    System.out.println("No vendor entered. Returning to Reports.");
                } else {
                    List<Transaction> matches = searchByVendor(all, q);

                    if (matches.isEmpty()) {
                        System.out.println("No transactions found for vendor: " + q);
                    } else {
                        List<Transaction> sorted = newestFirst(matches);
                        for (Transaction t : sorted) {
                            System.out.println(t.toCsvLine());
                        }
                        System.out.println("\nSubtotal (Vendor): " + balanceOf(matches));
                    }
                }
            } else if (reportChoice.equals("6")) {            // Custom Range
                List<Transaction> list = runCustomDateRange(sc, all);
                for (Transaction t : list) System.out.println(t.toCsvLine());
                System.out.println("\nSubtotal (Range): " + balanceOf(list));

            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    // 🔍 Filters transactions by vendor name (case-insensitive)
    private static List<Transaction> searchByVendor(List<Transaction> input, String query) {
        String lowerQuery = query.toLowerCase();
        List<Transaction> result = new ArrayList<>();

        for (Transaction t : input) {
            if (t.getVendor().toLowerCase().contains(lowerQuery)) {
                result.add(t);
            }
        }
        return result;
    }

    // Filters transactions between two LocalDate values (inclusive).
    // Returns only the transactions that fall within the given range.
    private static List<Transaction> betweenDates(
            List<Transaction> input,
            LocalDate startInclusive,
            LocalDate endInclusive) {

        List<Transaction> filteredList = new ArrayList<>();

        for (Transaction t : input) {
            LocalDate d = t.getDate();
            boolean withinRange = (d.isEqual(startInclusive) || d.isAfter(startInclusive))
                    && (d.isEqual(endInclusive) || d.isBefore(endInclusive));

            if (withinRange) {
                filteredList.add(t);
            }
        }
        return filteredList;
    }

    // Allows the user to enter a custom start and end date
    // to generate a report for a specific range.
    private static List<Transaction> runCustomDateRange(
            Scanner sc,
            List<Transaction> all) {

        System.out.print("Start (YYYY-MM-DD): ");
        LocalDate start = LocalDate.parse(sc.nextLine().trim());
        System.out.print("End   (YYYY-MM-DD): ");
        LocalDate end = LocalDate.parse(sc.nextLine().trim());
        return betweenDates(all, start, end);
    }

    // --- Cheat Code -------------------------------------------------
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
                "UP", "UP", "DOWN", "DOWN", "LEFT", "RIGHT", "LEFT", "RIGHT", "B", "A", "START"
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


}



