package com.bankingapp.services.accounts;

import com.bankingapp.models.Account;

public interface AccountService {
    Account createAccount(String holderName);

    Double checkBalance(Long accountNumber);
}
