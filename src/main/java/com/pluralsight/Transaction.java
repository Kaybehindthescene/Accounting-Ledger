package com.pluralsight;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/*
 * Represents a financial transaction with date, time, description, vendor, and amount details.
 */

public class Transaction {
    // fields show the property of a transaction
    private LocalDate date; // Date of when transaction occurred
    private LocalTime time; // time when transaction occurred
    private String description; // description of what the transaction was
    private String vendor; // vendor of what transaction comes from
    private BigDecimal amount; // the total amount of the transaction

// constructor for initializing the variable
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, BigDecimal amount){
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }


    //getter methods
    //getter to get date
    public LocalDate getDate() {
        return date;
    }
    // getter to get time
    public LocalTime getTime() {
        return time;
    }
    // getter to get description
    public String getDescription() {
        return description;
    }
    //getter to get vendor
    public String getVendor() {
        return vendor;
    }
    // getter to get amount
    public BigDecimal getAmount() {
        return amount;
    }

    public String toCsvLine() {
    //Adding a formatter to my time so it could be in the format of the model
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String formattedTime = time.format(timeFormatter);

        return date + "|" + formattedTime + "|" + description + "|" + vendor + "|" + amount;
    }

   public static Transaction fromCsvLine(String line) {
// this is how I split the CvsLine along the | delimeters
        String[] parts = line.split("\\|");

        //guard
       if (parts.length != 5) {
           throw new IllegalArgumentException("Bad Csv:" + line);
       }

       //parse the fields and add a trim so the spaces won't be there
       var date = LocalDate.parse(parts[0].trim());
       var time = LocalTime.parse(parts[1].trim());
       var description = parts[2].trim();
       var vendor = parts[3].trim();
       var amount = new BigDecimal(parts[4].trim());

       //build the project
       return new Transaction(date, time, description, vendor,amount);
   }



    public static void main(String[] args) {
        Transaction t = new Transaction(
                java.time.LocalDate.now(),
                java.time.LocalTime.now(),
                "Groceries",
                "Walmart",
                new java.math.BigDecimal("-42.50")
        );

        System.out.println(t.toCsvLine());

        String s = "2025-10-14|19:26:33|Groceries|Walmart|-42.50";
        Transaction t2 = Transaction.fromCsvLine(s);
        System.out.println(t2.toCsvLine());

    }


}
