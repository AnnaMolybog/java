package otus.atm.enums;

public enum Denomination {
    BANKNOTE_5 (Transaction.DEPOSIT_5, 5),
    BANKNOTE_10 (Transaction.DEPOSIT_10, 10),
    BANKNOTE_20 (Transaction.DEPOSIT_20, 20),
    BANKNOTE_50 (Transaction.DEPOSIT_50, 50),
    BANKNOTE_100 (Transaction.DEPOSIT_100, 100),
    BANKNOTE_200 (Transaction.DEPOSIT_200, 200),
    BANKNOTE_500 (Transaction.DEPOSIT_500, 500);

    private final Transaction transaction;
    private final int denomination;

    Denomination(Transaction transaction, int denomination) {
        this.transaction = transaction;
        this.denomination = denomination;
    }

    public int getDenomination() {
        return this.denomination;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public static Denomination getByTransaction(Transaction transaction) {
        for (Denomination denomination: Denomination.values()) {
            if (denomination.getTransaction().equals(transaction)) {
                return denomination;
            }
        }

        return null;
    }
}
