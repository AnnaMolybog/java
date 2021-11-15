package otus.atm.enums;

public enum Transaction {
    WITHDRAW,
    CHECK_BALANCE,
    DEPOSIT_5(5),
    DEPOSIT_10(10),
    DEPOSIT_20(20),
    DEPOSIT_50(50),
    DEPOSIT_100(100),
    DEPOSIT_200(200),
    DEPOSIT_500(500);

    private int denomination;

    Transaction() {
    }

    Transaction(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return this.denomination;
    }
}
