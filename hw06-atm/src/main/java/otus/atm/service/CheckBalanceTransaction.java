package otus.atm.service;

import otus.atm.entity.Balance;
import otus.atm.enums.Transaction;

public class CheckBalanceTransaction implements BalanceTransactionInterface {
    @Override
    public Balance process(Balance balance, Transaction transaction, int amount) {
        return balance;
    }

    @Override
    public boolean support(Transaction transaction) {
        return transaction.equals(Transaction.CHECK_BALANCE);
    }

    @Override
    public boolean isInputValid(int amount) {
        return true;
    }
}
