package com.bankingapp.services.accounts;

import com.bankingapp.database.Database;
import com.bankingapp.models.Account;
import com.bankingapp.utils.constants.Constants;
import com.bankingapp.utils.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    private Database database;

    @InjectMocks
    private AccountServiceImpl accountService;

    @ParameterizedTest
    @ValueSource(strings = {"Aditya", "Sahaj"})
    void verifyCreateAccount_ValidHolderName_ShouldCreateAccount(String input) {
        //Given
        Account account = new Account(input);
        when(database.createAccount(input)).thenReturn(account);

        //When
        Account actual = accountService.createAccount(input);

        //Then
        assertEquals(account, actual);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void verifyCreateAccount_InValidHolderName_ShouldCreateAccount(String input) {
        //When and Then
        assertThrows(ValidationException.class,
                () -> accountService.createAccount(input),
                Constants.Messages.INVALID_INPUT);
    }

    @Test
    void verifyCheckBalance_ValidAccountNumber_ShouldReturnBalance() {
        //Given
        Long accountNumber = 101L;
        Double balance = 1000D;
        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(balance);

        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.of(account));

        //When
        Double actual = accountService.checkBalance(accountNumber);

        //Then
        assertEquals(balance, actual);
    }

    @Test
    void verifyCheckBalance_NullInput_ShouldThrowValidation() {
        //When and Then
        assertThrows(ValidationException.class,
                () -> accountService.checkBalance(null),
                Constants.Messages.INVALID_INPUT);
    }

    @Test
    void verifyCheckBalance_AccountDoesNotExist_ShouldThrowValidation() {
        //Given
        Long accountNumber = 101L;
        when(database.getAccountByNumber(accountNumber)).thenReturn(Optional.empty());

        //When and Then
        assertThrows(ValidationException.class,
                () -> accountService.checkBalance(accountNumber),
                Constants.Messages.ACCOUNT_DOES_NOT_EXIST);
    }
}
