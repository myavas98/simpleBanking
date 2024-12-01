package com.eteration.simplebanking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Date date;
    private double amount;
    private String approvalCode;
    
    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number")
    private Account account;

    protected Transaction() {
        this.date = new Date();
        this.approvalCode = UUID.randomUUID().toString();
    }

    public Transaction(double amount) {
        this();
        this.amount = amount;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public abstract void execute(Account account) throws InsufficientBalanceException;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
