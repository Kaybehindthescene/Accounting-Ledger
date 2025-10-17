package com.pluralsight;

import java.nio.file.*;
import java.util.*;
import java.io.IOException;

// the transaction file manages reading and writing transaction data to
// the csv file
public class TransactionsFile {
    // field that remembers the location of my csv file
    private final Path path;

    // this constructor runs when you create a new TransactionsFile object
    public TransactionsFile(Path path) {
        this.path = path;
    }

    // declared a method that can read all lines of a text file into Transaction objects
    public List<Transaction> loadAll() throws IOException {
        // a list that will hold all Transaction objects
        List<Transaction> transactions = new ArrayList<>();
        //reads all lines in a file into a list of strings
        List<String> lines = Files.readAllLines(path);
        // loops through each line and process it
        for (String line : lines) {
            if (line.trim().isEmpty()) continue; //skips blank line
            // converts a single line into a transaction
            // object
            Transaction t = Transaction.fromCsvLine(line);
            //adds it to the list
            transactions.add(t);
        }

        return transactions;
    }

    // adds a single Transaction to the file
    public void append(Transaction t) throws IOException {
        // converts transaction object into a csv formatted string
        String line = t.toCsvLine() + System.lineSeparator();
        // writes a new transaction file, keeping the existing data
        Files.writeString(
                path,
                line,
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE
        );
    }
}

