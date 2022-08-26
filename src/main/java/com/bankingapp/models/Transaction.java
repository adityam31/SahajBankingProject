package com.bankingapp.models;

import com.bankingapp.models.enums.TransactionType;
import com.bankingapp.models.request.TransactionRequest;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
public class Transaction {

    private Long transactionId;
    private Long accountNumber;
    private Double amount;

    private Double resultantBalance;
    private TransactionType transactionType;
    private LocalDate transactionDate;

    private Transaction(Long accountNumber, Double amount, TransactionType transactionType, LocalDate transactionDate) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public static Transaction createTransaction(Long accountNumber, Double amount,
                                                TransactionType transactionType, LocalDate transactionDate) {
        return new Transaction(accountNumber, amount, transactionType, transactionDate);
    }

    public static Transaction fromTransactionRequest(TransactionRequest transactionRequest,
                                                     TransactionType transactionType) {
        return new Transaction(transactionRequest.getAccountOne(), transactionRequest.getAmount(),
                transactionType, LocalDate.now());
    }

}
