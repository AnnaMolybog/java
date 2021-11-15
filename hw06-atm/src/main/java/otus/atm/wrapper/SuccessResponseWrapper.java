package otus.atm.wrapper;

import otus.atm.entity.BanknoteInterface;

import java.util.List;

public class SuccessResponseWrapper extends ResponseWrapper {
    private int totalBalance;
    private List<BanknoteInterface> availableBanknotes;

    public int getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(int totalBalance) {
        this.totalBalance = totalBalance;
    }

    public List<BanknoteInterface> getAvailableBanknotes() {
        return availableBanknotes;
    }

    public void setAvailableBanknotes(List<BanknoteInterface> availableBanknotes) {
        this.availableBanknotes = availableBanknotes;
    }
}
