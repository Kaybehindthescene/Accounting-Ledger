package com.pluralsight;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private BigDecimal amount;


    public Transaction(LocalDate date, LocalTime time, String description, String vendor, BigDecimal amount){
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }


    //getter methods
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String toCsvLine() {
        return date + "|" + time + "|" + description + "|" + vendor + "|" + amount;
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
    }

}
