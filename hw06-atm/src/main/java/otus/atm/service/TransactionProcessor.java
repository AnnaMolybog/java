package otus.atm.service;

import otus.atm.entity.Balance;
import otus.atm.enums.Transaction;
import otus.atm.exception.InvalidInputException;

import java.util.ArrayList;

public class TransactionProcessor {
    private Balance balance;
    private final ArrayList<BalanceTransactionInterface> balanceTransactions;

    public TransactionProcessor() {
        this.balance = new Balance();
        this.balanceTransactions = new ArrayList<>();
        this.balanceTransactions.add(new DepositTransaction());
        this.balanceTransactions.add(new WithdrawTransaction());
        this.balanceTransactions.add(new CheckBalanceTransaction());
    }

    public Balance executeTransaction(Transaction transaction, int amount) throws Exception {
        for (BalanceTransactionInterface balanceTransaction: this.balanceTransactions) {
            if (balanceTransaction.support(transaction) && balanceTransaction.isInputValid(amount)) {
                this.balance = balanceTransaction.process(this.balance, transaction, amount);
                return this.balance;
            }
        }

        throw new InvalidInputException();
    }
}
