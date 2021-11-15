package otus.atm.entity;

public abstract class Banknote implements BanknoteInterface {
    private final int denomination;
    private int count;

    public Banknote(int denomination, int count) {
        this.denomination = denomination;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDenomination() {
        return this.denomination;
    }
}
