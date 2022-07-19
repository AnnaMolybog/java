package project.crm.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import project.crm.model.Client;
import project.crm.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {

    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clientsList = new ArrayList<Client>();
        Long prevClientId = null;
        while (rs.next()) {
            var clientId = rs.getLong("client_id");
            Client client = null;
            if (prevClientId == null || !prevClientId.equals(clientId)) {
                client = new Client(clientId, rs.getString("client_name"), rs.getLong("address_id"), new HashSet<>());
                clientsList.add(client);
                prevClientId = clientId;
            }
            
            Long phoneId = rs.getLong("phone_id");
            if (client !=  null && phoneId != null) {
                client.getPhones().add(new Phone(phoneId, rs.getString("phone_number"), clientId));
            }
        }
        return clientsList;
    }
}
