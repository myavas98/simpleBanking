package com.simplebanking.model; // Corrected package

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("DepositTransaction")
public class DepositTransaction extends Transaction {
    
    protected DepositTransaction() {
        super(0.0);
    }

    public DepositTransaction(double amount) {
        super(amount);
    }

    @Override
    public void execute(Account account) {
        account.credit(this.getAmount());
    }
}
