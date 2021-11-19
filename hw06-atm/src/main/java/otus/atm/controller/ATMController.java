package otus.atm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import otus.atm.entity.CassetteInterface;
import otus.atm.enums.Transaction;
import otus.atm.service.BalanceService;
import otus.atm.service.BalanceServiceInterface;
import otus.atm.service.TransactionService;
import otus.atm.wrapper.ErrorResponseWrapper;
import otus.atm.wrapper.SuccessResponseWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ATMController {
    private final TransactionService transactionProcessor;
    private final BalanceServiceInterface balanceService;

    public ATMController() {
        this.transactionProcessor = new TransactionService(new ArrayList<>());
        this.balanceService = new BalanceService();
    }

    public String executeTransaction(Transaction transaction) throws IOException {
        return this.executeTransaction(transaction, 0);
    }

    public String executeTransaction(Transaction transaction, int amount) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<CassetteInterface> cassettes = this.transactionProcessor.executeTransaction(transaction, amount);

            SuccessResponseWrapper response = new SuccessResponseWrapper();
            response.setSuccess(true);
            response.setTotalBalance(this.balanceService.getTotalSum(cassettes));
            response.setAvailableCassettes(cassettes);

            return mapper.writeValueAsString(response);
        } catch (Exception exception) {
            ErrorResponseWrapper response = new ErrorResponseWrapper();
            response.setSuccess(false);
            response.setMessage(exception.getMessage());

            return mapper.writeValueAsString(response);
        }
    }
}
