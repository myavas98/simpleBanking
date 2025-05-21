package com.simplebanking.controller;

import com.simplebanking.model.*;
import com.simplebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/account/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        Account account = accountService.findAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/credit/{accountNumber}")
    public ResponseEntity<TransactionStatus> credit(@PathVariable String accountNumber, @RequestBody DepositTransaction deposit) {
        Account account = accountService.findAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        
        account.deposit(deposit.getAmount());
        // accountService.save(account); // Save is handled by credit/debit methods in AccountService
        
        try {
            TransactionStatus status = accountService.credit(accountNumber, deposit.getAmount());
            return ResponseEntity.ok(status);
        } catch (InsufficientBalanceException e) { // Should not happen for credit, but good practice
            return ResponseEntity.badRequest().body(new TransactionStatus("ERROR", e.getMessage()));
        }
    }

    @PostMapping("/debit/{accountNumber}")
    public ResponseEntity<TransactionStatus> debit(@PathVariable String accountNumber, @RequestBody WithdrawalTransaction withdrawal) {
        // Account account = accountService.findAccount(accountNumber); // Account finding is in AccountService
        // if (account == null) { // Account finding is in AccountService
        //     return ResponseEntity.notFound().build();
        // }
        
        try {
            TransactionStatus status = accountService.debit(accountNumber, withdrawal.getAmount());
            return ResponseEntity.ok(status);
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(new TransactionStatus("ERROR", e.getMessage()));
        }
    }

    // Inner class for the request body of account creation
    static class AccountRequest {
        private String owner;
        private String accountNumber;

        // Getters and Setters
        public String getOwner() {
            return owner;
        }
        public void setOwner(String owner) {
            this.owner = owner;
        }
        public String getAccountNumber() {
            return accountNumber;
        }
        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequest request) {
        Account account = new Account(request.getOwner(), request.getAccountNumber());
        // The save method in AccountService now handles event publishing
        Account savedAccount = accountService.save(account); 
        return ResponseEntity.ok(savedAccount);
    }

}
