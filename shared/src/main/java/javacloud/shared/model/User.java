package javacloud.shared.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private final String userName;

    public User(String userName) {
        if (userName == null || userName.trim().length() == 0) {
            throw new NullPointerException("userName");
        }

        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }
}
