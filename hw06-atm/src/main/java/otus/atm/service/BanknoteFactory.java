package otus.atm.service;

import otus.atm.entity.*;
import otus.atm.enums.Transaction;

public class BanknoteFactory {
    public BanknoteInterface getBanknote(int denomination, int amount) throws Exception {
        if (denomination == Transaction.DEPOSIT_5.getDenomination()) {
            return new Banknote5(amount);
        }

        if (denomination == Transaction.DEPOSIT_10.getDenomination()) {
            return new Banknote10(amount);
        }

        if (denomination == Transaction.DEPOSIT_20.getDenomination()) {
            return new Banknote20(amount);
        }

        if (denomination == Transaction.DEPOSIT_50.getDenomination()) {
            return new Banknote50(amount);
        }

        if (denomination == Transaction.DEPOSIT_100.getDenomination()) {
            return new Banknote100(amount);
        }

        if (denomination == Transaction.DEPOSIT_200.getDenomination()) {
            return new Banknote200(amount);
        }

        if (denomination == Transaction.DEPOSIT_500.getDenomination()) {
            return new Banknote500(amount);
        }

        throw new Exception("invalid denomination");
    }
}
