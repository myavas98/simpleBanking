package com.eteration.simplebanking;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.eteration.simplebanking.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ModelTest {
	
	@Test
	public void testCreateAccountAndSetBalance0() {
		Account account = new Account("Kerem Karaca", "17892");
		assertTrue(account.getOwner().equals("Kerem Karaca"));
		assertTrue(account.getAccountNumber().equals("17892"));
		assertTrue(account.getBalance() == 0);
	}

	@Test
	public void testDepositIntoBankAccount() {
		Account account = new Account("Demet Demircan", "9834");
		account.deposit(100);
		assertTrue(account.getBalance() == 100);
	}

	@Test
	public void testWithdrawFromBankAccount() throws InsufficientBalanceException {
		Account account = new Account("Demet Demircan", "9834");
		account.deposit(100);
		assertTrue(account.getBalance() == 100);
		account.withdraw(50);
		assertTrue(account.getBalance() == 50);
	}

	@Test
	public void testWithdrawException() {
		Assertions.assertThrows( InsufficientBalanceException.class, () -> {
			Account account = new Account("Demet Demircan", "9834");
			account.deposit(100);
			account.withdraw(500);
		  });

	}
	
	@Test
	public void testTransactions() throws InsufficientBalanceException {
		// Create account
		Account account = new Account("Canan Kaya", "1234");
		assertTrue(account.getTransactions().size() == 0);

		// Deposit Transaction
		DepositTransaction depositTrx = new DepositTransaction(100);
		assertTrue(depositTrx.getDate() != null);
		account.post(depositTrx);
		assertTrue(account.getBalance() == 100);
		assertTrue(account.getTransactions().size() == 1);

		// Withdrawal Transaction
		WithdrawalTransaction withdrawalTrx = new WithdrawalTransaction(60);
		assertTrue(withdrawalTrx.getDate() != null);
		account.post(withdrawalTrx);
		assertTrue(account.getBalance() == 40);
		assertTrue(account.getTransactions().size() == 2);
	}

	@Test
	public void testPhoneBillPaymentTransaction() throws InsufficientBalanceException {
		Account account = new Account("Jim", "12345");
		account.post(new DepositTransaction(1000));
		account.post(new WithdrawalTransaction(200));
		account.post(new PhoneBillPaymentTransaction("Vodafone", "5423345566", 96.50));
		assertEquals(account.getBalance(), 703.50, 0.0001);
	}

	@Test
	public void testMultiplePhoneBillPayments() throws InsufficientBalanceException {
		Account account = new Account("Jim", "12345");
		account.post(new DepositTransaction(1000));
		account.post(new PhoneBillPaymentTransaction("Vodafone", "5423345566", 96.50));
		account.post(new PhoneBillPaymentTransaction("Turkcell", "5423345567", 103.50));
		assertEquals(account.getBalance(), 800.00, 0.0001);
	}

	@Test
	public void testInsufficientBalancePhoneBill() {
		Account account = new Account("Jim", "12345");
		Assertions.assertThrows(InsufficientBalanceException.class, () -> {
			account.post(new PhoneBillPaymentTransaction("Vodafone", "5423345566", 96.50));
		});
	}
	@Test
	public void testComplexTransactionSequence() throws InsufficientBalanceException {
		Account account = new Account("Jim", "12345");
		account.post(new DepositTransaction(1000));
		account.post(new WithdrawalTransaction(200));
		account.post(new PhoneBillPaymentTransaction("Vodafone", "5423345566", 96.50));
		account.post(new DepositTransaction(500));
		account.post(new PhoneBillPaymentTransaction("Turkcell", "5423345567", 103.50));
		assertEquals(account.getBalance(), 1100.00, 0.0001);
		assertEquals(account.getTransactions().size(), 5);
	}
}
