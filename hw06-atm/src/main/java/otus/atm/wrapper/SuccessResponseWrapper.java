package otus.atm.wrapper;

import otus.atm.enums.Denomination;

import java.util.Map;

public class SuccessResponseWrapper extends ResponseWrapper {
    private int totalBalance;
    private Map<Denomination, Integer> availableBanknotes;

    public int getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(int totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Map<Denomination, Integer> getAvailableBanknotes() {
        return availableBanknotes;
    }

    public void setAvailableBanknotes(Map<Denomination, Integer> availableBanknotes) {
        this.availableBanknotes = availableBanknotes;
    }
}
