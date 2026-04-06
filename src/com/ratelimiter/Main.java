package com.ratelimiter;

import com.ratelimiter.api.RemoteAPIService;
import com.ratelimiter.exception.RateLimitExceededException;
import com.ratelimiter.model.RateLimiterConfig;
import com.ratelimiter.model.Response;
import com.ratelimiter.proxy.RateLimitedAPIProxy;
import com.ratelimiter.real.RealRemoteAPIService;
import com.ratelimiter.strategy.FixedWindowStrategy;
import com.ratelimiter.strategy.RateLimitStrategy;
import com.ratelimiter.strategy.SlidingWindowStrategy;

public class Main {
    private static final int MAX_REQUESTS = 3;
    private static final int WINDOW_SIZE_SECONDS = 10;
    private static final int REFILL_RATE_PER_SECOND = 1;
    private static final String CLIENT_A = "client-A";
    private static final String CLIENT_B = "client-B";
    private static final String ENDPOINT = "https://api.example.com";
    private static final String REQUEST_PREFIX = "request-";

    public static void main(String[] args) {
        runSingleClientSection("FixedWindowStrategy", new FixedWindowStrategy(createDefaultConfig()));
        runSingleClientSection("SlidingWindowStrategy", new SlidingWindowStrategy(createDefaultConfig()));

        runTwoClientFixedWindowSection();
    }

    private static RateLimiterConfig createDefaultConfig() {
        return new RateLimiterConfig(MAX_REQUESTS, WINDOW_SIZE_SECONDS, REFILL_RATE_PER_SECOND);
    }

    private static void runSingleClientSection(String strategyName, RateLimitStrategy strategy) {
        System.out.println("=== " + strategyName + " ===");
        RemoteAPIService realService = new RealRemoteAPIService(ENDPOINT);
        RemoteAPIService proxyService = new RateLimitedAPIProxy(realService, strategy);

        int firstCallNumber = 1;
        int lastCallNumber = 5;
        int callNumber = firstCallNumber;
        while (callNumber <= lastCallNumber) {
            try {
                Response response = proxyService.call(CLIENT_A, REQUEST_PREFIX + callNumber);
                System.out.println("Call " + callNumber + " succeeded: " + response.getBody());
            } catch (RateLimitExceededException exception) {
                System.out.println("Call " + callNumber + " blocked: " + exception.getMessage());
            }
            callNumber = callNumber + 1;
        }
    }

    private static void runTwoClientFixedWindowSection() {
        System.out.println("=== Two clients, FixedWindow ===");
        int twoClientMaxRequests = 2;
        RateLimiterConfig twoClientConfig = new RateLimiterConfig(twoClientMaxRequests, WINDOW_SIZE_SECONDS, REFILL_RATE_PER_SECOND);
        RateLimitStrategy strategy = new FixedWindowStrategy(twoClientConfig);
        RemoteAPIService realService = new RealRemoteAPIService(ENDPOINT);
        RemoteAPIService proxyService = new RateLimitedAPIProxy(realService, strategy);

        String[] interleavedClients = new String[]{CLIENT_A, CLIENT_B, CLIENT_A, CLIENT_B, CLIENT_A, CLIENT_B};
        int callIndex = 0;
        while (callIndex < interleavedClients.length) {
            int callNumber = callIndex + 1;
            String clientId = interleavedClients[callIndex];
            try {
                Response response = proxyService.call(clientId, REQUEST_PREFIX + callNumber);
                System.out.println("Client " + clientId + " call " + callNumber + " succeeded: " + response.getBody());
            } catch (RateLimitExceededException exception) {
                System.out.println("Client " + clientId + " call " + callNumber + " blocked: " + exception.getMessage());
            }
            callIndex = callIndex + 1;
        }
    }
}

