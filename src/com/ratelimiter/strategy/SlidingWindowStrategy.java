package com.ratelimiter.strategy;

import com.ratelimiter.model.RateLimiterConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SlidingWindowStrategy implements RateLimitStrategy {
    private static final long MILLIS_PER_SECOND = 1000L;

    private final RateLimiterConfig config;
    private final Map<String, List<Long>> requestTimestamps;

    public SlidingWindowStrategy(RateLimiterConfig config) {
        this.config = config;
        this.requestTimestamps = new HashMap<String, List<Long>>();
    }

    @Override
    public boolean isAllowed(String clientId) {
        if (!requestTimestamps.containsKey(clientId)) {
            requestTimestamps.put(clientId, new ArrayList<Long>());
        }

        long now = System.currentTimeMillis();
        List<Long> timestampsForClient = requestTimestamps.get(clientId);
        long windowMillis = config.getWindowSizeSeconds() * MILLIS_PER_SECOND;

        Iterator<Long> iterator = timestampsForClient.iterator();
        while (iterator.hasNext()) {
            long timestamp = iterator.next();
            if (now - timestamp > windowMillis) {
                iterator.remove();
            }
        }

        int currentCount = timestampsForClient.size();
        if (currentCount >= config.getMaxRequests()) {
            return false;
        }

        timestampsForClient.add(now);
        return true;
    }

    @Override
    public RateLimiterConfig getConfig() {
        return config;
    }
}

