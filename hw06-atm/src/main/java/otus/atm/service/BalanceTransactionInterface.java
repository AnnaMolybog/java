package otus.atm.service;

import otus.atm.entity.BanknoteInterface;
import otus.atm.enums.Transaction;

import java.util.List;

public interface BalanceTransactionInterface {
    List<BanknoteInterface> process(
        List<BanknoteInterface> banknotes,
        Transaction transaction,
        int amount
    ) throws Exception;
    boolean support(Transaction transaction);
    boolean isInputValid(int amount);
}
