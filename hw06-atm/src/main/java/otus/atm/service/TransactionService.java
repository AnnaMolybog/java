package otus.atm.service;

import otus.atm.entity.CassetteInterface;
import otus.atm.enums.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    private List<CassetteInterface> cassettes;
    private final List<BalanceTransactionInterface> balanceTransactions;

    public TransactionService(List<CassetteInterface> cassettes) {
        this.cassettes = cassettes;
        this.balanceTransactions = new ArrayList<>();
        this.balanceTransactions.add(new DepositTransaction());
        this.balanceTransactions.add(new WithdrawTransaction());
        this.balanceTransactions.add(new CheckBalanceTransaction());
    }

    public List<CassetteInterface> executeTransaction(Transaction transaction, int amount) throws Exception {
        for (BalanceTransactionInterface balanceTransaction: this.balanceTransactions) {
            if (balanceTransaction.support(transaction) && balanceTransaction.isInputValid(amount)) {
                return balanceTransaction.process(cassettes, transaction, amount);
            }
        }

        throw new Exception("Transaction or amount value are invalid");
    }
}
