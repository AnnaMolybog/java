package project.crm.service;

import project.crm.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String login);
}
