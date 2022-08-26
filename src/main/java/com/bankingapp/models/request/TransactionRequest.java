package com.bankingapp.models.request;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public class TransactionRequest {
    @NonNull
    private Long accountOne;
    private Long accountTwo;
    @NonNull
    private Double amount;

    private TransactionRequest(Long accountOne, Double amount) {
        this.accountOne = accountOne;
        this.accountTwo = null;
        this.amount = amount;
    }

    private TransactionRequest (Long accountOne, Long accountTwo, Double amount) {
        this.accountOne = accountOne;
        this.accountTwo = accountTwo;
        this.amount = amount;
    }

    public TransactionRequest newSingleAccountTransactionRequest(Long accountOne, Double amount) {
        return new TransactionRequest(accountOne, amount);
    }

    public TransactionRequest newTransferRequest(Long accountOne, Long accountTwo, Double amount) {
        return new TransactionRequest(accountOne, accountTwo, amount);
    }

    public boolean isValidSingleAccountTransactionRequest() {
        return Objects.nonNull(accountOne) && Objects.nonNull(amount)
                && Objects.isNull(accountTwo);
    }

    public boolean isValidTransferRequest() {
        return Objects.nonNull(accountOne) && Objects.nonNull(amount)
                && Objects.nonNull(accountTwo);
    }
}
