package project.crm.service;

import project.crm.model.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    Address saveAddress(Address address);
    
    List<Address> getAddressesByIds(List<Long> addressIds);

    Optional<Address> getAddress(Long addressId);
}
