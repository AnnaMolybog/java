package otus.atm.service;

import otus.atm.entity.CassetteInterface;

import java.util.List;

public interface CassettesStorageInterface {
    public List<CassetteInterface> getCassettes();
    public void updateCassettes(List<CassetteInterface> cassettes);
}
