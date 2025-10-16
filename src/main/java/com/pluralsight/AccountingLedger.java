package com.pluralsight;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

public class AccountingLedger {
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

            if (choice.equals("D")){
                addDeposit(scanner, file);
            } else if (choice.equals("P")) {
                makePayment(scanner, file);
            } else if (choice.equals("L")) {
                viewLedger(file);
            } else if (choice.equals("X")) {
                System.out.println("Goodbye!!!!!!");
                break;
            }else {
                System.out.println("Invalid choice! Please try Again!!!");
            }
        }

    }

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

    private static void makePayment(Scanner scanner, TransactionsFile file) throws IOException {
        System.out.println("\n--- Make Payment ---");

        System.out.println("Enter Description: ");
        String description = scanner.nextLine();

        System.out.println("Enter vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Enter amount: ");
        String amountInput = scanner.nextLine().trim();

        java.math.BigDecimal amount = new java.math.BigDecimal(amountInput);
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

    private static void viewLedger(TransactionsFile file) throws java.io.IOException {
        var all = file.loadAll();  // Load every transaction from file

        System.out.println("\n--- Ledger Menu ---");
        System.out.println("A) All (newest first)");
        System.out.println("D) Deposits only");
        System.out.println("P) Payments only");
        System.out.println("V) Search by vendor");
        System.out.println("B) Show balance");
        System.out.println("R) Running balance");
        System.out.println("H) Home");
        System.out.print("Choose: ");

        var sc = new java.util.Scanner(System.in);
        var c = sc.nextLine().trim().toUpperCase();

        if (c.equals("A")) {
            for (var t : newestFirst(all)) System.out.println(t.toCsvLine());
        } else if (c.equals("D")) {
            for (var t : newestFirst(depositsOnly(all))) System.out.println(t.toCsvLine());
        } else if (c.equals("P")) {
            for (var t : newestFirst(paymentsOnly(all))) System.out.println(t.toCsvLine());
        } else if (c.equals("V")) {
            System.out.print("Vendor search: ");
            var q = sc.nextLine().trim();
            var hits = newestFirst(searchByVendor(all, q));
            for (var t : hits) System.out.println(t.toCsvLine());
        } else if (c.equals("B")) {
            System.out.println("Balance: " + balanceOf(all));
        else if (c.equals("R")) {
                viewReportsMenu(new java.util.Scanner(System.in), file);
            }

        } else {
            System.out.println("Back to Home.");
        }
    }


    private static List<Transaction> newestFirst(List<Transaction> input) {
        var list = new ArrayList<>(input);
        list.sort((a, b) ->{
            int byDate = b.getDate().compareTo(a.getDate());
            if (byDate != 0) return byDate;
            return b.getTime().compareTo(a.getTime());
        });
        return list;
    }

    private static java.math.BigDecimal balanceOf(List<Transaction> input) {
        var sum = BigDecimal.ZERO;
        for (var t : input) sum = sum.add(t.getAmount());
        return sum;
    }

    //  positive amounts only
    private static List<Transaction> depositsOnly(List<Transaction> input) {
        var out = new ArrayList<Transaction>();
        for (var t : input) {
            if (t.getAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                out.add(t);
            }
        }
        return out;
    }

    //  negative amounts only
    private static List<Transaction> paymentsOnly(List<Transaction> input) {
        var out = new ArrayList<Transaction>();
        for (var t : input) {
            if (t.getAmount().compareTo(java.math.BigDecimal.ZERO) < 0) {
                out.add(t);
            }
        }
        return out;
    }

    // 🔍 vendor contains (case-insensitive)
    private static List<Transaction> searchByVendor(List<Transaction> input, String query) {
        var q = query.toLowerCase();
        var out = new ArrayList<Transaction>();
        for (var t : input) {
            if (t.getVendor().toLowerCase().contains(q)) {
                out.add(t);
            }
        }
        return out;
    }

    private static void runningBalance(List<Transaction> input) {
        var list = newestFirst(input);
        var running = BigDecimal.ZERO;

        //so the program computes correctly need the list in oldest - newest order
        java.util.Collections.reverse(list);

        for (var t : list) {
            running = running.add(t.getAmount());
            System.out.println(t.toCsvLine() + " | balance=$" + running);
        }
    }

    // secret cheat code





}
