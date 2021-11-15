package otus.atm.service;

import otus.atm.entity.BanknoteInterface;

import java.util.List;

public interface BalanceServiceInterface {
    List<BanknoteInterface> add(List<BanknoteInterface> banknotes, BanknoteInterface banknote);
    List<BanknoteInterface> update(List<BanknoteInterface> banknotes, BanknoteInterface banknote) throws Exception;
    List<BanknoteInterface> clean(List<BanknoteInterface> banknotes);
    int getTotalSum(List<BanknoteInterface> banknotes);
    int getMinimalDenomination(List<BanknoteInterface> banknotes) throws Exception;
    List<BanknoteInterface> sortBanknotesByDenomination(List<BanknoteInterface> banknotes);
}
