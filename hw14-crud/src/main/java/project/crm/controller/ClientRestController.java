package project.crm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.crm.dto.ClientDto;
import project.crm.service.ClientFacade;

import java.util.List;

@RestController
public class ClientRestController {
    private static final Logger log = LoggerFactory.getLogger(ClientRestController.class);
    
    private final ClientFacade clientFacade;

    public ClientRestController(ClientFacade clientFacade) {
        this.clientFacade = clientFacade;
    }

    @GetMapping("/api/v1/clients")
    public ResponseEntity<List<ClientDto>> clients() {
        try {
            List<ClientDto> clients = clientFacade.getClientsWithAddressesAndPhones();
            log.info("clientDtoList:{}", clients);
            return new ResponseEntity<>(clients, HttpStatus.OK);
        } catch (Exception exception) {
            log.error("Clients cannot be retrieved, error: " + exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/v1/client/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable(name = "id") long id) {
        try {
            return new ResponseEntity<>(
                clientFacade.getClient(id),
                HttpStatus.OK
            );
        } catch (Exception exception) {
            System.out.println(exception);
            log.error("Get client by id error: " + exception.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/v1/client")
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto client) {
        try {
            return new ResponseEntity<>(
                clientFacade.create(client),
                HttpStatus.CREATED
            );
        } catch (Exception exception) {
            log.error("Save client error: " + exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
