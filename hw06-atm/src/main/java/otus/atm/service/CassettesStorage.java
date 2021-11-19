package otus.atm.service;

import otus.atm.entity.CassetteInterface;

import java.util.ArrayList;
import java.util.List;

public class CassettesStorage implements CassettesStorageInterface {
    private List<CassetteInterface> cassettes;

    public CassettesStorage() {
        this.cassettes = new ArrayList<>();
    }

    @Override
    public List<CassetteInterface> getCassettes() {
        return this.cassettes;
    }

    @Override
    public void updateCassettes(List<CassetteInterface> cassettes) {
        this.cassettes = cassettes;
    }
}
