package otus.atm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import otus.atm.entity.CassetteInterface;
import otus.atm.enums.Transaction;
import otus.atm.service.*;
import otus.atm.wrapper.ErrorResponseWrapper;
import otus.atm.wrapper.SuccessResponseWrapper;

import java.io.IOException;
import java.util.List;

public class ATMController {
    private final ATMService atmService;
    private final BalanceServiceInterface balanceService;
    private final CassettesStorageInterface cassettesStorage;

    public ATMController() {
        this.balanceService = new BalanceService();
        this.cassettesStorage = new CassettesStorage();
        this.atmService = new ATMService(this.cassettesStorage,  this.balanceService);
    }

    public String executeTransaction(Transaction transaction) throws IOException {
        return this.executeTransaction(transaction, 0);
    }

    public String executeTransaction(Transaction transaction, int amount) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.atmService.executeTransaction(transaction, amount);

            List<CassetteInterface> cassettes = this.cassettesStorage.getCassettes();
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
