package com.eteration.simplebanking.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Account {
    @Id
    private String accountNumber;
    private String owner;
    private double balance;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    private List<Transaction> transactions = new ArrayList<>();

    protected Account() {
        this.transactions = new ArrayList<>();
        this.balance = 0.0;
        this.createDate = new Date();
    }

    public Account(String owner, String accountNumber) {
        this();
        this.owner = owner;
        this.accountNumber = accountNumber;
    }

    public void credit(double amount) {
        this.balance += amount;
    }

    public void debit(double amount) throws InsufficientBalanceException {
        if (this.balance < amount) {
            throw new InsufficientBalanceException();
        }
        this.balance -= amount;
    }

    public void deposit(double amount) {
        Transaction transaction = new DepositTransaction(amount);
        credit(amount);
        transactions.add(transaction);
        transaction.setAccount(this);
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        Transaction transaction = new WithdrawalTransaction(amount);
        debit(amount);
        transactions.add(transaction);
        transaction.setAccount(this);
    }

    public void post(Transaction transaction) throws InsufficientBalanceException {
        transaction.execute(this);
        transactions.add(transaction);
        transaction.setAccount(this);
    }

    public String getOwner() { return owner; }
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactions() { return transactions; }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", owner='" + owner + '\'' +
                ", balance=" + balance +
                '}';
    }
}
