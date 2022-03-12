package com.gelasimova.notif_bot.bot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public abstract class Command {
    public abstract SendMessage execute(Long chatId);
}
