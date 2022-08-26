package com.bankingapp.utils.di;

import com.bankingapp.controllers.AccountController;
import com.bankingapp.controllers.TransactionController;
import com.bankingapp.database.Database;
import com.bankingapp.services.accounts.AccountService;
import com.bankingapp.services.accounts.AccountServiceImpl;
import com.bankingapp.services.transactions.TransactionService;
import com.bankingapp.services.transactions.TransactionServiceImpl;
import com.bankingapp.services.transactions.executors.DepositExecutor;
import com.bankingapp.services.transactions.executors.WithdrawalExecutor;
import com.google.inject.AbstractModule;

public class BindingModule extends AbstractModule {
    @Override
    protected void configure() {
        //Binding database
        bind(Database.class);

        //Binding services
        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(TransactionService.class).to(TransactionServiceImpl.class);
        bind(DepositExecutor.class);
        bind(WithdrawalExecutor.class);

        //Binding controllers
        bind(AccountController.class);
        bind(TransactionController.class);
    }
}
