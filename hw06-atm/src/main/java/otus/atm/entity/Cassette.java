package otus.atm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import otus.atm.enums.Transaction;

public class Cassette implements CassetteInterface {
    private final int denomination;
    private int count;

    public Cassette(@JsonProperty("denomination") int denomination, @JsonProperty("count") int count) throws Exception {
        this.validateDenomination(denomination);
        this.denomination = denomination;
        this.count = count;
    }

    public int getDenomination() {
        return denomination;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private void validateDenomination(int denomination) throws Exception {
        if (Transaction.isDenominationSupported(denomination)) {
            return;
        }

        throw new Exception("Denomination value is not supported: " + denomination);
    }
}
