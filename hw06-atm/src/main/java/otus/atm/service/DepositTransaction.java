package otus.atm.service;

import otus.atm.entity.BanknoteInterface;
import otus.atm.enums.Transaction;

import java.util.List;

public class DepositTransaction extends AbstractBalanceTransaction {
    @Override
    public List<BanknoteInterface> process(
        List<BanknoteInterface> banknotes,
        Transaction transaction,
        int amount
    ) throws Exception {
        return this.getBalanceService().add(
            banknotes,
            this.getBanknoteFactory().getBanknote(transaction.getDenomination(), amount)
        );
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
