package com.ratelimiter.strategy;

import com.ratelimiter.model.RateLimiterConfig;

public interface RateLimitStrategy {
    boolean isAllowed(String clientId);

    RateLimiterConfig getConfig();
}

