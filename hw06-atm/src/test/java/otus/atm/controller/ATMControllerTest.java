package otus.atm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.atm.entity.CassetteInterface;
import otus.atm.enums.Transaction;
import otus.atm.wrapper.ErrorResponseWrapper;
import otus.atm.wrapper.SuccessResponseWrapper;

import java.io.IOException;
import java.util.List;

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
        List<CassetteInterface> availableCassettes = response.getAvailableCassettes();
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(10050, response.getTotalBalance());
        Assertions.assertEquals(
            10,
            availableCassettes.stream()
                .filter(cassette -> cassette.getDenomination() == Transaction.DEPOSIT_5.getDenomination())
                .findFirst()
                .get()
                .getCount()
        );
        Assertions.assertEquals(
            20,
            availableCassettes.stream()
                .filter(cassette -> cassette.getDenomination() == (Transaction.DEPOSIT_500.getDenomination()))
                .findFirst()
                .get()
                .getCount()
        );
    }

    @Test
    public void testDepositInvalidCount() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponseWrapper response = mapper.readValue(
                this.atm.executeTransaction(Transaction.DEPOSIT_100, -10),
                ErrorResponseWrapper.class
        );
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("Transaction or amount value are invalid", response.getMessage());
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
        List<CassetteInterface> availableCassettes = response.getAvailableCassettes();
        Assertions.assertEquals(3100, response.getTotalBalance());
        Assertions.assertEquals(
            5,
            availableCassettes.stream()
                .filter(cassette -> cassette.getDenomination() == Transaction.DEPOSIT_500.getDenomination())
                .findFirst()
                .get()
                .getCount()
        );
        Assertions.assertEquals(
            3,
            availableCassettes.stream()
                .filter(cassette -> cassette.getDenomination() == (Transaction.DEPOSIT_200.getDenomination()))
                .findFirst()
                .get()
                .getCount()
        );
        Assertions.assertTrue(
            availableCassettes.stream()
                .filter(cassette -> cassette.getDenomination() == Transaction.DEPOSIT_20.getDenomination())
                .findFirst()
                .isEmpty()
        );
        Assertions.assertTrue(
            availableCassettes.stream()
                .filter(cassette -> cassette.getDenomination() == Transaction.DEPOSIT_10.getDenomination())
                .findFirst()
                .isEmpty()
        );
        Assertions.assertTrue(
            availableCassettes.stream()
                .filter(cassette -> cassette.getDenomination() == Transaction.DEPOSIT_50.getDenomination())
                .findFirst()
                .isEmpty()
        );
    }

    @Test
    public void testWithdrawInsufficientFund() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponseWrapper response = mapper.readValue(
            this.atm.executeTransaction(Transaction.WITHDRAW, 20000),
            ErrorResponseWrapper.class
        );

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("Insufficient funds in the account", response.getMessage());
    }

    @Test
    public void testWithdrawMinimalDenominationException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponseWrapper response = mapper.readValue(
            this.atm.executeTransaction(Transaction.WITHDRAW, 50),
            ErrorResponseWrapper.class
        );

        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals(String.format("Minimal denomination is: %d", 500), response.getMessage());
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
