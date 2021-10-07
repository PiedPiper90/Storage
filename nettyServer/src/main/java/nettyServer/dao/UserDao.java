package nettyServer.dao;

import nettyServer.model.User;

import java.util.Optional;

public interface UserDao {
    public Optional<User> login(String login, String pass);

}
