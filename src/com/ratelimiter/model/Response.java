package com.ratelimiter.model;

public class Response {
    private final String clientId;
    private final String body;
    private final int statusCode;

    public Response(String clientId, String body, int statusCode) {
        this.clientId = clientId;
        this.body = body;
        this.statusCode = statusCode;
    }

    public String getClientId() {
        return clientId;
    }

    public String getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

