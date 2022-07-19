package project.crm.dto;

public class AddressDto {
    private String streetName;

    private AddressDto() {
        
    }
    
    public AddressDto(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetName() {
        return streetName;
    }
}
