package com.bankingapp.services.accounts;

import com.bankingapp.database.Database;
import com.bankingapp.models.Account;
import com.bankingapp.utils.constants.Constants.*;
import com.bankingapp.utils.exceptions.ValidationException;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {
    @Inject
    private Database database;

    @Override
    public Account createAccount(String holderName) {
        if(Objects.isNull(holderName) || holderName.isBlank())
            throw new ValidationException(Messages.INVALID_INPUT);

        return database.createAccount(holderName);
    }

    @Override
    public Double checkBalance(Long accountNumber) {
        if(Objects.isNull(accountNumber))
            throw new ValidationException(Messages.INVALID_INPUT);

        Optional<Account> accountOptional = database.getAccountByNumber(accountNumber);

        if(accountOptional.isPresent())
            return accountOptional.get().getBalance();

        throw new ValidationException(Messages.ACCOUNT_DOES_NOT_EXIST, accountNumber);
    }
}
