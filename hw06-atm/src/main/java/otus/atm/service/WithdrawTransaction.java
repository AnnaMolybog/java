package otus.atm.service;

import otus.atm.entity.Balance;
import otus.atm.enums.Denomination;
import otus.atm.enums.Transaction;
import otus.atm.exception.InsufficientFundsException;
import otus.atm.exception.MinimalDenominationException;

import java.util.Map;

public class WithdrawTransaction implements BalanceTransactionInterface {
    @Override
    public Balance process(Balance balance, Transaction transaction, int amount) throws Exception {
        this.checkRetrievalPossibility(amount, balance);
        while (amount != 0) {
            for (Map.Entry<Denomination, Integer> banknote: balance.getBanknotes().entrySet()) {
                int numberOfBanknotesRequired = amount / banknote.getKey().getDenomination();
                int numberOfBanknotesExistent = Math.min(numberOfBanknotesRequired, banknote.getValue());

                balance = balance.update(banknote.getKey(), banknote.getValue() - numberOfBanknotesExistent);
                amount = amount - banknote.getKey().getDenomination() * numberOfBanknotesExistent;
            }
        }

        return balance.clean();
    }

    @Override
    public boolean support(Transaction transaction) {
        return transaction.equals(Transaction.WITHDRAW);
    }

    @Override
    public boolean isInputValid(int amount) {
        return amount >= 0;
    }

    private void checkRetrievalPossibility(int amount, Balance balance) throws Exception {
        if (amount > balance.getTotalSum()) {
            throw new InsufficientFundsException();
        }

        int minimalDenomination = balance.getMinimalDenomination();
        if (amount % minimalDenomination != 0) {
            throw new MinimalDenominationException(minimalDenomination);
        }
    }
}
