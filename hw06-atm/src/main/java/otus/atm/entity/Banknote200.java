package otus.atm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import otus.atm.enums.Transaction;

public class Banknote200 extends Banknote {
    public Banknote200(@JsonProperty("count") int count) {
        super(Transaction.DEPOSIT_200.getDenomination(), count);
    }
}
