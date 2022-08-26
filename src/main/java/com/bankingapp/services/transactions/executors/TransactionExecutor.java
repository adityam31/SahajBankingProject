package com.bankingapp.services.transactions.executors;

import com.bankingapp.models.Transaction;

public abstract class TransactionExecutor {
    protected abstract void validateRequest(Long accountNumber, Double amount);
    protected abstract Transaction performOperation(Long accountNumber, Double amount);

    public Transaction transact(Long accountNumber, Double amount) {
        validateRequest(accountNumber, amount);
        return performOperation(accountNumber, amount);
    }
}
