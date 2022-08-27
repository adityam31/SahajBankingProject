package com.bankingapp.services.transactions.executors;

import com.bankingapp.database.Database;
import com.bankingapp.models.Account;
import com.bankingapp.models.Transaction;
import com.bankingapp.utils.constants.Constants;
import com.bankingapp.utils.constants.Constants.Messages;
import com.bankingapp.utils.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepositExecutorTest {
    @Mock
    private Database database;

    @InjectMocks
    private DepositExecutor depositExecutor;

    @Test
    void verifyDepositTransact_NullInputs_ShouldThrowValidation() {
        //Given
        Long accountNumber = null;
        Double amount = null;

        //When and Then
        assertThrows(ValidationException.class,
                ()-> depositExecutor.transact(accountNumber, amount),
                Messages.INVALID_INPUT);
        verifyNoInteractions(database);
    }

    @Test
    void verifyDepositTransact_AccountDoesNotExist_ShouldThrowValidation() {
        //Given
        Long accountNumber = 100L;
        Double amount = 5000D;

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.empty());

        //When and Then
        assertValidationExceptionAndNoTransactionDone(accountNumber, amount, Messages.ACCOUNT_DOES_NOT_EXIST);
    }

    @ParameterizedTest
    @ValueSource(doubles = {100, 200, 300, 0, -1, 499.99, Double.MIN_VALUE})
    void verifyDepositTransact_AmountLessThanLowerLimit_ShouldThrowValidation(Double amount) {
        //Given
        Long accountNumber = 100L;
        Account account = mock(Account.class);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));

        //When and Then
        assertValidationExceptionAndNoTransactionDone(accountNumber, amount, Messages.MIN_DEPOSIT_AMOUNT_MSG);
    }

    @ParameterizedTest
    @ValueSource(doubles = {100000, 50000.1, 60000, 999999, Double.MAX_VALUE})
    void verifyDepositTransact_AmountMoreThanUpperLimit_ShouldThrowValidation(Double amount) {
        //Given
        Long accountNumber = 100L;
        Account account = mock(Account.class);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));

        //When and Then
        assertValidationExceptionAndNoTransactionDone(accountNumber, amount, Messages.MAX_DEPOSIT_AMOUNT_MSG);
    }

    @Test
    void verifyDepositTransact_NumberOfDepositsMoreThanAllowed_ShouldThrowValidation() {
        //Given
        Long accountNumber = 100L;
        Double amount = 50000D;
        Account account = mock(Account.class);
        List<Transaction> depositList = getMockedTransactionList(Constants.MAX_DEPOSITS_PER_DAY);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));
        when(database.getHolderDepositsForTheDay(accountNumber)).thenReturn(depositList);

        //When and Then
        assertValidationExceptionAndNoTransactionDone(accountNumber, amount, Messages.MAX_NO_DEPOSITS_MSG);
    }

    @Test
    void verifyDepositTransact_BalanceMoreThanUpperLimit_ShouldThrowValidation() {
        //Given
        Long accountNumber = 100L;
        Double amount = 50000D;
        Double balance = Constants.MAX_ACCOUNT_BALANCE;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(balance);
        List<Transaction> depositList = getMockedTransactionList(2);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));
        when(database.getHolderDepositsForTheDay(accountNumber)).thenReturn(depositList);

        //When and Then
        assertValidationExceptionAndNoTransactionDone(accountNumber, amount, Messages.MAX_ACCOUNT_BALANCE_MSG);
    }

    @Test
    void verifyDepositTransact_AllParametersValid_ShouldProcessTransaction() {
        //Given
        Long accountNumber = 100L;
        Double amount = 20000D;
        Double balance = 10000D;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(balance);
        List<Transaction> depositList = getMockedTransactionList(2);
        Transaction depositTransaction = mock(Transaction.class);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));
        when(database.getHolderDepositsForTheDay(accountNumber)).thenReturn(depositList);
        when(database.depositAmount(accountNumber, amount)).thenReturn(depositTransaction);

        //When
        Transaction actual = depositExecutor.transact(accountNumber, amount);

        //Then
        assertEquals(depositTransaction, actual);
        verify(database, times(1)).depositAmount(accountNumber, amount);
    }

    @Test
    void verifyDepositTransact_DatabaseRuntimeException_ShouldThrowException() {
        //Given
        Long accountNumber = 100L;
        Double amount = 20000D;
        Double balance = 10000D;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(balance);
        List<Transaction> depositList = getMockedTransactionList(2);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));
        when(database.getHolderDepositsForTheDay(accountNumber)).thenReturn(depositList);
        when(database.depositAmount(accountNumber, amount)).thenThrow(new RuntimeException());

        //When and Then
        assertThrows(RuntimeException.class,
                ()-> depositExecutor.transact(accountNumber, amount));
    }

    private void assertValidationExceptionAndNoTransactionDone(Long accountNumber, Double amount, String message) {
        assertThrows(ValidationException.class,
                ()-> depositExecutor.transact(accountNumber, amount),
                String.format(message, accountNumber));
        verify(database, times(0)).depositAmount(accountNumber, amount);
    }

    private List<Transaction> getMockedTransactionList(int size) {
        List<Transaction> mockTransactionList = mock(List.class);
        when(mockTransactionList.size()).thenReturn(size);
        return mockTransactionList;
    }
}