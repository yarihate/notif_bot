package com.gelasimova.notif_bot.jira;

import com.gelasimova.notif_bot.common.Client;
import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

import static com.gelasimova.notif_bot.jira.ClientService.*;

@Component
public class OAuthClient {
    private final Map<Command, Function<Client, Optional<Exception>>> actionHandlers;
    private final JiraOAuthClient jiraOAuthClient;

    @Autowired
    public OAuthClient(JiraOAuthClient jiraOAuthClient) {
        this.jiraOAuthClient = jiraOAuthClient;

        actionHandlers = ImmutableMap.<Command, Function<Client, Optional<Exception>>>builder()
                .put(Command.REQUEST_TOKEN, this::handleGetRequestTokenAction)
                .put(Command.ACCESS_TOKEN, this::handleGetAccessToken)
                .put(Command.REQUEST, this::handleGetRequest)
                .build();
    }

    /**
     * Executes action (if found) with  given lists of arguments
     */
    public void execute(Command action, Client client) {
        actionHandlers.getOrDefault(action, this::handleUnknownCommand)
                .apply(client)
                .ifPresent(Throwable::printStackTrace);
    }

    private Optional<Exception> handleUnknownCommand(Client client) {
        System.out.println("Command not supported. Only " + Command.names() + " are supported.");
        return Optional.empty();
    }

    /**
     * Gets request token and saves it to properties file
     */
    private Optional<Exception> handleGetRequestTokenAction(Client client) {
        Map<String, String> properties = DEFAULT_PROPERTY_VALUES;
        try {
            OAuthAuthorizeTemporaryTokenUrl authorizeTemporaryToken = jiraOAuthClient.getAndAuthorizeTemporaryToken(properties.get(CONSUMER_KEY), properties.get(PRIVATE_KEY));
            client.setRequestToken(authorizeTemporaryToken.temporaryToken);
            client.setAuthUrl(authorizeTemporaryToken.toString());
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    /**
     * Gets access token and saves it to properties file
     */
    private Optional<Exception> handleGetAccessToken(Client client) {
        Map<String, String> properties = DEFAULT_PROPERTY_VALUES;
        String tmpToken = client.getRequestToken();
        String secret = client.getSecret();

        try {
            String accessToken = jiraOAuthClient.getAccessToken(tmpToken, secret, properties.get(CONSUMER_KEY), properties.get(PRIVATE_KEY));
            client.setAccessToken(accessToken);
            client.setSecret(secret);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    /**
     * Makes request to JIRA to provided url and prints response contect
     */
    private Optional<Exception> handleGetRequest(Client client) {
        Map<String, String> properties = DEFAULT_PROPERTY_VALUES;
        String tmpToken = client.getAccessToken();
        String secret = client.getSecret();
        String url = DEFAULT_PROPERTY_VALUES.get(JIRA_REQUEST_URL);
        try {
            OAuthParameters parameters = jiraOAuthClient.getParameters(tmpToken, secret, properties.get(CONSUMER_KEY), properties.get(PRIVATE_KEY));
            HttpResponse response = getResponseFromUrl(parameters, new GenericUrl(url));
            client.setJiraResponse(response.parseAsString());
            // parseResponse(response);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    /**
     * Prints response content
     * if response content is valid JSON it prints it in 'pretty' format
     */
    private void parseResponse(HttpResponse response) throws IOException {
        Scanner s = new Scanner(response.getContent()).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        try {
            JSONObject jsonObj = new JSONObject(result);
            System.out.println(jsonObj.toString(2));
        } catch (Exception e) {
            System.out.println(result);
        }
    }

    /**
     * Authanticates to JIRA with given OAuthParameters and makes request to url
     *
     * @param parameters
     * @param jiraUrl
     * @return
     * @throws IOException
     */
    private static HttpResponse getResponseFromUrl(OAuthParameters parameters, GenericUrl jiraUrl) throws IOException {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(parameters);
        HttpRequest request = requestFactory.buildGetRequest(jiraUrl);
        return request.execute();
    }
}
