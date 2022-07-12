package project.crm.dto;

import java.util.Set;

public class ClientDto {
    
    private String clientName;
    
    private AddressDto address;

    private Set<PhoneDto> phones;

    public ClientDto() {
        
    }
    
    public ClientDto( String clientName, AddressDto address, Set<PhoneDto> phones) {
        this.clientName = clientName;
        this.address = address;
        this.phones = phones;
    }

    public String getClientName() {
        return clientName;
    }

    public AddressDto getAddress() {
        return address;
    }

    public Set<PhoneDto> getPhones() {
        return phones;
    }
}
