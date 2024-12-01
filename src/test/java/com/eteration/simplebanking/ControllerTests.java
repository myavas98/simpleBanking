package com.eteration.simplebanking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.eteration.simplebanking.controller.AccountController;
import com.eteration.simplebanking.controller.AccountRequest;
import com.eteration.simplebanking.controller.TransactionStatus;
import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.InsufficientBalanceException;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.services.AccountService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
class ControllerTests  {

    @Spy
    @InjectMocks
    private AccountController controller;
 
    @Mock
    private AccountService service;

    
    @Test
    public void givenId_Credit_thenReturnJson()
    throws Exception {
        
        Account account = new Account("Kerem Karaca", "17892");

        doReturn(account).when(service).findAccount( "17892");
        ResponseEntity<TransactionStatus> result = controller.credit( "17892", new DepositTransaction(1000.0));
        verify(service, times(1)).findAccount("17892");
        assertEquals("OK", result.getBody().getStatus());
    }

    @Test
    public void givenId_CreditAndThenDebit_thenReturnJson()
    throws Exception {
        
        Account account = new Account("Kerem Karaca", "17892");

        doReturn(account).when(service).findAccount( "17892");
        ResponseEntity<TransactionStatus> result = controller.credit( "17892", new DepositTransaction(1000.0));
        ResponseEntity<TransactionStatus> result2 = controller.debit( "17892", new WithdrawalTransaction(50.0));
        verify(service, times(2)).findAccount("17892");
        assertEquals("OK", result.getBody().getStatus());
        assertEquals("OK", result2.getBody().getStatus());
        assertEquals(950.0, account.getBalance(),0.001);
    }

   @Test
    public void givenId_CreditAndThenDebitMoreGetException_thenReturnJson()
    throws Exception {
        Assertions.assertThrows( InsufficientBalanceException.class, () -> {
            Account account = new Account("Kerem Karaca", "17892");

            doReturn(account).when(service).findAccount( "17892");
            ResponseEntity<TransactionStatus> result = controller.credit( "17892", new DepositTransaction(1000.0));
            assertEquals("OK", result.getBody().getStatus());
            assertEquals(1000.0, account.getBalance(),0.001);
            verify(service, times(1)).findAccount("17892");

            ResponseEntity<TransactionStatus> result2 = controller.debit( "17892", new WithdrawalTransaction(5000.0));
        });
    }

    @Test
    public void givenId_GetAccount_thenReturnJson()
    throws Exception {
        
        Account account = new Account("Kerem Karaca", "17892");

        doReturn(account).when(service).findAccount( "17892");
        ResponseEntity<Account> result = controller.getAccount( "17892");
        verify(service, times(1)).findAccount("17892");
        assertEquals(account, result.getBody());
    }

    @Test
    public void testCreateAccount() {
        Account account = new Account("Kerem Karaca", "17892");
        AccountRequest request = new AccountRequest();
        request.setOwner("Kerem Karaca");
        request.setAccountNumber("17892");
        
        doNothing().when(service).save(any(Account.class));
        ResponseEntity<Account> response = controller.createAccount(request);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Kerem Karaca", response.getBody().getOwner());
        assertEquals("17892", response.getBody().getAccountNumber());
        
        verify(service, times(1)).save(any(Account.class));
    }

    @Test
    public void testAccountNotFound() {
        doReturn(null).when(service).findAccount("99999");
        ResponseEntity<Account> response = controller.getAccount("99999");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testSuccessfulDebitAndCredit() throws InsufficientBalanceException {
        Account account = new Account("Kerem Karaca", "17892");
        doReturn(account).when(service).findAccount("17892");

        // Test credit
        ResponseEntity<TransactionStatus> creditResponse = controller.credit("17892", new DepositTransaction(1000.0));
        assertEquals(200, creditResponse.getStatusCodeValue());
        assertEquals("OK", creditResponse.getBody().getStatus());
        assertEquals(1000.0, account.getBalance());

        // Test debit
        ResponseEntity<TransactionStatus> debitResponse = controller.debit("17892", new WithdrawalTransaction(500.0));
        assertEquals(200, debitResponse.getStatusCodeValue());
        assertEquals("OK", debitResponse.getBody().getStatus());
        assertEquals(500.0, account.getBalance());
    }

    @Test
    public void testDebitWithInsufficientBalance() {
        Account account = new Account("Kerem Karaca", "17892");
        doReturn(account).when(service).findAccount("17892");

        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            controller.debit("17892", new WithdrawalTransaction(100.0));
        });
    }

    @Test
    public void testCreditToNonexistentAccount() {
        doReturn(null).when(service).findAccount("99999");
        ResponseEntity<TransactionStatus> response = controller.credit("99999", new DepositTransaction(100.0));
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testDebitFromNonexistentAccount() throws InsufficientBalanceException {
        doReturn(null).when(service).findAccount("99999");
        ResponseEntity<TransactionStatus> response = controller.debit("99999", new WithdrawalTransaction(100.0));
        assertEquals(404, response.getStatusCodeValue());
    }

}