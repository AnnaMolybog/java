package otus.atm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import otus.atm.enums.Transaction;

public class Banknote100 extends Banknote {
    public Banknote100(@JsonProperty("count") int count) {
        super(Transaction.DEPOSIT_100.getDenomination(), count);
    }
}
