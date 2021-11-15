package otus.atm.service;

import otus.atm.entity.BanknoteInterface;
import otus.atm.enums.Transaction;

import java.util.List;

public class CheckBalanceTransaction extends AbstractBalanceTransaction {
    @Override
    public List<BanknoteInterface> process(
        List<BanknoteInterface> banknotes,
        Transaction transaction,
        int amount
    ) {
        return banknotes;
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
