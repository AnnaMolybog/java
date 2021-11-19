package otus.atm.service;

import otus.atm.entity.CassetteInterface;

import java.util.List;

public interface BalanceServiceInterface {
    List<CassetteInterface> add(List<CassetteInterface> cassettes, CassetteInterface cassette);
    List<CassetteInterface> update(List<CassetteInterface> cassettes, CassetteInterface cassette) throws Exception;
    List<CassetteInterface> clean(List<CassetteInterface> cassettes);
    int getTotalSum(List<CassetteInterface> cassettes);
    int getMinimalDenomination(List<CassetteInterface> cassettes) throws Exception;
    List<CassetteInterface> sortCassettesByDenomination(List<CassetteInterface> cassettes);
}
