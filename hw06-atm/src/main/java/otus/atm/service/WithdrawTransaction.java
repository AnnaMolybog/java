package otus.atm.service;

import otus.atm.entity.Cassette;
import otus.atm.entity.CassetteInterface;
import otus.atm.enums.Transaction;

import java.util.List;

public class WithdrawTransaction extends AbstractBalanceTransaction {
    @Override
    public List<CassetteInterface> process(
        List<CassetteInterface> cassettes,
        Transaction transaction,
        int amount
    ) throws Exception {
        this.getBalanceService().sortCassettesByDenomination(cassettes);
        this.checkRetrievalPossibility(amount, cassettes);

        while (amount != 0) {
            for (CassetteInterface cassette: cassettes) {

                int numberOfBanknotesRequired = amount / cassette.getDenomination();
                int numberOfBanknotesExistent = Math.min(numberOfBanknotesRequired, cassette.getCount());

                cassettes = this.getBalanceService().update(
                    cassettes,
                    new Cassette(cassette.getDenomination(), cassette.getCount() - numberOfBanknotesExistent)
                );

                amount = amount - cassette.getDenomination() * numberOfBanknotesExistent;
            }
        }

        return this.getBalanceService().clean(cassettes);
    }

    @Override
    public boolean support(Transaction transaction) {
        return transaction.equals(Transaction.WITHDRAW);
    }

    @Override
    public boolean isInputValid(int amount) {
        return amount >= 0;
    }

    private void checkRetrievalPossibility(int amount, List<CassetteInterface> cassettes) throws Exception {
        if (amount > this.getBalanceService().getTotalSum(cassettes)) {
            throw new Exception("Insufficient funds in the account");
        }

        int minimalDenomination = this.getBalanceService().getMinimalDenomination(cassettes);
        if (amount % minimalDenomination != 0) {
            throw new Exception(String.format("Minimal denomination is: %d", minimalDenomination));
        }
    }
}
