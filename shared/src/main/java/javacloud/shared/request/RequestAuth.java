package javacloud.shared.request;

import javacloud.shared.model.Command;
import javacloud.shared.utils.StringUtils;

import java.util.Objects;

public class RequestAuth extends Request {
    private final String userName;
    private final String password;

    public RequestAuth(String userName, String password) {
        super(Command.AUTH);
        this.userName = Objects.requireNonNull(StringUtils.nullIfEmpty(userName));
        this.password = Objects.requireNonNull(StringUtils.nullIfEmpty(password));
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
