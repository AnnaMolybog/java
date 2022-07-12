package project.crm.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import project.crm.model.Client;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {

    @Override
    @Query(value = "select c.id as client_id, c.client_name as client_name, c.address_id as address_id, p.id as phone_id, p.phone_number as phone_number from client c left outer join phone p on c.id = p.client_id order by c.id", 
            resultSetExtractorClass = ClientResultSetExtractorClass.class
    )
    List<Client> findAll();
}