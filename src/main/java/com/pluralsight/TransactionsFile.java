package com.pluralsight;

import java.nio.file.*;
import java.util.*;
import java.io.IOException;

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
        List<String> lines = Files.readAllLines(path);

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            Transaction t = Transaction.fromCsvLine(line);
            transactions.add(t);
        }

        return transactions;
    }

    // adds a single Transaction to the file
    public void append(Transaction t) throws IOException {
        String line = t.toCsvLine() + System.lineSeparator();

        Files.writeString(
                path,
                line,
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE
        );
    }
}

