package project.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import project.core.sessionmanager.TransactionManager;
import project.crm.model.Client;
import project.crm.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;

    public ClientServiceImpl(ClientRepository clientRepository, TransactionManager transactionManager) {
        this.clientRepository = clientRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var clientOptional = clientRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    public List<Client> findAll() {
        var clientList = new ArrayList<Client>(clientRepository.findAll());
        log.info("clientList:{}", clientList);
        return clientList;
    }
}
