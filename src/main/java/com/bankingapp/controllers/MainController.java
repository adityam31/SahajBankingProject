package com.bankingapp.controllers;

import com.bankingapp.models.response.Response;
import com.bankingapp.models.response.Status;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static com.bankingapp.utils.constants.Constants.Commands;
import static com.bankingapp.utils.constants.Constants.Messages;

public class MainController {
    @Inject
    private AccountController accountController;

    @Inject
    private TransactionController transactionController;


    public List<String> perform(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        List<String> output = new ArrayList<>();
        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null) {
            Response response = routeToController(inputLine);

            if(response.getStatus() == Status.SUCCESSFUL)
                output.add(response.getOutput().toString());
            else
                output.add(response.getErrorMessage());
        }
        return output;
    }

    private Response routeToController(String fullCommand) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(fullCommand, "\t");
            String command = tokenizer.nextToken();

            switch (command) {
                case Commands.CREATE_ACCOUNT: {
                    return accountController.createAccount(tokenizer.nextToken());
                }
                case Commands.CHECK_BALANCE: {
                    return accountController.checkBalance(Long.parseLong(tokenizer.nextToken()));
                }
                case Commands.DEPOSIT: {
                    return transactionController.deposit(Long.parseLong(tokenizer.nextToken()),
                            Double.parseDouble(tokenizer.nextToken()));
                }
                case Commands.WITHDRAW: {
                    return transactionController.withdraw(Long.parseLong(tokenizer.nextToken()),
                            Double.parseDouble(tokenizer.nextToken()));
                }
                case Commands.TRANSFER: {
                    return transactionController.transfer(Long.parseLong(tokenizer.nextToken()),
                            Long.parseLong(tokenizer.nextToken()),
                            Double.parseDouble(tokenizer.nextToken()));
                }
                default:
                    throw new IllegalArgumentException(Messages.COMMAND_INVALID);
            }
        } catch(Exception e) {
            return Response.builder().status(Status.FAILED)
                    .errorMessage(e.getMessage()).build();
        }
    }
}
