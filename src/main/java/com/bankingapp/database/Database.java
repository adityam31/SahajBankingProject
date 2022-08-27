package com.bankingapp.database;

import com.bankingapp.models.Account;
import com.bankingapp.models.Transaction;
import com.bankingapp.models.enums.TransactionType;
import com.bankingapp.utils.constants.Constants;
import com.bankingapp.utils.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.bankingapp.utils.constants.Constants.MAX_ACCOUNT_BALANCE;
import static com.bankingapp.utils.constants.Constants.MIN_ACCOUNT_BALANCE;

/**
 * This class is meant to simulate a database.
 */
public class Database {
    private static Long accountNumberSequence = 1001L;
    private static Long transactionIdSequence = 9000L;

    private static final Map<Long, Account> accounts = new ConcurrentHashMap<>();
    private static final Map<Long, Transaction> transactions = new ConcurrentHashMap<>();

    public Optional<Account> getAccountByNumber(Long accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    public Account createAccount(String holderName) {
        Account account = new Account(holderName);

        synchronized (accountNumberSequence) {
            account.setAccountNumber(accountNumberSequence);
            accounts.put(accountNumberSequence++, account);
        }

        return account;
    }

    public Transaction depositAmount(Long accountNumber, Double amount) {
        Account account = accounts.get(accountNumber);
        Transaction transaction = Transaction.createTransaction(accountNumber, amount,
                TransactionType.DEPOSIT, LocalDate.now());

        synchronized (account) {
            Double resultantBalance = account.getBalance() + amount;
            updateBalance(account, resultantBalance);
            transaction.setResultantBalance(resultantBalance);
        }

        synchronized (transactionIdSequence) {
            transaction.setTransactionId(transactionIdSequence);
            transactions.put(transactionIdSequence++, transaction);
        }

        return transaction;
    }

    public Transaction withdrawAmount(Long accountNumber, Double amount) {
        Account account = accounts.get(accountNumber);
        Transaction transaction = Transaction.createTransaction(accountNumber, amount,
                TransactionType.WITHDRAWAL, LocalDate.now());


        synchronized (account) {
            Double resultantBalance = account.getBalance() - amount;
            updateBalance(account, resultantBalance);
            transaction.setResultantBalance(resultantBalance);
        }

        synchronized (transactionIdSequence) {
            transaction.setTransactionId(transactionIdSequence);
            transactions.put(transactionIdSequence++, transaction);
        }

        return transaction;
    }

    public List<Transaction> getHolderDepositsForTheDay(Long accountNumber) {
        LocalDate currentDate = LocalDate.now();
        return transactions.values().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getTransactionType() == TransactionType.DEPOSIT)
                .filter(transaction -> transaction.getTransactionDate().equals(currentDate))
                .collect(Collectors.toList());
    }

    public List<Transaction> getHolderWithdrawalsForTheDay(Long accountNumber) {
        LocalDate currentDate = LocalDate.now();
        return transactions.values().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getTransactionType() == TransactionType.WITHDRAWAL)
                .filter(transaction -> transaction.getTransactionDate().equals(currentDate))
                .collect(Collectors.toList());
    }

    private void updateBalance(Account account, Double resultantBalance) {
        if(resultantBalance < MIN_ACCOUNT_BALANCE)
            throw new ValidationException(Constants.Messages.INSUFFICIENT_FUNDS, account.getAccountNumber());

        if(resultantBalance > MAX_ACCOUNT_BALANCE)
            throw new ValidationException(Constants.Messages.MAX_ACCOUNT_BALANCE_MSG, account.getAccountNumber());

        account.setBalance(resultantBalance);
        accounts.put(account.getAccountNumber(), account);
    }
}
