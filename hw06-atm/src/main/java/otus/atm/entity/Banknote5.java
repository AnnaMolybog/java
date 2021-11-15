package otus.atm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import otus.atm.enums.Transaction;

public class Banknote5 extends Banknote {
    public Banknote5(@JsonProperty("count") int count) {
        super(Transaction.DEPOSIT_5.getDenomination(), count);
    }
}
