package project.crm.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import project.crm.helpers.FileSystemHelper;
import project.crm.service.AuthService;
import project.crm.service.ClientService;
import project.crm.service.TemplateProcessor;
import project.crm.servlet.*;

import java.util.Arrays;

public class WebServerImpl implements WebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final Gson gson;
    private final TemplateProcessor templateProcessor;
    private final AuthService authService;
    private final ClientService clientService;
    private final Server server;

    public WebServerImpl(
        int port,
        AuthService authService,
        ClientService clientService,
        Gson gson, TemplateProcessor templateProcessor
    ) {
        this.authService = authService;
        this.clientService = clientService;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/admin/*", "/api/client/*"));


        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new AdminServlet(templateProcessor)), "/admin");
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(templateProcessor, clientService)), "/admin/clients");
        servletContextHandler.addServlet(new ServletHolder(new CreateClientServlet(templateProcessor)), "/admin/client/new");

        servletContextHandler.addServlet(new ServletHolder(new ClientApiServlet(clientService, gson)), "/api/client/*");
        return servletContextHandler;
    }
}
