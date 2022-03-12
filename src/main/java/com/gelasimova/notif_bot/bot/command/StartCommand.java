package com.gelasimova.notif_bot.bot.command;

import com.gelasimova.notif_bot.oauth_client.JiraOAuthClient;
import com.gelasimova.notif_bot.oauth_client.OAuthClient;
import com.gelasimova.notif_bot.oauth_client.PropertiesClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Collections;

import static com.gelasimova.notif_bot.oauth_client.Command.REQUEST_TOKEN;

public class StartCommand extends Command {
    @Override
    public SendMessage execute(Long chatId) {
        PropertiesClient propertiesClient = new PropertiesClient();
        JiraOAuthClient jiraOAuthClient = new JiraOAuthClient(propertiesClient);

        OAuthClient oAuthClient = new OAuthClient(propertiesClient, jiraOAuthClient);
        oAuthClient.execute(REQUEST_TOKEN, Collections.emptyList());


        SendMessage sm = new SendMessage();
        sm.setChatId(String.valueOf(chatId));
        sm.setText(oAuthClient.getAuthorizeUrl());
        return sm;
        //for access token
        //  List<String> argumentsWithoutFirst = Arrays.asList(args).subList(1, args.length);
        //todo run scheduler
    }
}
