package com.pluralsight;

import java.io.IOException;
import java.nio.file.Path;
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
}
