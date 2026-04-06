package com.ratelimiter.strategy;

import com.ratelimiter.model.RateLimiterConfig;

import java.util.HashMap;
import java.util.Map;

public class FixedWindowStrategy implements RateLimitStrategy {
    private static final long MILLIS_PER_SECOND = 1000L;

    private final RateLimiterConfig config;
    private final Map<String, Integer> requestCounts;
    private final Map<String, Long> windowStartTimes;

    public FixedWindowStrategy(RateLimiterConfig config) {
        this.config = config;
        this.requestCounts = new HashMap<String, Integer>();
        this.windowStartTimes = new HashMap<String, Long>();
    }

    @Override
    public boolean isAllowed(String clientId) {
        long now = System.currentTimeMillis();

        if (!windowStartTimes.containsKey(clientId)) {
            windowStartTimes.put(clientId, now);
            requestCounts.put(clientId, 0);
        }

        long elapsed = now - windowStartTimes.get(clientId);
        long windowMillis = config.getWindowSizeSeconds() * MILLIS_PER_SECOND;
        if (elapsed >= windowMillis) {
            windowStartTimes.put(clientId, now);
            requestCounts.put(clientId, 0);
        }

        int currentCount = requestCounts.get(clientId);
        if (currentCount >= config.getMaxRequests()) {
            return false;
        }

        requestCounts.put(clientId, currentCount + 1);
        return true;
    }

    @Override
    public RateLimiterConfig getConfig() {
        return config;
    }
}

