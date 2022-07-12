package project.crm.converter;

import project.crm.dto.ClientDto;
import project.crm.model.Address;
import project.crm.model.Client;
import project.crm.model.Phone;

import java.util.List;
import java.util.Set;

public interface ClientConverter {
    ClientDto convert(Client client, Address address, Set<Phone> phones);
    
    List<ClientDto> convert(List<Client> clients, List<Address> addresses);
}
