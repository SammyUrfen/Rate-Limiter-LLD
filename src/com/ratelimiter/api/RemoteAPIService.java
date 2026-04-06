package com.ratelimiter.api;

import com.ratelimiter.model.Response;

public interface RemoteAPIService {
    Response call(String clientId, String request);
}

