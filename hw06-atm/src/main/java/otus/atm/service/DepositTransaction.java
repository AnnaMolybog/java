package otus.atm.service;

import otus.atm.entity.Balance;
import otus.atm.enums.Denomination;
import otus.atm.enums.Transaction;

public class DepositTransaction implements BalanceTransactionInterface {
    @Override
    public Balance process(Balance balance, Transaction transaction, int amount) {
        return balance.add(Denomination.getByTransaction(transaction), amount);
    }

    @Override
    public boolean support(Transaction transaction) {
        return transaction.equals(Transaction.DEPOSIT_5) ||
        transaction.equals(Transaction.DEPOSIT_10) ||
        transaction.equals(Transaction.DEPOSIT_20) ||
        transaction.equals(Transaction.DEPOSIT_50) ||
        transaction.equals(Transaction.DEPOSIT_100) ||
        transaction.equals(Transaction.DEPOSIT_200) ||
        transaction.equals(Transaction.DEPOSIT_500);
    }

    public boolean isInputValid(int amount) {
        return amount > 0;
    }
}
