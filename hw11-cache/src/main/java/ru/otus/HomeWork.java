package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) throws InterruptedException {
        // Run migrations
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        // Init required services
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); //реализация DataTemplate, универсальная
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);

        // Create clients
        List<Long> clientIds = new ArrayList<>();
        for (var idx = 0; idx < 1000; idx++) {
            var client = dbServiceClient.saveClient(new Client("dbService" + (idx + 1)));
            clientIds.add(client.getId());
        }

        // Get clients and check execution time
        var executionTimeBeforeCaching = execute(dbServiceClient, clientIds);
        var executionTimeAfterCaching = execute(dbServiceClient, clientIds);

        // Force GC, Get clients and check execution time
        System.gc();
        Thread.sleep(100);
        var executionTimeAfterGC = execute(dbServiceClient, clientIds);

        // Print execution time
        System.out.println("executionTimeBeforeCaching: " + executionTimeBeforeCaching);
        System.out.println("executionTimeAfterCaching: " + executionTimeAfterCaching);
        System.out.println("Execution time after gc: " + executionTimeAfterGC);
    }

    private static Double execute(DBServiceClient dbServiceClient, List<Long> clientIds) {
        long startTime = System.currentTimeMillis();
        clientIds.forEach(clientId -> {
            var clientSecondSelected = dbServiceClient
                    .getClient(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientId));
            log.info("clientSecondSelected:{}", clientSecondSelected);
        });

        return (double) (System.currentTimeMillis() - startTime);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
