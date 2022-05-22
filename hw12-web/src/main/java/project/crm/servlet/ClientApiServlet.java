package project.crm.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.crm.model.Address;
import project.crm.model.Client;
import project.crm.model.Phone;
import project.crm.service.ClientService;
import project.crm.wrapper.ClientRequestWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientApiServlet extends HttpServlet {

    private final ClientService clientService;
    private final Gson gson;

    public ClientApiServlet(ClientService clientService, Gson gson) {
        this.clientService = clientService;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ClientRequestWrapper clientWrapper = gson.fromJson(request.getReader(), ClientRequestWrapper.class);

        Client client = new Client(
                null,
                clientWrapper.getName(),
                new Address(null, clientWrapper.getAddress()),
                List.of(new Phone(null, clientWrapper.getPhone()))
        );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream out = response.getOutputStream();

        Map<String, String> responseWrapper = new HashMap<>();

        try {
            clientService.saveClient(client);
            responseWrapper.put("statusCode", "ok");
            responseWrapper.put("message", "Client was created");
        } catch (Exception e) {
            responseWrapper.put("statusCode", "error");
            responseWrapper.put("message", "Client was not created");
        }

        out.print(gson.toJson(responseWrapper));
    }
}
