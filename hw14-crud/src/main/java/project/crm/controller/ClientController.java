package project.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.crm.dto.ClientDto;
import project.crm.service.ClientFacade;

import java.util.List;

@Controller
public class ClientController {
    private final ClientFacade clientFacade;

    public ClientController(ClientFacade clientFacade) {
        this.clientFacade = clientFacade;
    }
    
    @GetMapping({"/", "/client/list"})
    public String clients(Model model) {
        List<ClientDto> clients = clientFacade.getClientsWithAddressesAndPhones();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @GetMapping({"/client"})
    public String client() {
        return "client";
    }
}
