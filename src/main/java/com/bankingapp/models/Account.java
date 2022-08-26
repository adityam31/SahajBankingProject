package com.bankingapp.models;

import com.bankingapp.utils.constants.Constants;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Account {
    private Long accountNumber;
    private String holderName;
    private Double balance;

    public Account(String holderName) {
        this.holderName = holderName;
        this.balance = Constants.INITIAL_ACCOUNT_BALANCE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        if (!Objects.equals(accountNumber, account.accountNumber))
            return false;
        return holderName.equals(account.holderName);
    }

    @Override
    public int hashCode() {
        int result = accountNumber != null ? accountNumber.hashCode() : 0;
        result = 31 * result + holderName.hashCode();
        return result;
    }
}
