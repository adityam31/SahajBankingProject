package com.bankingapp.models;

import com.bankingapp.models.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Objects;


@Getter
@Setter
public class Transaction implements Comparable<Transaction> {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;

        Transaction that = (Transaction) o;

        if (!Objects.equals(transactionId, that.transactionId))
            return false;
        if (!accountNumber.equals(that.accountNumber)) return false;
        return transactionType == that.transactionType;
    }

    @Override
    public int hashCode() {
        int result = transactionId != null ? transactionId.hashCode() : 0;
        result = 31 * result + accountNumber.hashCode();
        result = 31 * result + transactionType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", accountNumber=" + accountNumber +
                ", amount=" + amount +
                ", resultantBalance=" + resultantBalance +
                ", transactionType=" + transactionType +
                ", transactionDate=" + transactionDate +
                '}';
    }

    @Override
    public int compareTo(Transaction o) {
        return Long.compare(transactionId, o.transactionId);
    }
}
