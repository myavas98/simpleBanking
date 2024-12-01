package com.eteration.simplebanking.model;

public class PhoneBillPaymentTransaction extends Transaction {
    private String operator;
    private String phoneNumber;

    public PhoneBillPaymentTransaction(String operator, String phoneNumber, double amount) {
        super(amount);
        this.operator = operator;
        this.phoneNumber = phoneNumber;
    }

    public String getOperator() {
        return operator;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void execute(Account account) throws InsufficientBalanceException {
        account.debit(getAmount());
    }
} 