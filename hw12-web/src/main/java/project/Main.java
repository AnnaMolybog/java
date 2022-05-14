package project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import project.core.repository.DataTemplateHibernate;
import project.core.repository.HibernateUtils;
import project.core.sessionmanager.TransactionManagerHibernate;
import project.crm.dbmigrations.MigrationsExecutorFlyway;
import project.crm.model.Address;
import project.crm.model.Client;
import project.crm.model.Phone;
import project.crm.model.User;
import project.crm.server.WebServer;
import project.crm.server.WebServerImpl;
import project.crm.service.*;

public class Main {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {

        // DB configuration
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        // Migrations
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        // Create client and user services
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class, User.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var clientService = new ClientServiceImpl(transactionManager, clientTemplate);

        var userTemplate = new DataTemplateHibernate<>(User.class);
        var userService = new UserServiceImpl(userTemplate, transactionManager);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        AuthService authService = new UserAuthServiceImpl(userService);

        WebServer webServer = new WebServerImpl(WEB_SERVER_PORT, authService, clientService, gson, templateProcessor);

        webServer.start();
        webServer.join();
    }
}
