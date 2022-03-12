package com.gelasimova.notif_bot.bot.command;

import static com.gelasimova.notif_bot.bot.command.CommandType.getCommandTypeByName;

public class CommandFactory {

    public Command createCommand(String message) {
        return switch (getCommandTypeByName(message)) {
            case START -> new StartCommand();
            case UPDATE -> new UpdateCommand();
            case CHANGE_FREQUENCY -> new ChangeFrequencyCommand();
            case STOP -> new StopCommand();
            case null -> throw new IllegalStateException("Unexpected value: " + message);
        };
    }
}
