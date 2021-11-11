package otus.atm.service;

import otus.atm.entity.Balance;
import otus.atm.enums.Transaction;

public interface BalanceTransactionInterface {
    Balance process(Balance balance, Transaction transaction, int amount) throws Exception;
    boolean support(Transaction transaction);
    boolean isInputValid(int amount);
}
