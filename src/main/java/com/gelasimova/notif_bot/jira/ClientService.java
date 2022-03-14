package com.gelasimova.notif_bot.jira;


import com.gelasimova.notif_bot.common.Client;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.gelasimova.notif_bot.common.AuthState.NO_AUTH;

@Service
public class ClientService {
    public static final String CONSUMER_KEY = "consumer_key";
    public static final String PRIVATE_KEY = "private_key";
    public static final String JIRA_HOME = "jira_home";
    public static final String JIRA_REQUEST_URL = "jira_request_url";

    private final static Map<Long, Client> STORAGE = new ConcurrentHashMap<>();

    public final static Map<String, String> DEFAULT_PROPERTY_VALUES = ImmutableMap.<String, String>builder()
            .put(JIRA_HOME, System.getenv("JIRA_HOME"))
            .put(CONSUMER_KEY, System.getenv("JIRA_CONSUMER_KEY"))
            .put(PRIVATE_KEY, System.getenv("JIRA_PRIVATE_KEY"))
            .put(JIRA_REQUEST_URL, System.getenv("JIRA_REQUEST_URL"))
            .build();

    public Client getOrCreate(Long chatId) {
        Client client = STORAGE.get(chatId);
        if (client == null) {
            client = new Client();
            client.setChatId(chatId);
            client.setAuthState(NO_AUTH);
            STORAGE.put(chatId, client);
        }
        return client;
    }
}
