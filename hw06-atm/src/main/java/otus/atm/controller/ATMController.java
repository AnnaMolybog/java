package otus.atm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import otus.atm.entity.Balance;
import otus.atm.enums.Transaction;
import otus.atm.service.TransactionProcessor;
import otus.atm.wrapper.ErrorResponseWrapper;
import otus.atm.wrapper.SuccessResponseWrapper;

import java.io.IOException;

public class ATMController {
    private TransactionProcessor transactionProcessor;

    public ATMController() {
        this.transactionProcessor = new TransactionProcessor();
    }

    public String executeTransaction(Transaction transaction) throws IOException {
        return this.executeTransaction(transaction, 0);
    }

    public String executeTransaction(Transaction transaction, int amount) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Balance balance = this.transactionProcessor.executeTransaction(transaction, amount);

            SuccessResponseWrapper response = new SuccessResponseWrapper();
            response.setSuccess(true);
            response.setTotalBalance(balance.getTotalSum());
            response.setAvailableBanknotes(balance.getBanknotes());

            return mapper.writeValueAsString(response);
        } catch (Exception exception) {
            ErrorResponseWrapper response = new ErrorResponseWrapper();
            response.setSuccess(false);
            response.setMessage(exception.getMessage());

            return mapper.writeValueAsString(response);
        }
    }
}
