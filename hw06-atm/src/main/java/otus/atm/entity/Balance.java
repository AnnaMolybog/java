package otus.atm.entity;

import otus.atm.enums.Denomination;

import java.util.*;

public class Balance {
    private SortedMap<Denomination, Integer> banknotes;

    public Balance() {
        this.banknotes = new TreeMap<>(Comparator.comparing(Denomination::getDenomination).reversed());
    }

    public Balance add(Denomination denomination, int amount) {
        this.banknotes.put(
            denomination,
            this.banknotes.entrySet().stream()
                .filter(banknote -> banknote.getKey().equals(denomination))
                .findAny()
                .map(banknote -> banknote.getValue() + amount)
                .orElse(amount)
        );
        return this;
    }

    public Balance update(Denomination denomination, int amount) {
        this.banknotes.put(denomination, amount);
        return this;
    }

    public Balance clean() {
        this.banknotes.values().removeIf(value -> value == 0);
        return this;
    }

    public int getTotalSum() {
        return this.banknotes.entrySet()
            .stream()
            .mapToInt(banknote -> banknote.getKey().getDenomination() * banknote.getValue())
            .sum();
    }
    
    public int getMinimalDenomination() throws Exception {
        if (this.banknotes.isEmpty()) {
            throw new Exception("balance is empty");
        }

        return this.banknotes.lastKey().getDenomination();
    }

    public Map<Denomination, Integer> getBanknotes() {
        return Collections.unmodifiableMap(this.banknotes);
    }
}
