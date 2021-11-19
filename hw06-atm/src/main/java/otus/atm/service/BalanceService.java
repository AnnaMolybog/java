package otus.atm.service;

import otus.atm.entity.CassetteInterface;

import java.util.Comparator;
import java.util.List;

public class BalanceService implements BalanceServiceInterface {
    @Override
    public List<CassetteInterface> add(List<CassetteInterface> cassettes, CassetteInterface cassette) {
        cassettes.add(cassettes.stream()
            .filter(existentCassette -> existentCassette.getDenomination() == cassette.getDenomination())
            .findAny()
            .map(existentCassette -> {
                existentCassette.setCount(existentCassette.getCount() + cassette.getCount());
                return existentCassette;
            })
            .orElse(cassette)
        );

        return cassettes;
    }

    @Override
    public List<CassetteInterface> update(List<CassetteInterface> cassettes, CassetteInterface cassette) throws Exception {
        cassettes.stream()
            .filter(existentCassette -> existentCassette.getDenomination() == cassette.getDenomination())
            .findAny()
            .map(existentCassette -> {
                existentCassette.setCount(cassette.getCount());
                return existentCassette;
            })
            .orElseThrow(() -> new Exception("Banknote was not found"));

        return cassettes;
    }

    @Override
    public List<CassetteInterface> clean(List<CassetteInterface> cassettes) {
        cassettes.removeIf(cassette -> cassette.getCount() == 0);
        return cassettes;
    }

    @Override
    public int getTotalSum(List<CassetteInterface> cassettes) {
        return cassettes.stream()
            .mapToInt(cassette -> cassette.getDenomination() * cassette.getCount())
            .sum();
    }

    @Override
    public int getMinimalDenomination(List<CassetteInterface> cassettes) throws Exception {
        if (cassettes.isEmpty()) {
            throw new Exception("balance is empty");
        }

        this.sortCassettesByDenomination(cassettes);
        return cassettes.get(cassettes.size() - 1).getDenomination();
    }

    @Override
    public List<CassetteInterface> sortCassettesByDenomination(List<CassetteInterface> cassettes) {
        cassettes.sort(Comparator.comparing(CassetteInterface::getDenomination).reversed());
        return cassettes;
    }
}
