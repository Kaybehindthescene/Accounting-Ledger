package com.pluralsight;
// Imports necessary classes for handling files, dates, and lists
import java.nio.file.Path;
import java.util.List;
import java.io.IOException;

public class TestLedger {
    public static void main(String[] args) throws IOException {
        Path filePath = Path.of("transactions.csv");   // relative to project root
        TransactionsFile file = new TransactionsFile(filePath);

        Transaction newTransaction = new Transaction(
                java.time.LocalDate.now(),
                java.time.LocalTime.now(),
                "Deposit",
                "Direct Payroll",
                new java.math.BigDecimal("1500.00")
        );

        file.append(newTransaction);
        System.out.println("Added new transaction!");


        List<Transaction> all = file.loadAll();

        // 3) print to verify
        for (Transaction tr : all) {
            System.out.println(tr.toCsvLine());
        }
    }
}

