package com.bankingapp.controllers;

import com.bankingapp.models.Account;
import com.bankingapp.models.response.Response;
import com.bankingapp.models.response.Status;
import com.bankingapp.services.accounts.AccountService;
import javax.inject.Inject;

public class AccountController {
    @Inject
    private AccountService accountService;

    public Response createAccount(String holderName) {
        try {
            Account account = accountService.createAccount(holderName);

            return Response.builder().status(Status.SUCCESSFUL)
                    .output(account.getAccountNumber()).build();
        } catch (Exception e) {
            return Response.builder().status(Status.FAILED)
                    .errorMessage(e.getMessage()).build();
        }
    }

    public Response checkBalance(Long accountNumber) {
        try {
            Double balance = accountService.checkBalance(accountNumber);

            return Response.builder().status(Status.SUCCESSFUL)
                    .output(balance).build();
        } catch (Exception e) {
            return Response.builder().status(Status.FAILED)
                    .errorMessage(e.getMessage()).build();
        }
    }
}
