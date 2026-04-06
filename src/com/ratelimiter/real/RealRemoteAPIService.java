package com.ratelimiter.real;

import com.ratelimiter.api.RemoteAPIService;
import com.ratelimiter.model.Response;

public class RealRemoteAPIService implements RemoteAPIService {
    private final String endpoint;

    public RealRemoteAPIService(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public Response call(String clientId, String request) {
        System.out.println("Calling real API at " + endpoint + " for client " + clientId + " with request: " + request);
        return new Response(clientId, "OK from " + endpoint, 200);
    }
}

