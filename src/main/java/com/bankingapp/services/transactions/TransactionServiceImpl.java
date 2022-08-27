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
        return doAction(depositExecutor, accountNumber, amount);
    }

    public Transaction withdraw(Long accountNumber, Double amount) {
        return doAction(withdrawalExecutor, accountNumber, amount);
    }

    public void transfer(Long senderAccountNumber, Long receiverAccountNumber, Double amount) {
        doAction(withdrawalExecutor, senderAccountNumber, amount);
        doAction(depositExecutor, receiverAccountNumber, amount);
    }

    private Transaction doAction(TransactionExecutor executor, Long accountNumber, Double amount) {
        return executor.transact(accountNumber, amount);
    }
}
