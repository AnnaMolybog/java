package project.crm.service;

import project.crm.model.Phone;

import java.util.List;
import java.util.Set;

public interface PhoneService {
    Set<Phone> savePhones(List<Phone> phones);
}
