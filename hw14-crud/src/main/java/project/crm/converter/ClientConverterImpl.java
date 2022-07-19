package project.crm.converter;

import org.springframework.stereotype.Component;
import project.crm.dto.AddressDto;
import project.crm.dto.ClientDto;
import project.crm.dto.PhoneDto;
import project.crm.model.Address;
import project.crm.model.Client;
import project.crm.model.Phone;

import java.util.*;

@Component
public class ClientConverterImpl implements ClientConverter {
    @Override
    public ClientDto convert(Client client, Address address, Set<Phone> phones) {
        Set<PhoneDto> phoneDtos = new HashSet<>();
        phones.forEach(phone -> phoneDtos.add(new PhoneDto(phone.getPhoneNumber())));
        
        String addressName = address != null  ? address.getStreetName() : null;
        
        return new ClientDto(
                client.getName(),
                new AddressDto(addressName),
                phoneDtos
        );
    }

    @Override
    public List<ClientDto> convert(List<Client> clients, List<Address> addresses) {
        List<ClientDto> clientDtos = new ArrayList<>();
        clients.forEach(client -> {
            Address address = addresses.stream()
                    .filter(address1 -> address1.getId().equals(client.getAddressId()))
                    .findFirst()
                    .orElse(null);
            clientDtos.add(this.convert(client, address, client.getPhones()));
        });
        
        return clientDtos;
    }
}
