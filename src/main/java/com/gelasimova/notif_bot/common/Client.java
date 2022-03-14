package com.gelasimova.notif_bot.common;

public class Client {
    private Long chatId;
    private String requestToken;
    private String authUrl;
    private String accessToken;
    private String secret;
    private AuthState authState;
    private String jiraResponse;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public AuthState getAuthState() {
        return authState;
    }

    public void setAuthState(AuthState authState) {
        this.authState = authState;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getJiraResponse() {
        return jiraResponse;
    }

    public void setJiraResponse(String jiraResponse) {
        this.jiraResponse = jiraResponse;
    }
}
