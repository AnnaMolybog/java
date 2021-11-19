package otus.atm.service;

import otus.atm.entity.CassetteInterface;
import otus.atm.enums.Transaction;

import java.util.List;

public abstract class AbstractBalanceTransaction implements BalanceTransactionInterface {
    private final CassettesStorageInterface cassettesStorage;
    private final BalanceServiceInterface balanceService;

    public AbstractBalanceTransaction(CassettesStorageInterface cassettesStorage, BalanceServiceInterface balanceService) {
        this.cassettesStorage = cassettesStorage;
        this.balanceService = balanceService;
    }

    public void process(
        Transaction transaction,
        int amount
    ) throws Exception {
        this.cassettesStorage.updateCassettes(
            this.processInternal(
                this.cassettesStorage.getCassettes(),
                transaction,
                amount
            )
        );
    }

    public BalanceServiceInterface getBalanceService() {
        return balanceService;
    }

    abstract protected List<CassetteInterface> processInternal(
        List<CassetteInterface> cassettes,
        Transaction transaction,
        int amount
    ) throws Exception;
}
