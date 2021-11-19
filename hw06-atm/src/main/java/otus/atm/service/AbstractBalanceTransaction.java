package otus.atm.service;

public abstract class AbstractBalanceTransaction implements BalanceTransactionInterface {
    private final BalanceService balanceService;

    public AbstractBalanceTransaction() {
        this.balanceService = new BalanceService();
    }

    public BalanceService getBalanceService() {
        return balanceService;
    }
}
