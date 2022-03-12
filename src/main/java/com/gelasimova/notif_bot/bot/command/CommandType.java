package com.gelasimova.notif_bot.bot.command;

import java.util.Arrays;

public enum CommandType {
    START("/start"),
    UPDATE("/update"),
    CHANGE_FREQUENCY("/change_frequency"),
    STOP("/stop");

    final String name;

    CommandType(String name) {
        this.name = name;
    }

    public static CommandType getCommandTypeByName(String name) {
        return Arrays.stream(CommandType.values()).filter(v -> v.name.equals(name)).findFirst().orElse(null);
    }
}
