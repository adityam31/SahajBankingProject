package com.bankingapp.services.transactions.executors;

import com.bankingapp.database.Database;
import com.bankingapp.models.Account;
import com.bankingapp.models.Transaction;
import com.bankingapp.utils.exceptions.ValidationException;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

import static com.bankingapp.utils.constants.Constants.*;

public class WithdrawalExecutor extends TransactionExecutor {
    @Inject
    private Database database;

    @Override
    protected void validateRequest(Long accountNumber, Double amount) {
        if(Objects.isNull(accountNumber) || Objects.isNull(amount))
            throw new ValidationException(Messages.TRANSACTION_REQUEST_INVALID);

        Optional<Account> accountOptional = database.getAccountByNumber(accountNumber);

        if(!accountOptional.isPresent())
            throw new ValidationException(Messages.ACCOUNT_DOES_NOT_EXIST, accountNumber);
        if(amount < MIN_WITHDRAW_AMOUNT)
            throw new ValidationException(Messages.MIN_WITHDRAWAL_AMOUNT_MSG, accountNumber);
        if(amount > MAX_WITHDRAW_AMOUNT)
            throw new ValidationException(Messages.MAX_WITHDRAWAL_AMOUNT_MSG, accountNumber);
        if(database.getHolderWithdrawalsForTheDay(accountNumber).size() >= MAX_WITHDRAWALS_PER_DAY)
            throw new ValidationException(Messages.MAX_NO_WITHDRAWALS_MSG, accountNumber);

        Double totalBalance = accountOptional.get().getBalance() - amount;

        if(totalBalance < MIN_ACCOUNT_BALANCE)
            throw new ValidationException(Messages.INSUFFICIENT_FUNDS, accountNumber);
    }

    @Override
    protected Transaction performOperation(Long accountNumber, Double amount) {
        return database.withdrawAmount(accountNumber, amount);
    }
}
