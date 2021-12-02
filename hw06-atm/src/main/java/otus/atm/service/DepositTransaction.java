package otus.atm.service;

import otus.atm.entity.Cassette;
import otus.atm.entity.CassetteInterface;
import otus.atm.enums.Transaction;

import java.util.List;

public class DepositTransaction extends AbstractBalanceTransaction {
    public DepositTransaction(CassettesStorageInterface cassettesStorage, BalanceServiceInterface balanceService) {
        super(cassettesStorage, balanceService);
    }

    @Override
    protected List<CassetteInterface> processInternal(
        List<CassetteInterface> cassettes,
        Transaction transaction,
        int amount
    ) throws Exception {
        return this.getBalanceService().add(
            cassettes,
            new Cassette(transaction.getDenomination(), amount)
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
