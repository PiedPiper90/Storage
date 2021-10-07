package nettyServer.model;

import nettyServer.util.Request;

public class UserRequest {
    private User user;
    private Request request;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }


    public UserRequest(User user, Request request) {
        this.user = user;
        this.request = request;
    }

    public UserRequest() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
