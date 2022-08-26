package com.bankingapp.controllers;

import com.bankingapp.models.Transaction;
import com.bankingapp.models.response.Response;
import com.bankingapp.models.response.Status;
import com.bankingapp.services.transactions.TransactionService;
import com.bankingapp.utils.constants.Constants.*;

import javax.inject.Inject;

public class TransactionController {
    @Inject
    private TransactionService transactionService;


    public Response deposit(Long accountNumber, Double amount) {
        try {
            Transaction transaction = transactionService.deposit(accountNumber, amount);
            return Response.builder().status(Status.SUCCESSFUL)
                    .output(transaction.getResultantBalance()).build();
        } catch (Exception e) {
            return Response.builder().status(Status.FAILED)
                    .errorMessage(e.getMessage()).build();
        }
    }

    public Response withdraw(Long accountNumber, Double amount) {
        try {
            Transaction transaction = transactionService.withdraw(accountNumber, amount);
            return Response.builder().status(Status.SUCCESSFUL)
                    .output(transaction.getResultantBalance()).build();
        } catch (Exception e) {
            return Response.builder().status(Status.FAILED)
                    .errorMessage(e.getMessage()).build();
        }
    }

    public Response transfer(Long senderAccountNumber, Long receiverAccountNumber, Double amount) {
        try {
            transactionService.transfer(senderAccountNumber, receiverAccountNumber, amount);
            return Response.builder().status(Status.SUCCESSFUL)
                    .output(Messages.SUCCESSFUL).build();
        } catch (Exception e) {
            return Response.builder().status(Status.FAILED)
                    .errorMessage(e.getMessage()).build();
        }
    }
}
