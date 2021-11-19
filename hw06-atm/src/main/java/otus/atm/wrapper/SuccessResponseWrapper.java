package otus.atm.wrapper;

import otus.atm.entity.CassetteInterface;

import java.util.List;

public class SuccessResponseWrapper extends ResponseWrapper {
    private int totalBalance;
    private List<CassetteInterface> availableCassettes;

    public int getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(int totalBalance) {
        this.totalBalance = totalBalance;
    }

    public List<CassetteInterface> getAvailableCassettes() {
        return availableCassettes;
    }

    public void setAvailableCassettes(List<CassetteInterface> availableCassettes) {
        this.availableCassettes = availableCassettes;
    }
}
