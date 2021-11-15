package otus.atm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import otus.atm.enums.Transaction;

public class Banknote10 extends Banknote {
    public Banknote10(@JsonProperty("count") int count) {
        super(Transaction.DEPOSIT_10.getDenomination(), count);
    }
}
