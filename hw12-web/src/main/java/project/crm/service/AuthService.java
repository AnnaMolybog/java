package project.crm.service;

public interface AuthService {
    boolean authenticate(String login, String password);
}
