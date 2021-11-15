package otus.atm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import otus.atm.entity.BanknoteInterface;
import otus.atm.enums.Transaction;
import otus.atm.service.BalanceService;
import otus.atm.service.BalanceServiceInterface;
import otus.atm.service.TransactionProcessor;
import otus.atm.wrapper.ErrorResponseWrapper;
import otus.atm.wrapper.SuccessResponseWrapper;

import java.io.IOException;
import java.util.List;

public class ATMController {
    private TransactionProcessor transactionProcessor;
    private BalanceServiceInterface balanceService;

    public ATMController() {
        this.transactionProcessor = new TransactionProcessor();
        this.balanceService = new BalanceService();
    }

    public String executeTransaction(Transaction transaction) throws IOException {
        return this.executeTransaction(transaction, 0);
    }

    public String executeTransaction(Transaction transaction, int amount) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<BanknoteInterface> banknotes = this.transactionProcessor.executeTransaction(transaction, amount);

            SuccessResponseWrapper response = new SuccessResponseWrapper();
            response.setSuccess(true);
            response.setTotalBalance(this.balanceService.getTotalSum(banknotes));
            response.setAvailableBanknotes(banknotes);

            return mapper.writeValueAsString(response);
        } catch (Exception exception) {
            ErrorResponseWrapper response = new ErrorResponseWrapper();
            response.setSuccess(false);
            response.setMessage(exception.getMessage());

            return mapper.writeValueAsString(response);
        }
    }
}
