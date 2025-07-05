package model;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private List<UserData> users = new ArrayList<>();

    public List<UserData> getUsers() {
        return users;
    }

    public void setUsers(List<UserData> users) {
        this.users = users;
    }

    public void addUser(UserData user) {
        this.users.add(user);
    }
}
