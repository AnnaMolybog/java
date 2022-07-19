package project.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import project.core.sessionmanager.TransactionManager;
import project.crm.model.Address;
import project.crm.repository.AddressRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);
    
    private final AddressRepository addressRepository;

    private final TransactionManager transactionManager;

    public AddressServiceImpl(AddressRepository addressRepository, TransactionManager transactionManager) {
        this.addressRepository = addressRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public Address saveAddress(Address address) {
        return transactionManager.doInTransaction(() -> {
            var savedAddress = addressRepository.save(address);
            log.info("saved address: {}", savedAddress);
            return savedAddress;
        });
    }

    @Override
    public List<Address> getAddressesByIds(List<Long> addressIds) {
        var addressList = this.addressRepository.findAllByIdIn(addressIds);
        log.info("addressList:{}", addressList);
        return addressList;
    }

    @Override
    public Optional<Address> getAddress(Long addressId) {
        return this.addressRepository.findById(addressId);
    }
}
