package otus.atm.service;

import otus.atm.entity.CassetteInterface;
import otus.atm.enums.Transaction;

import java.util.List;

public interface BalanceTransactionInterface {
    List<CassetteInterface> process(
        List<CassetteInterface> cassettes,
        Transaction transaction,
        int amount
    ) throws Exception;
    boolean support(Transaction transaction);
    boolean isInputValid(int amount);
}
