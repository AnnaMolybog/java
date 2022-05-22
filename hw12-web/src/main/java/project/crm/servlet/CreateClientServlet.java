package project.crm.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.crm.service.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;

public class CreateClientServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "create_client.html";

    private final TemplateProcessor templateProcessor;

    public CreateClientServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, new HashMap<>()));
    }
}
