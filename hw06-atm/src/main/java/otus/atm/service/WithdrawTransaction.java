package otus.atm.service;

import otus.atm.entity.BanknoteInterface;
import otus.atm.enums.Transaction;

import java.util.List;

public class WithdrawTransaction extends AbstractBalanceTransaction {
    @Override
    public List<BanknoteInterface> process(
        List<BanknoteInterface> banknotes,
        Transaction transaction,
        int amount
    ) throws Exception {
        this.getBalanceService().sortBanknotesByDenomination(banknotes);
        this.checkRetrievalPossibility(amount, banknotes);

        while (amount != 0) {
            for (BanknoteInterface banknote: banknotes) {

                int numberOfBanknotesRequired = amount / banknote.getDenomination();
                int numberOfBanknotesExistent = Math.min(numberOfBanknotesRequired, banknote.getCount());

                banknotes = this.getBalanceService().update(
                    banknotes,
                    this.getBanknoteFactory().getBanknote(
                        banknote.getDenomination(),
                        banknote.getCount() - numberOfBanknotesExistent
                    )
                );

                amount = amount - banknote.getDenomination() * numberOfBanknotesExistent;
            }
        }

        return this.getBalanceService().clean(banknotes);
    }

    @Override
    public boolean support(Transaction transaction) {
        return transaction.equals(Transaction.WITHDRAW);
    }

    @Override
    public boolean isInputValid(int amount) {
        return amount >= 0;
    }

    private void checkRetrievalPossibility(int amount, List<BanknoteInterface> banknotes) throws Exception {
        if (amount > this.getBalanceService().getTotalSum(banknotes)) {
            throw new Exception("Insufficient funds in the account");
        }

        int minimalDenomination = this.getBalanceService().getMinimalDenomination(banknotes);
        if (amount % minimalDenomination != 0) {
            throw new Exception(String.format("Minimal denomination is: %d", minimalDenomination));
        }
    }
}
