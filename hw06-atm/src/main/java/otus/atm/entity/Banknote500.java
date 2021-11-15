package otus.atm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import otus.atm.enums.Transaction;

public class Banknote500 extends Banknote {
    public Banknote500(@JsonProperty("count") int count) {
        super(Transaction.DEPOSIT_500.getDenomination(), count);
    }
}
