package com.bankingapp.utils.constants;

public class Constants {
    public static Double INITIAL_ACCOUNT_BALANCE = 0.0D;

    public static final Double MIN_DEPOSIT_AMOUNT = 500.0D;

    public static final Double MAX_DEPOSIT_AMOUNT = 50000.0D;

    public static final Integer MAX_DEPOSITS_PER_DAY = 3;

    public static final Double MIN_WITHDRAW_AMOUNT = 1000.0D;

    public static final Double MAX_WITHDRAW_AMOUNT = 25000.0D;

    public static final Integer MAX_WITHDRAWALS_PER_DAY = 3;

    public static final Double MAX_ACCOUNT_BALANCE = 100000.0D;

    public static final Double MIN_ACCOUNT_BALANCE = 0.0D;

    public static class Commands {
        public static final String CREATE_ACCOUNT = "CREATE";

        public static final String CHECK_BALANCE = "BALANCE";

        public static final String DEPOSIT = "DEPOSIT";

        public static final String WITHDRAW = "WITHDRAW";

        public static final String TRANSFER = "TRANSFER";
    }

    public static class Messages {
        public static String SUCCESSFUL = "Successful";

        public static String COMMAND_INVALID = "Command invalid or cannot be parsed";

        public static String INVALID_INPUT = "Please provide valid input";

        public static String TRANSACTION_REQUEST_INVALID = "Transaction Request sent is invalid";

        public static String ACCOUNT_DOES_NOT_EXIST = "Account %1$s does not exist";

        public static final String MIN_DEPOSIT_AMOUNT_MSG = "Minimum deposit amount is " + MIN_DEPOSIT_AMOUNT + " for " +
                "account %1$s";

        public static final String MAX_DEPOSIT_AMOUNT_MSG = "Maximum deposit amount is " + MAX_DEPOSIT_AMOUNT + " for " +
                "account %1$s";

        public static final String MAX_NO_DEPOSITS_MSG = "Only " + MAX_DEPOSITS_PER_DAY + " deposits are allowed in a " +
                "day for account %1$s";


        public static final String MIN_WITHDRAWAL_AMOUNT_MSG = "Minimum withdrawal amount is " + MIN_WITHDRAW_AMOUNT +
                " for account %1$s";

        public static final String MAX_WITHDRAWAL_AMOUNT_MSG = "Maximum withdrawal amount is " + MAX_WITHDRAW_AMOUNT +
                " for account %1$s";

        public static final String MAX_NO_WITHDRAWALS_MSG = "Only " + MAX_WITHDRAWALS_PER_DAY + " withdrawals are allowed " +
                "in a day for account %1$s";

        public static final String MAX_ACCOUNT_BALANCE_MSG = "This transaction will make the account balance " +
                "exceed the maximum limit of " + MAX_ACCOUNT_BALANCE + " for account %1$s";

        public static final String INSUFFICIENT_FUNDS = "Insufficient funds to perform the transaction for account %1$s";
    }
}
