package otus.atm.service;

import otus.atm.enums.Transaction;

public interface BalanceTransactionInterface {
    void process(
        Transaction transaction,
        int amount
    ) throws Exception;
    boolean support(Transaction transaction);
    boolean isInputValid(int amount);
}
