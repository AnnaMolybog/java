package otus.atm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import otus.atm.enums.Transaction;

public class Banknote20 extends Banknote {
    public Banknote20(@JsonProperty("count") int count) {
        super(Transaction.DEPOSIT_20.getDenomination(), count);
    }
}
