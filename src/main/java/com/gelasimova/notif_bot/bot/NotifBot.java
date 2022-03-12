package com.gelasimova.notif_bot.bot;

import com.gelasimova.notif_bot.bot.command.CommandFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class NotifBot extends TelegramLongPollingBot {
    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            Long chatId = update.getMessage().getChatId();

            new CommandFactory().createCommand(message).execute(chatId);


//            SendMessage sm = new SendMessage();
//            sm.setChatId(String.valueOf(chatId));
//            sm.setText(message);
//
//            try {
//                execute(sm);
//            } catch (TelegramApiException e) {
//                //todo add logging to the project.
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
