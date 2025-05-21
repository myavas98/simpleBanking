package com.simplebanking.events;

public class AccountCreatedEvent {
    private String accountId;
    private String owner;
    // Add getters and setters
    public AccountCreatedEvent() {}
    public AccountCreatedEvent(String accountId, String owner) {
        this.accountId = accountId;
        this.owner = owner;
    }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}
