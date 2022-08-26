package com.bankingapp.services.transactions;

import com.bankingapp.models.Transaction;
import com.bankingapp.services.transactions.executors.DepositExecutor;
import com.bankingapp.services.transactions.executors.TransactionExecutor;
import com.bankingapp.services.transactions.executors.WithdrawalExecutor;
import com.google.inject.Inject;

public class TransactionServiceImpl implements TransactionService {
    @Inject
    private DepositExecutor depositExecutor;

    @Inject
    private WithdrawalExecutor withdrawalExecutor;

    @Override
    public Transaction deposit(Long accountNumber, Double amount) {
        return doAction(accountNumber, amount, depositExecutor);
    }

    public Transaction withdraw(Long accountNumber, Double amount) {
        return doAction(accountNumber, amount, withdrawalExecutor);
    }

    public void transfer(Long senderAccountNumber, Long receiverAccountNumber, Double amount) {
        doAction(senderAccountNumber, amount, withdrawalExecutor);
        doAction(receiverAccountNumber, amount, depositExecutor);
    }

    private Transaction doAction(Long accountNumber, Double amount, TransactionExecutor executor) {
        return executor.transact(accountNumber, amount);
    }
}
