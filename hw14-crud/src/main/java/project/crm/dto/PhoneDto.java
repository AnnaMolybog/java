package project.crm.dto;

public class PhoneDto {
    private String phoneNumber;

    public PhoneDto() {
        
    }

    public PhoneDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
