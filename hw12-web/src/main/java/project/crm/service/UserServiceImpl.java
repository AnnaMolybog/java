package project.crm.service;

import project.core.repository.DataTemplate;
import project.core.sessionmanager.TransactionManager;
import project.crm.model.User;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final int FIRST_ELEMENT_FROM_RESULT_LIST = 0;

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;

    public UserServiceImpl(DataTemplate<User> userDataTemplate, TransactionManager transactionManager) {
        this.userDataTemplate = userDataTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return transactionManager.doInTransaction(session -> {
            var users = userDataTemplate.findByEntityField(session, "login", login);
            return Optional.ofNullable(users.size() > 0 ? users.get(FIRST_ELEMENT_FROM_RESULT_LIST) : null);
        });
    }
}
