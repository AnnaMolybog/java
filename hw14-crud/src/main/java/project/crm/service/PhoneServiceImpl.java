package project.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import project.core.sessionmanager.TransactionManager;
import project.crm.model.Phone;
import project.crm.repository.PhoneRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PhoneServiceImpl implements PhoneService {
    private static final Logger log = LoggerFactory.getLogger(PhoneServiceImpl.class);

    private final PhoneRepository phoneRepository;
    private final TransactionManager transactionManager;

    public PhoneServiceImpl(PhoneRepository phoneRepository, TransactionManager transactionManager) {
        this.phoneRepository = phoneRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public Set<Phone> savePhones(List<Phone> phones) {
        return transactionManager.doInTransaction(() -> {
            var savedPhones = phoneRepository.saveAll(phones);
            log.info("saved client: {}", savedPhones);
            Set<Phone> result = new HashSet<>();
            savedPhones.forEach(result::add);
            return result;
        });
    }
}
