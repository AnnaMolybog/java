package project.crm.repository;

import org.springframework.data.repository.CrudRepository;
import project.crm.model.Address;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address, Long> {
    List<Address> findAllByIdIn(List<Long> addressIds);
}
