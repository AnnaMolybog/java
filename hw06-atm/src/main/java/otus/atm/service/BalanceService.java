package otus.atm.service;

import otus.atm.entity.BanknoteInterface;

import java.util.Comparator;
import java.util.List;

public class BalanceService implements BalanceServiceInterface {
    @Override
    public List<BanknoteInterface> add(List<BanknoteInterface> banknotes, BanknoteInterface banknote) {
        banknotes.add(banknotes.stream()
            .filter(existentBanknote -> existentBanknote.getDenomination() == banknote.getDenomination())
            .findAny()
            .map(existentBanknote -> {
                existentBanknote.setCount(existentBanknote.getCount() + banknote.getCount());
                return existentBanknote;
            })
            .orElse(banknote)
        );

        return banknotes;
    }

    @Override
    public List<BanknoteInterface> update(List<BanknoteInterface> banknotes, BanknoteInterface banknote) throws Exception {
        banknotes.stream()
            .filter(existentBanknote -> existentBanknote.getDenomination() == banknote.getDenomination())
            .findAny()
            .map(existentBanknote -> {
                existentBanknote.setCount(banknote.getCount());
                return existentBanknote;
            })
            .orElseThrow(() -> new Exception("Banknote was not found"));

        return banknotes;
    }

    @Override
    public List<BanknoteInterface> clean(List<BanknoteInterface> banknotes) {
        banknotes.removeIf(banknote -> banknote.getCount() == 0);
        return banknotes;
    }

    @Override
    public int getTotalSum(List<BanknoteInterface> banknotes) {
        return banknotes.stream()
            .mapToInt(banknote -> banknote.getDenomination() * banknote.getCount())
            .sum();
    }

    @Override
    public int getMinimalDenomination(List<BanknoteInterface> banknotes) throws Exception {
        if (banknotes.isEmpty()) {
            throw new Exception("balance is empty");
        }

        this.sortBanknotesByDenomination(banknotes);
        return banknotes.get(banknotes.size() - 1).getDenomination();
    }

    @Override
    public List<BanknoteInterface> sortBanknotesByDenomination(List<BanknoteInterface> banknotes) {
        banknotes.sort(Comparator.comparing(BanknoteInterface::getDenomination).reversed());
        return banknotes;
    }
}
