package com.gelasimova.notif_bot.bot;

import com.gelasimova.notif_bot.common.Client;
import com.gelasimova.notif_bot.jira.ClientService;
import com.gelasimova.notif_bot.jira.OAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.gelasimova.notif_bot.bot.CommandType.getCommandTypeByName;
import static com.gelasimova.notif_bot.common.AuthState.AUTHENTICATED;
import static com.gelasimova.notif_bot.common.AuthState.SENT_AUTH_LINK;
import static com.gelasimova.notif_bot.jira.Command.*;

@Component
public class NotifBot extends TelegramLongPollingBot {
    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    private final ClientService clientService;
    private final OAuthClient oAuthClient;

    @Autowired
    public NotifBot(ClientService clientService, OAuthClient oAuthClient) {
        this.clientService = clientService;
        this.oAuthClient = oAuthClient;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            Long chatId = update.getMessage().getChatId();

            SendMessage sm = foo(clientService.getOrCreate(chatId), message);

            try {
                execute(sm);
            } catch (TelegramApiException e) {

                //todo add logging to the project.
                e.printStackTrace();
            }
        }
    }

    private SendMessage foo(Client client, String message) {
        return switch (getCommandTypeByName(message)) {
            case START -> start(client);
            case UPDATE -> update(client);
//            case CHANGE_FREQUENCY -> change(client);
//            case STOP -> stop(client);
            case null -> getAccessToken(message, client);
            default -> throw new IllegalStateException("Unexpected value: " + getCommandTypeByName(message));
        };
    }


    private SendMessage start(Client client) {
        oAuthClient.execute(REQUEST_TOKEN, client);
        client.setAuthState(SENT_AUTH_LINK);
        return new SendMessage(String.valueOf(client.getChatId()), String.format("Retrieve request token. Go to %s to authorize it.", client.getAuthUrl()));
    }


    private SendMessage update(Client client) {
        oAuthClient.execute(REQUEST, client);
        return new SendMessage(String.valueOf(client.getChatId()), client.getJiraResponse());
    }


    private SendMessage getAccessToken(String message, Client client) {
        if (messageContainsSecretCode(message, client)) {
            client.setSecret(message);
            oAuthClient.execute(ACCESS_TOKEN, client);
            client.setAuthState(AUTHENTICATED);
            return new SendMessage(String.valueOf(client.getChatId()), "Authorization success");
        } else return null;
    }

    private boolean messageContainsSecretCode(String message, Client client) {
        return !message.isBlank() && client.getAuthState().equals(SENT_AUTH_LINK);
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
