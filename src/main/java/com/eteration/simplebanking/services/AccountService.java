package com.eteration.simplebanking.services;

import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.repository.AccountRepository;
import com.eteration.simplebanking.controller.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    public Account findAccount(String accountNumber) {
        return accountRepository.findById(accountNumber)
                .orElse(null);
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    public TransactionStatus credit(String accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        account.deposit(amount);
        accountRepository.save(account);
        return new TransactionStatus("OK", UUID.randomUUID().toString());
    }

    @Transactional
    public TransactionStatus debit(String accountNumber, double amount) throws InsufficientBalanceException {
        Account account = findAccount(accountNumber);
        account.withdraw(amount);
        accountRepository.save(account);
        return new TransactionStatus("OK", UUID.randomUUID().toString());
    }
}
