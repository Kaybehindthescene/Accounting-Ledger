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
        } else if (c.equals("R")) {
            viewReports(file);   // <— new screen (loops until user presses 0)
        } else {
            System.out.println("Back to Home.");
        }
    }


    private static List<Transaction> newestFirst(List<Transaction> input) {
        var list = new ArrayList<>(input);
        list.sort((a, b) -> {
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

    //  vendor contains (case-insensitive)
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

    private static void viewReports(TransactionsFile file) throws java.io.IOException {
        Scanner sc = new java.util.Scanner(System.in);
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

            String ch = sc.nextLine().trim();

            if (ch.equals("0")) {
                System.out.println("Back to the Ledger.");
                break;
            } else if (ch.equals("1")) {
                LocalDate start = java.time.LocalDate.now().withDayOfMonth(1);
                LocalDate end = java.time.LocalDate.now();
                List<Transaction> list = betweenDates(all, start, end);
                for (Transaction t : list) {
                    System.out.println(t.toCsvLine());
                }
                System.out.println("\nSubtotal (MTD):" + balanceOf(list));
            } else if (ch.equals("2")) {   // Previous Month
                LocalDate firstThis = LocalDate.now().withDayOfMonth(1);
                LocalDate start = firstThis.minusMonths(1);
                LocalDate end = firstThis.minusDays(1);
                List<Transaction> list = betweenDates(all, start, end);
                for (Transaction t : list) {
                    System.out.println(t.toCsvLine());
                }
                System.out.println("\nSubtotal (Prev Mo): " + balanceOf(list));

            } else if (ch.equals("3")) {   // Year To Date
                LocalDate start = LocalDate.now().withDayOfYear(1);
                LocalDate end = LocalDate.now();
                List<Transaction> list = betweenDates(all, start, end);
                for (Transaction t : list) {
                    System.out.println(t.toCsvLine());
                }
                System.out.println("\nSubtotal (YTD): " + balanceOf(list));

            } else if (ch.equals("4")) {   // Previous Year
                LocalDate now = LocalDate.now();
                LocalDate start = now.withDayOfYear(1).minusYears(1);
                LocalDate end = now.withDayOfYear(1).minusDays(1);
                List<Transaction> list = betweenDates(all, start, end);
                for (Transaction t : list) {
                    System.out.println(t.toCsvLine());
                }
                System.out.println("\nSubtotal (Prev Yr): " + balanceOf(list));

            } else if (ch.equals("5")) {   // Search by Vendor
                System.out.print("Vendor: ");
                String q = sc.nextLine().trim();
                List<Transaction> list = searchByVendor(all, q);
                for (Transaction t : list) {
                    System.out.println(t.toCsvLine());
                }
                System.out.println("\nSubtotal (Vendor): " + balanceOf(list));

            } else if (ch.equals("6")) {   // Custom Range
                List<Transaction> list = runCustomDateRange(sc, all);
                for (Transaction t : list) {
                    System.out.println(t.toCsvLine());
                }
                System.out.println("\nSubtotal (Range): " + balanceOf(list));

            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static List<Transaction> betweenDates(
            List<Transaction> input,
            LocalDate startInclusive,
            LocalDate endInclusive) {

        List<Transaction> out = new ArrayList<>();

        for (Transaction t : input) {
            LocalDate d = t.getDate();
            boolean withinRange = (d.isEqual(startInclusive) || d.isAfter(startInclusive))
                    && (d.isEqual(endInclusive) || d.isBefore(endInclusive));

            if (withinRange) {
                out.add(t);
            }
        }
        return out;
    }

    private static List<Transaction> runCustomDateRange(
            Scanner sc,
            List<Transaction> all) {

        System.out.print("Start (YYYY-MM-DD): ");
        LocalDate start = LocalDate.parse(sc.nextLine().trim());
        System.out.print("End   (YYYY-MM-DD): ");
        LocalDate end = LocalDate.parse(sc.nextLine().trim());
        return betweenDates(all, start, end);
    }

}



