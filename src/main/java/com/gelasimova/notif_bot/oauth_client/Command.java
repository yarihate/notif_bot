package com.gelasimova.notif_bot.oauth_client;

import java.util.Arrays;

public enum Command {
    REQUEST_TOKEN("requestToken"),
    ACCESS_TOKEN("accessToken"),
    REQUEST("request");

    private final String name;

    Command(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static String names() {
        return Arrays.stream(values())
                .map(Command::getName).toList()
                .toString();
    }

    public static Command fromString(String name) {
        if (name != null) {
            for (Command b : Command.values()) {
                if (name.equalsIgnoreCase(b.name)) {
                    return b;
                }
            }
        }
        return null;
    }
}