package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);
    private static final MyCache<String, Client> cache = new MyCache<>();

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionRunner transactionRunner;

    public DbServiceClientImpl(
            TransactionRunner transactionRunner,
            DataTemplate<Client> clientDataTemplate
    ) {
        this.transactionRunner = transactionRunner;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = clientDataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            clientDataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Optional<Client> cachedClient = Optional.ofNullable(cache.get(this.generateCacheKey(id)));
        if (cachedClient.isPresent()) {
            return cachedClient;
        }

        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = clientDataTemplate.findById(connection, id);
            clientOptional.ifPresent(client -> cache.put(this.generateCacheKey(id), client));
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = clientDataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }

    private String generateCacheKey(long id) {
        return "client_" + id;
    }
}
