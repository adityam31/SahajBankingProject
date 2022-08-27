package com.bankingapp.controllers;

import com.bankingapp.models.Account;
import com.bankingapp.models.response.Response;
import com.bankingapp.models.response.Status;
import com.bankingapp.services.accounts.AccountService;
import com.bankingapp.utils.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    void verifyCreateAccount_ServiceCreatesAccount_ShouldReturnSuccessResponseWithAccountNumber() {
        //Given
        String holderName = "James";
        Long accountNumber = 100L;
        Account account = mock(Account.class);
        when(account.getAccountNumber()).thenReturn(accountNumber);

        when(accountService.createAccount(holderName)).thenReturn(account);

        //When
        Response response = accountController.createAccount(holderName);

        //Then
        assertEquals(Status.SUCCESSFUL, response.getStatus());
        assertEquals(accountNumber, response.getOutput());
        assertNull(response.getErrorMessage());
    }

    @Test
    void verifyCreateAccount_ServiceThrowsException_ShouldReturnFailedResponseWithError() {
        //Given
        String holderName = "James";
        String errorMessage = "sample";
        when(accountService.createAccount(holderName)).thenThrow(new ValidationException(errorMessage));

        //When
        Response response = accountController.createAccount(holderName);

        //Then
        assertEquals(Status.FAILED, response.getStatus());
        assertEquals(errorMessage, response.getErrorMessage());
        assertNull(response.getOutput());
    }

    @Test
    void verifyCheckBalance_ServiceReturnsBalance_ShouldReturnSuccessResponseWithBalance() {
        //Given
        Long accountNumber = 100L;
        Double balance = 1000D;
        when(accountService.checkBalance(accountNumber)).thenReturn(balance);

        //When
        Response response = accountController.checkBalance(accountNumber);

        //Then
        assertEquals(Status.SUCCESSFUL, response.getStatus());
        assertEquals(balance, response.getOutput());
        assertNull(response.getErrorMessage());
    }

    @Test
    void verifyCheckBalance_ServiceThrowsException_ShouldReturnFailedResponseWithError() {
        //Given
        Long accountNumber = 100L;
        String errorMessage = "sample";
        when(accountService.checkBalance(accountNumber)).thenThrow(new ValidationException(errorMessage));

        //When
        Response response = accountController.checkBalance(accountNumber);

        //Then
        assertEquals(Status.FAILED, response.getStatus());
        assertEquals(errorMessage, response.getErrorMessage());
        assertNull(response.getOutput());
    }
}
