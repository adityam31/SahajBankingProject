package com.bankingapp.services.transactions;

import com.bankingapp.models.Transaction;
import com.bankingapp.services.transactions.executors.DepositExecutor;
import com.bankingapp.services.transactions.executors.WithdrawalExecutor;
import com.bankingapp.utils.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {
    @Mock
    private DepositExecutor depositExecutor;

    @Mock
    private WithdrawalExecutor withdrawalExecutor;

    @InjectMocks
    private TransactionServiceImpl transactionService;


    @Test
    void verifyDeposit_ExecutorProcessesTransaction_ShouldReturnTransaction() {
        //Given
        Long accountNumber = 100L;
        Double amount = 10000D;
        Transaction transaction = mock(Transaction.class);

        when(depositExecutor.transact(accountNumber, amount)).thenReturn(transaction);

        //When
        Transaction actual = transactionService.deposit(accountNumber, amount);

        //Then
        assertEquals(transaction, actual);
    }


    @Test
    void verifyDeposit_ExecutorThrowsException_ShouldThrowException() {
        //Given
        Long accountNumber = 100L;
        Double amount = 10000D;
        String message = "sample";

        when(depositExecutor.transact(accountNumber, amount)).thenThrow(new ValidationException(message));

        //When and Then
        assertThrows(ValidationException.class,
                () -> transactionService.deposit(accountNumber, amount),
                message);
    }

    @Test
    void verifyWithdraw_ExecutorProcessesTransaction_ShouldReturnTransaction() {
        //Given
        Long accountNumber = 100L;
        Double amount = 10000D;
        Transaction transaction = mock(Transaction.class);

        when(withdrawalExecutor.transact(accountNumber, amount)).thenReturn(transaction);

        //When
        Transaction actual = transactionService.withdraw(accountNumber, amount);

        //Then
        assertEquals(transaction, actual);
    }


    @Test
    void verifyWithdraw_ExecutorThrowsException_ShouldThrowException() {
        //Given
        Long accountNumber = 100L;
        Double amount = 10000D;
        String message = "sample";

        when(withdrawalExecutor.transact(accountNumber, amount)).thenThrow(new ValidationException(message));

        //When and Then
        assertThrows(ValidationException.class,
                () -> transactionService.withdraw(accountNumber, amount),
                message);
    }

    @Test
    void verifyTransfer_ExecutorsProcessTransaction_ShouldCallWithdrawAndDepositExecutors() {
        //Given
        Long senderAccountNumber = 100L;
        Long receiverAccountNumber = 200L;
        Double amount = 10000D;
        Transaction withdrawTransaction = mock(Transaction.class);
        Transaction depositTransaction = mock(Transaction.class);

        when(withdrawalExecutor.transact(senderAccountNumber, amount)).thenReturn(withdrawTransaction);
        when(depositExecutor.transact(receiverAccountNumber, amount)).thenReturn(depositTransaction);

        //When
        transactionService.transfer(senderAccountNumber, receiverAccountNumber, amount);

        //Then
        verify(withdrawalExecutor, times(1)).transact(senderAccountNumber, amount);
        verify(depositExecutor, times(1)).transact(receiverAccountNumber, amount);
    }


    @Test
    void verifyTransfer_WithdrawExecutorThrowsException_ShouldThrowException() {
        //Given
        Long senderAccountNumber = 100L;
        Long receiverAccountNumber = 200L;
        Double amount = 10000D;
        String message = "message";

        when(withdrawalExecutor.transact(senderAccountNumber, amount)).thenThrow(new ValidationException(message));

        //When and Then
        assertThrows(ValidationException.class,
                ()-> transactionService.transfer(senderAccountNumber, receiverAccountNumber, amount),
                message);
        verify(withdrawalExecutor, times(1)).transact(senderAccountNumber, amount);
        verifyNoInteractions(depositExecutor);
    }

    @Test
    void verifyTransfer_DepositExecutorThrowsException_ShouldThrowException() {
        //Given
        Long senderAccountNumber = 100L;
        Long receiverAccountNumber = 200L;
        Double amount = 10000D;
        String message = "message";
        Transaction withdrawTransaction = mock(Transaction.class);

        when(withdrawalExecutor.transact(senderAccountNumber, amount)).thenReturn(withdrawTransaction);
        when(depositExecutor.transact(receiverAccountNumber, amount)).thenThrow(new ValidationException(message));

        //When and Then
        assertThrows(ValidationException.class,
                ()-> transactionService.transfer(senderAccountNumber, receiverAccountNumber, amount),
                message);
        verify(withdrawalExecutor, times(1)).transact(senderAccountNumber, amount);
        verify(depositExecutor, times(1)).transact(receiverAccountNumber, amount);
    }

}
