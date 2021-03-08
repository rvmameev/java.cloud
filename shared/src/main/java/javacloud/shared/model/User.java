package javacloud.shared.model;

import javacloud.shared.utils.StringUtils;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private final int id;
    private final String userName;
    private final String token;

    public User(int id, String userName, String token) {
        this.id = id;
        this.userName = Objects.requireNonNull(StringUtils.nullIfEmpty(userName));
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }
}
