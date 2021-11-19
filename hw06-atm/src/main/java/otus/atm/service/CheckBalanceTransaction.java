package otus.atm.service;

import otus.atm.entity.CassetteInterface;
import otus.atm.enums.Transaction;

import java.util.List;

public class CheckBalanceTransaction extends AbstractBalanceTransaction {
    @Override
    public List<CassetteInterface> process(
        List<CassetteInterface> cassettes,
        Transaction transaction,
        int amount
    ) {
        return cassettes;
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
