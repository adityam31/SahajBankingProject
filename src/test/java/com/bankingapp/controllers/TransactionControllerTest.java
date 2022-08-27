package com.bankingapp.controllers;

import com.bankingapp.models.Transaction;
import com.bankingapp.models.response.Response;
import com.bankingapp.models.response.Status;
import com.bankingapp.services.transactions.TransactionService;
import com.bankingapp.utils.constants.Constants;
import com.bankingapp.utils.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void verifyDeposit_ServiceProcessesTransaction_ShouldReturnSuccessResponseWithBalance() {
        //Given
        Long accountNumber = 100L;
        Double amount = 10000D;
        Double balance = 10000D;
        Transaction transaction = mock(Transaction.class);
        when(transaction.getResultantBalance()).thenReturn(balance);

        when(transactionService.deposit(accountNumber, amount)).thenReturn(transaction);

        //When
        Response response = transactionController.deposit(accountNumber, amount);

        //Then
        assertEquals(Status.SUCCESSFUL, response.getStatus());
        assertEquals(balance, response.getOutput());
        assertNull(response.getErrorMessage());
    }

    @Test
    void verifyDeposit_ServiceThrowsException_ShouldReturnFailedResponseWithError() {
        //Given
        Long accountNumber = 100L;
        Double amount = 10000D;
        String message = "sample";

        when(transactionService.deposit(accountNumber, amount)).thenThrow(new ValidationException(message));

        //When
        Response response = transactionController.deposit(accountNumber, amount);

        //Then
        assertEquals(Status.FAILED, response.getStatus());
        assertEquals(message, response.getErrorMessage());
        assertNull(response.getOutput());
    }

    @Test
    void verifyWithdraw_ServiceProcessesTransaction_ShouldReturnSuccessResponseWithBalance() {
        //Given
        Long accountNumber = 100L;
        Double amount = 500D;
        Double balance = 10000D;
        Transaction transaction = mock(Transaction.class);
        when(transaction.getResultantBalance()).thenReturn(balance);

        when(transactionService.withdraw(accountNumber, amount)).thenReturn(transaction);

        //When
        Response response = transactionController.withdraw(accountNumber, amount);

        //Then
        assertEquals(Status.SUCCESSFUL, response.getStatus());
        assertEquals(balance, response.getOutput());
        assertNull(response.getErrorMessage());
    }

    @Test
    void verifyWithdraw_ServiceThrowsException_ShouldReturnFailedResponseWithError() {
        //Given
        Long accountNumber = 100L;
        Double amount = 500D;
        String message = "sample";

        when(transactionService.withdraw(accountNumber, amount)).thenThrow(new ValidationException(message));

        //When
        Response response = transactionController.withdraw(accountNumber, amount);

        //Then
        assertEquals(Status.FAILED, response.getStatus());
        assertEquals(message, response.getErrorMessage());
        assertNull(response.getOutput());
    }

    @Test
    void verifyTransfer_ServiceProcessesTransaction_ShouldReturnSuccessResponseWithMessage() {
        //Given
        Long senderAccountNumber = 100L;
        Long receiverAccountNumber = 200L;
        Double amount = 500D;

        doNothing().when(transactionService).transfer(senderAccountNumber, receiverAccountNumber, amount);

        //When
        Response response = transactionController.transfer(senderAccountNumber, receiverAccountNumber, amount);

        //Then
        assertEquals(Status.SUCCESSFUL, response.getStatus());
        assertEquals(Constants.Messages.SUCCESSFUL, response.getOutput());
        assertNull(response.getErrorMessage());
    }

    @Test
    void verifyTransfer_ServiceThrowsException_ShouldReturnSuccessResponseWithMessage() {
        //Given
        Long senderAccountNumber = 100L;
        Long receiverAccountNumber = 200L;
        Double amount = 500D;
        String message = "sample";

        doThrow(new ValidationException(message))
                .when(transactionService).transfer(senderAccountNumber, receiverAccountNumber, amount);

        //When
        Response response = transactionController.transfer(senderAccountNumber, receiverAccountNumber, amount);

        //Then
        assertEquals(Status.FAILED, response.getStatus());
        assertEquals(message, response.getErrorMessage());
        assertNull(response.getOutput());
    }

}