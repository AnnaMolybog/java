package project.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@Table("phone")
public class Phone implements Cloneable {
    @Id
    private final Long id;

    @NonNull
    private final String phoneNumber;

    @NonNull
    private final Long clientId;

    public Phone(String phoneNumber, Long clientId) {
        this.id = null;
        this.phoneNumber = phoneNumber;
        this.clientId = clientId;
    }
    
    @PersistenceConstructor
    public Phone(Long id, String phoneNumber, Long clientId) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Long getClientId() {
        return clientId;
    }
}
