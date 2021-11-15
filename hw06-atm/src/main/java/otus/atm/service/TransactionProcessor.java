package otus.atm.service;

import otus.atm.entity.BanknoteInterface;
import otus.atm.enums.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionProcessor {
    private List<BanknoteInterface> banknotes;
    private final List<BalanceTransactionInterface> balanceTransactions;

    public TransactionProcessor() {
        this.banknotes = new ArrayList<>();
        this.balanceTransactions = new ArrayList<>();
        this.balanceTransactions.add(new DepositTransaction());
        this.balanceTransactions.add(new WithdrawTransaction());
        this.balanceTransactions.add(new CheckBalanceTransaction());
    }

    public List<BanknoteInterface> executeTransaction(Transaction transaction, int amount) throws Exception {
        for (BalanceTransactionInterface balanceTransaction: this.balanceTransactions) {
            if (balanceTransaction.support(transaction) && balanceTransaction.isInputValid(amount)) {
                this.banknotes = balanceTransaction.process(this.banknotes, transaction, amount);
                return this.banknotes;
            }
        }

        throw new Exception("Transaction or amount value are invalid");
    }
}
