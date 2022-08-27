package com.bankingapp.services.transactions;

import com.bankingapp.models.Transaction;

public interface TransactionService {
    Transaction deposit(Long accountNumber, Double amount);
    Transaction withdraw(Long accountNumber, Double amount);
    void transfer(Long senderAccountNumber, Long receiverAccountNumber, Double amount);
}
