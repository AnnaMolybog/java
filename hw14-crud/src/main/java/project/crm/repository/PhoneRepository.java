package project.crm.repository;

import org.springframework.data.repository.CrudRepository;
import project.crm.model.Phone;

public interface PhoneRepository extends CrudRepository<Phone, Long> {
}
