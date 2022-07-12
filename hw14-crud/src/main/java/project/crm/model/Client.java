package project.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.Set;
import java.util.stream.Collectors;

@Table("client")
public class Client implements Cloneable {

    @Id
    private final Long id;

    @NonNull
    private final String clientName;

    @NonNull
    private final Long addressId;

    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;

    public Client(String clientName, Long addressId, Set<Phone> phones) {
        this.id = null;
        this.clientName = clientName;
        this.addressId = addressId;
        this.phones = phones;
    }
    
    @PersistenceConstructor
    public Client(Long id, String clientName, Long addressId, Set<Phone> phones) {
        this.id = id;
        this.clientName = clientName;
        this.addressId = addressId;
        this.phones = phones;
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.clientName, this.addressId, this.phones);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return clientName;
    }

    public Long getAddressId() {
        return addressId;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + clientName + '\'' +
                ", addressId='" + addressId + '\'' +
                ", phoneNumber='" + phones.stream().map(Phone::getPhoneNumber).collect(Collectors.joining(", ")) + '\'' +
                '}';
    }
}
