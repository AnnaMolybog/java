package otus.atm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import otus.atm.enums.Transaction;

public class Banknote50 extends Banknote {
    public Banknote50(@JsonProperty("count") int count) {
        super(Transaction.DEPOSIT_50.getDenomination(), count);
    }
}
