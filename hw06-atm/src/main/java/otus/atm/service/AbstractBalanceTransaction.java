package otus.atm.service;

public abstract class AbstractBalanceTransaction implements BalanceTransactionInterface {
    private final BalanceService balanceService;
    private final BanknoteFactory banknoteFactory;

    public AbstractBalanceTransaction() {
        this.balanceService = new BalanceService();
        this.banknoteFactory = new BanknoteFactory();
    }

    public BalanceService getBalanceService() {
        return balanceService;
    }

    public BanknoteFactory getBanknoteFactory() {
        return this.banknoteFactory;
    }
}
