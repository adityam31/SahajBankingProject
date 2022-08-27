package com.bankingapp.database;

import com.bankingapp.models.Account;
import com.bankingapp.models.Transaction;
import com.bankingapp.models.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.bankingapp.utils.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseTest {
    @InjectMocks
    private Database database;


    @Test
    void verifyCreateAccount_ShouldCreateNewAccount() {
        //Given
        String holderName = "Adam";

        //When
        Account account = database.createAccount(holderName);

        //Then
        assertNotNull(account);
        assertNotNull(account.getAccountNumber());
        assertEquals(holderName, account.getHolderName());
        assertEquals(INITIAL_ACCOUNT_BALANCE, account.getBalance());
    }

    @Test
    void verifyDepositAccount_ShouldUpdateBalanceAndReturnTransaction() {
        //Given
        String holderName = "Adam";
        Double amount = 5000D;
        LocalDate transactionDate = LocalDate.now();
        Account account = database.createAccount(holderName);

        //When
        Transaction transaction = database.depositAmount(account.getAccountNumber(), amount);

        //Then
        assertNotNull(transaction);
        assertNotNull(transaction.getTransactionId());
        assertEquals(account.getAccountNumber(), transaction.getAccountNumber());
        assertEquals(amount, transaction.getResultantBalance());
        assertEquals(TransactionType.DEPOSIT, transaction.getTransactionType());
        assertEquals(transactionDate, transaction.getTransactionDate());
    }

    @Test
    void verifyWithdrawAccount_ShouldUpdateBalanceAndReturnTransaction() {
        //Given
        String holderName = "Adam";
        Double depositAmount = 5000D;
        Double withdrawalAmount = 1000D;
        Double resultantBalance = depositAmount - withdrawalAmount;
        LocalDate transactionDate = LocalDate.now();
        Account account = database.createAccount(holderName);
        database.depositAmount(account.getAccountNumber(), depositAmount);

        //When
        Transaction transaction = database.withdrawAmount(account.getAccountNumber(), withdrawalAmount);

        //Then
        assertNotNull(transaction);
        assertNotNull(transaction.getTransactionId());
        assertEquals(account.getAccountNumber(), transaction.getAccountNumber());
        assertEquals(resultantBalance, transaction.getResultantBalance());
        assertEquals(TransactionType.WITHDRAWAL, transaction.getTransactionType());
        assertEquals(transactionDate, transaction.getTransactionDate());
    }

    @Test
    void verifyGetHolderDepositsForTheDay_ShouldReturnCurrentDayDeposits() {
        //Given
        String holderName = "Adam";
        int eachTransactionCount = 5;

        Account account = database.createAccount(holderName);
        List<Transaction> transactionList = performTransactions(account.getAccountNumber(), eachTransactionCount);
        List<Transaction> expectedList = transactionList.stream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.DEPOSIT)
                .collect(Collectors.toList());

        //When
        List<Transaction> actualList = database.getHolderDepositsForTheDay(account.getAccountNumber());
        Collections.sort(actualList);

        //Then
        assertNotNull(actualList);
        assertEquals(eachTransactionCount, actualList.size());
        assertEquals(expectedList, actualList);
    }

    @Test
    void verifyGetHolderWithdrawalsForTheDay_ShouldReturnCurrentDayWithdrawals() {
        //Given
        String holderName = "Adam";
        int eachTransactionCount = 5;

        Account account = database.createAccount(holderName);
        List<Transaction> transactionList = performTransactions(account.getAccountNumber(), eachTransactionCount);
        List<Transaction> expectedList = transactionList.stream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.WITHDRAWAL)
                .collect(Collectors.toList());

        //When
        List<Transaction> actualList = database.getHolderWithdrawalsForTheDay(account.getAccountNumber());
        Collections.sort(actualList);

        //Then
        assertNotNull(actualList);
        assertEquals(eachTransactionCount, actualList.size());
        assertEquals(expectedList, actualList);
    }


    private List<Transaction> performTransactions(Long accountNumber, int eachTransactionCount) {
        List<Transaction> transactionList = new ArrayList<>();
        Double depositAmount = 5000D;
        Double withdrawalAmount = 1000D;

        for(int idx = 0; idx < eachTransactionCount; idx++) {
            transactionList.add(database.depositAmount(accountNumber, depositAmount));
            transactionList.add(database.withdrawAmount(accountNumber, withdrawalAmount));
        }

        Collections.sort(transactionList);

        return transactionList;
    }


}