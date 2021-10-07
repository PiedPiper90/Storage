package nettyServer.model;

public class User {
    private String login;
    private String password;
    private Long id;

    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User(String login, String password, Long id) {
        this.login = login;
        this.password = password;
        this.id = id;
    }
}
