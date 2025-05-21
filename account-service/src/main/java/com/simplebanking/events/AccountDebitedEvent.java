package com.simplebanking.events;

import java.util.Date;

public class AccountDebitedEvent {
    private String accountId;
    private double amount;
    private Date timestamp;
    // Add getters and setters
    public AccountDebitedEvent() {}
    public AccountDebitedEvent(String accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
        this.timestamp = new Date();
    }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
