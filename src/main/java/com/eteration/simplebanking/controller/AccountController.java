package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.services.AccountService;
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
        accountService.save(account);
        
        return ResponseEntity.ok(new TransactionStatus("OK", UUID.randomUUID().toString()));
    }

    @PostMapping("/debit/{accountNumber}")
    public ResponseEntity<TransactionStatus> debit(@PathVariable String accountNumber, @RequestBody WithdrawalTransaction withdrawal) 
            throws InsufficientBalanceException {
        Account account = accountService.findAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        
        account.withdraw(withdrawal.getAmount());
        accountService.save(account);
        
        return ResponseEntity.ok(new TransactionStatus("OK", UUID.randomUUID().toString()));
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequest request) {
        Account account = new Account(request.getOwner(), request.getAccountNumber());
        accountService.save(account);
        return ResponseEntity.ok(account);
    }

}
