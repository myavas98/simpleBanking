package com.simplebanking.controller;

import com.simplebanking.model.Transaction;
import com.simplebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions/v1")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String accountNumber) {
        List<Transaction> transactions = transactionService.findTransactionsByAccountNumber(accountNumber);
        if (transactions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactions);
    }
}
