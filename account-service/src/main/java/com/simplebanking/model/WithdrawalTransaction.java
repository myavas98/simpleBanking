package com.simplebanking.model; // Corrected package

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("WithdrawalTransaction")
public class WithdrawalTransaction extends Transaction {
    
    protected WithdrawalTransaction() {
        super(0.0);
    }

    public WithdrawalTransaction(double amount) {
        super(amount);
    }

    @Override
    public void execute(Account account) throws InsufficientBalanceException {
        account.debit(getAmount());
    }
}
