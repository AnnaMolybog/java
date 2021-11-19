package otus.atm.service;

import otus.atm.enums.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ATMService {
    private final List<BalanceTransactionInterface> balanceTransactions;

    public ATMService(CassettesStorageInterface cassettesStorage, BalanceServiceInterface balanceService) {
        this.balanceTransactions = new ArrayList<>();
        this.balanceTransactions.add(new DepositTransaction(cassettesStorage, balanceService));
        this.balanceTransactions.add(new WithdrawTransaction(cassettesStorage, balanceService));
        this.balanceTransactions.add(new CheckBalanceTransaction(cassettesStorage, balanceService));
    }

    public void executeTransaction(Transaction transaction, int amount) throws Exception {
        for (BalanceTransactionInterface balanceTransaction: this.balanceTransactions) {
            if (balanceTransaction.support(transaction) && balanceTransaction.isInputValid(amount)) {
                balanceTransaction.process(transaction, amount);
                return;
            }
        }

        throw new Exception("Transaction or amount value are invalid");
    }
}
