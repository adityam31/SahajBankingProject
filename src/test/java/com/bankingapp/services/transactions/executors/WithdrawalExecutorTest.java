package com.bankingapp.services.transactions.executors;

import com.bankingapp.database.Database;
import com.bankingapp.models.Account;
import com.bankingapp.models.Transaction;
import com.bankingapp.utils.constants.Constants;
import com.bankingapp.utils.constants.Constants.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WithdrawalExecutorTest {
    @Mock
    private Database database;

    @InjectMocks
    private WithdrawalExecutor withdrawalExecutor;

    @Test
    void verifyWithdrawTransact_NullInputs_ShouldThrowValidation() {
        //Given
        Long accountNumber = null;
        Double amount = null;

        //When and Then
        assertThrows(ValidationException.class,
                ()-> withdrawalExecutor.transact(accountNumber, amount),
                Messages.INVALID_INPUT);
        verifyNoInteractions(database);
    }

    @Test
    void verifyWithdrawTransact_AccountDoesNotExist_ShouldThrowValidation() {
        //Given
        Long accountNumber = 100L;
        Double amount = 5000D;

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.empty());

        //When and Then
        assertValidationExceptionAndNoTransactionDone(accountNumber, amount, Messages.ACCOUNT_DOES_NOT_EXIST);
    }

    @ParameterizedTest
    @ValueSource(doubles = {100, 200, 300, 0, -1, 999.99, Double.MIN_VALUE})
    void verifyWithdrawTransact_AmountLessThanLowerLimit_ShouldThrowValidation(Double amount) {
        //Given
        Long accountNumber = 100L;
        Account account = mock(Account.class);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));

        //When and Then
        assertValidationExceptionAndNoTransactionDone(accountNumber, amount, Messages.MIN_WITHDRAWAL_AMOUNT_MSG);
    }

    @ParameterizedTest
    @ValueSource(doubles = {100000, 25000.1, 60000, 999999, Double.MAX_VALUE})
    void verifyWithdrawTransact_AmountMoreThanUpperLimit_ShouldThrowValidation(Double amount) {
        //Given
        Long accountNumber = 100L;
        Account account = mock(Account.class);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));

        //When and Then
        assertValidationExceptionAndNoTransactionDone(accountNumber, amount, Messages.MAX_WITHDRAWAL_AMOUNT_MSG);
    }

    @Test
    void verifyWithdrawTransact_NumberOfWithdrawalsMoreThanAllowed_ShouldThrowValidation() {
        //Given
        Long accountNumber = 100L;
        Double amount = 5000D;
        Account account = mock(Account.class);
        List<Transaction> withdrawalList = getMockedTransactionList(Constants.MAX_WITHDRAWALS_PER_DAY);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));
        when(database.getHolderWithdrawalsForTheDay(accountNumber)).thenReturn(withdrawalList);

        //When and Then
        assertValidationExceptionAndNoTransactionDone(accountNumber, amount, Messages.MAX_NO_WITHDRAWALS_MSG);
    }

    @Test
    void verifyWithdrawTransact_BalanceLessThanLowerLimit_ShouldThrowValidation() {
        //Given
        Long accountNumber = 100L;
        Double amount = 5000D;
        Double balance = Constants.MIN_ACCOUNT_BALANCE;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(balance);
        List<Transaction> withdrawalList = getMockedTransactionList(2);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));
        when(database.getHolderWithdrawalsForTheDay(accountNumber)).thenReturn(withdrawalList);

        //When and Then
        assertValidationExceptionAndNoTransactionDone(accountNumber, amount, Messages.INSUFFICIENT_FUNDS);
    }

    @Test
    void verifyWithdrawTransact_AllParametersValid_ShouldProcessTransaction() {
        //Given
        Long accountNumber = 100L;
        Double amount = 2000D;
        Double balance = 10000D;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(balance);
        List<Transaction> withdrawalList = getMockedTransactionList(2);
        Transaction withdrawalTransaction = mock(Transaction.class);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));
        when(database.getHolderWithdrawalsForTheDay(accountNumber)).thenReturn(withdrawalList);
        when(database.withdrawAmount(accountNumber, amount)).thenReturn(withdrawalTransaction);

        //When
        Transaction actual = withdrawalExecutor.transact(accountNumber, amount);

        //Then
        assertEquals(withdrawalTransaction, actual);
        verify(database, times(1)).withdrawAmount(accountNumber, amount);
    }

    @Test
    void verifyWithdrawTransact_DatabaseRuntimeException_ShouldThrowException() {
        //Given
        Long accountNumber = 100L;
        Double amount = 2000D;
        Double balance = 10000D;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(balance);
        List<Transaction> withdrawalList = getMockedTransactionList(2);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));
        when(database.getHolderWithdrawalsForTheDay(accountNumber)).thenReturn(withdrawalList);
        when(database.withdrawAmount(accountNumber, amount)).thenThrow(new RuntimeException());

        //When and Then
        assertThrows(RuntimeException.class,
                ()-> withdrawalExecutor.transact(accountNumber, amount));
    }

    private void assertValidationExceptionAndNoTransactionDone(Long accountNumber, Double amount, String message) {
        assertThrows(ValidationException.class,
                ()-> withdrawalExecutor.transact(accountNumber, amount),
                String.format(message, accountNumber));
        verify(database, times(0)).withdrawAmount(accountNumber, amount);
    }

    private List<Transaction> getMockedTransactionList(int size) {
        List<Transaction> mockTransactionList = mock(List.class);
        when(mockTransactionList.size()).thenReturn(size);
        return mockTransactionList;
    }

}