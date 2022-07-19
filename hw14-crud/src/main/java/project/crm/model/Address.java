package project.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@Table("address")
public class Address implements Cloneable {
    @Id
    private final Long id;

    @NonNull
    private final String streetName;
    
    public Address(String streetName) {
        this.id = null;
        this.streetName = streetName;
    }
    
    @PersistenceConstructor
    public Address(Long id, String streetName) {
        this.id = id;
        this.streetName = streetName;
    }

    public Long getId() {
        return id;
    }

    public String getStreetName() {
        return streetName;
    }
}
