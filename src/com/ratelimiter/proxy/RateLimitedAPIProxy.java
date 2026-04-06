package com.ratelimiter.proxy;

import com.ratelimiter.api.RemoteAPIService;
import com.ratelimiter.exception.RateLimitExceededException;
import com.ratelimiter.model.Response;
import com.ratelimiter.strategy.RateLimitStrategy;

public class RateLimitedAPIProxy implements RemoteAPIService {
    private static final int RATE_LIMIT_STATUS_CODE = 429;

    private final RemoteAPIService realService;
    private final RateLimitStrategy strategy;

    public RateLimitedAPIProxy(RemoteAPIService realService, RateLimitStrategy strategy) {
        this.realService = realService;
        this.strategy = strategy;
    }

    @Override
    public Response call(String clientId, String request) {
        boolean isAllowed = strategy.isAllowed(clientId);
        if (!isAllowed) {
            throw new RateLimitExceededException("Rate limit exceeded for client " + clientId + ". Try again later. Status: " + RATE_LIMIT_STATUS_CODE);
        }
        return realService.call(clientId, request);
    }
}

