package project.crm.service;

import org.springframework.stereotype.Service;
import project.crm.converter.ClientConverter;
import project.crm.dto.ClientDto;
import project.crm.model.Address;
import project.crm.model.Client;
import project.crm.model.Phone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientFacade {
    private final ClientService clientService;

    private final AddressService addressService;
    
    private final ClientConverter clientConverter;

    private final PhoneService phoneService;

    public ClientFacade(ClientService clientService,
                        AddressService addressService,
                        ClientConverter clientConverter,
                        PhoneService phoneService
    ) {
        this.clientService = clientService;
        this.addressService = addressService;
        this.clientConverter = clientConverter;
        this.phoneService = phoneService;
    }

    public List<ClientDto> getClientsWithAddressesAndPhones() {
        List<Client> clientsEntities = this.clientService.findAll();
        List<Long> addressIds = clientsEntities.stream().map(Client::getAddressId).collect(Collectors.toList());
        List<Address> addressEntities = this.addressService.getAddressesByIds(addressIds);
        return clientConverter.convert(clientsEntities, addressEntities);
    }

    public ClientDto getClient(Long clientId) throws Exception {
        Client clientEntity = this.clientService
                .getClient(clientId)
                .orElseThrow(() -> new Exception("Client was not found, id: " + clientId));
        
        Address addressEntity = this.addressService.getAddress(clientEntity.getAddressId()).orElse(null);
        return clientConverter.convert(clientEntity, addressEntity, clientEntity.getPhones());
    }

    public ClientDto create(ClientDto clientDto) throws Exception {
        Long addressId = null;
        Address addressEntity = null;
        if (clientDto.getAddress() != null) {
            addressEntity = this.addressService.saveAddress(
                new Address(
                    clientDto.getAddress().getStreetName()
                )
            );
            addressId = addressEntity.getId();
        }
        
        
        Client clientEntity = this.clientService.saveClient(
            new Client(
                clientDto.getClientName(),
                addressId,
                new HashSet<>()
            )
        );
        
        if (clientEntity == null) {
            throw new Exception("Client was not saved");
        }
        
        List<Phone> phones = new ArrayList<>();
        clientDto.getPhones().forEach(phoneDto -> phones.add(new Phone(phoneDto.getPhoneNumber(), clientEntity.getId())));
        Set<Phone> phoneEntities = phoneService.savePhones(phones);

        return clientConverter.convert(clientEntity, addressEntity, phoneEntities);
    }
}
