package com.redue.newsflow.component;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionIpStore {
    private final ConcurrentHashMap<String, String> sessionIpMap = new ConcurrentHashMap<>();

    public void saveIp(String sessionId, String ip) {
        sessionIpMap.put(sessionId, ip);
    }

    public String getIp(String sessionId) {
        return sessionIpMap.get(sessionId);
    }

    public void removeIp(String sessionId) {
        sessionIpMap.remove(sessionId);
    }
}
