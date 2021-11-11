package otus.atm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.atm.enums.Denomination;
import otus.atm.enums.Transaction;
import otus.atm.exception.InsufficientFundsException;
import otus.atm.exception.InvalidInputException;
import otus.atm.exception.MinimalDenominationException;
import otus.atm.wrapper.ErrorResponseWrapper;
import otus.atm.wrapper.SuccessResponseWrapper;

import java.io.IOException;
import java.util.Map;

public class ATMControllerTest {
    private ATMController atm;

    @BeforeEach
    public void setUp() throws Exception {
        this.atm = new ATMController();
        this.atm.executeTransaction(Transaction.DEPOSIT_500, 20);
    }

    @Test
    public void testDepositSuccess() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SuccessResponseWrapper response = mapper.readValue(
            this.atm.executeTransaction(Transaction.DEPOSIT_5, 10),
            SuccessResponseWrapper.class
        );
        Map<Denomination, Integer> availableBanknotes = response.getAvailableBanknotes();
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(10050, response.getTotalBalance());
        Assertions.assertTrue(availableBanknotes.containsKey(Denomination.BANKNOTE_5));
        Assertions.assertTrue(availableBanknotes.containsKey(Denomination.BANKNOTE_500));
        Assertions.assertEquals(10, availableBanknotes.get(Denomination.BANKNOTE_5));
    }

    @Test
    public void testDepositInvalidCount() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponseWrapper response = mapper.readValue(
                this.atm.executeTransaction(Transaction.DEPOSIT_100, -10),
                ErrorResponseWrapper.class
        );
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(InvalidInputException.errorMessage, response.getMessage());
    }

    @Test
    public void testWithdrawSuccess() throws Exception {
        this.atm.executeTransaction(Transaction.DEPOSIT_200, 4);
        this.atm.executeTransaction(Transaction.DEPOSIT_50, 2);
        this.atm.executeTransaction(Transaction.DEPOSIT_20, 1);
        this.atm.executeTransaction(Transaction.DEPOSIT_10, 3);
        ObjectMapper mapper = new ObjectMapper();
        SuccessResponseWrapper response = mapper.readValue(
            this.atm.executeTransaction(Transaction.WITHDRAW, 7850),
            SuccessResponseWrapper.class
        );

        Assertions.assertTrue(response.isSuccess());
        Map<Denomination, Integer> availableBanknotes = response.getAvailableBanknotes();
        Assertions.assertEquals(3100, response.getTotalBalance());
        Assertions.assertEquals(5, availableBanknotes.get(Denomination.BANKNOTE_500));
        Assertions.assertEquals(3, availableBanknotes.get(Denomination.BANKNOTE_200));
        Assertions.assertFalse(availableBanknotes.containsKey(Denomination.BANKNOTE_20));
        Assertions.assertFalse(availableBanknotes.containsKey(Denomination.BANKNOTE_10));
        Assertions.assertFalse(availableBanknotes.containsKey(Denomination.BANKNOTE_50));
    }

    @Test
    public void testWithdrawInsufficientFund() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponseWrapper response = mapper.readValue(
            this.atm.executeTransaction(Transaction.WITHDRAW, 20000),
            ErrorResponseWrapper.class
        );

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(InsufficientFundsException.errorMessage, response.getMessage());
    }

    @Test
    public void testWithdrawMinimalDenominationException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponseWrapper response = mapper.readValue(
            this.atm.executeTransaction(Transaction.WITHDRAW, 50),
            ErrorResponseWrapper.class
        );

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(String.format(MinimalDenominationException.errorMessage, 500), response.getMessage());
    }

    @Test
    public void testCheckBalance() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SuccessResponseWrapper response = mapper.readValue(
            this.atm.executeTransaction(Transaction.CHECK_BALANCE),
            SuccessResponseWrapper.class
        );

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(10000, response.getTotalBalance());
    }
}
