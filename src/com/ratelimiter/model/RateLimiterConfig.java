package com.ratelimiter.model;

public class RateLimiterConfig {
    private final int maxRequests;
    private final int windowSizeSeconds;
    private final int refillRatePerSecond;

    public RateLimiterConfig(int maxRequests, int windowSizeSeconds, int refillRatePerSecond) {
        this.maxRequests = maxRequests;
        this.windowSizeSeconds = windowSizeSeconds;
        this.refillRatePerSecond = refillRatePerSecond;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public int getWindowSizeSeconds() {
        return windowSizeSeconds;
    }

    public int getRefillRatePerSecond() {
        return refillRatePerSecond;
    }
}

