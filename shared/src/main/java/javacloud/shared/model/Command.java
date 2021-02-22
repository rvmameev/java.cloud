package javacloud.shared.model;

import java.io.Serializable;

public enum Command implements Serializable
{
    AUTH("/auth"),
    LS("/ls"),
    GET_FILE("/get_file"),
    PUT_FILE("/put_file");

    private final String command;

    Command(String command)
    {
        this.command = command;
    }

    public String getCommand()
    {
        return command;
    }
}