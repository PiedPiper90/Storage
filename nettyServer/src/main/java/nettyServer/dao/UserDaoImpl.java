package nettyServer.dao;

import nettyServer.model.User;
import nettyServer.util.JdbcConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    @Override
    public Optional<User> login(String login, String pass) {
        Connection connection = null;

        try {
            connection = JdbcConnect.connect();
            PreparedStatement statement = connection.prepareStatement("SELECT id,login,password FROM users WHERE login=? AND password=?;");
            statement.setString(1, login);
            statement.setString(2, pass);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("id");
            String log = resultSet.getString("login");
            String password = resultSet.getString("password");
            return Optional.of(new User(log, password, id));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
}
