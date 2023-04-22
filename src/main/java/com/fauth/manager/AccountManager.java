package com.fauth.manager;

import com.fauth.FAuth;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class AccountManager {
    private final Map<String, String> accountCache = new HashMap<>();

    public void addToCache(String username) {
        synchronized (accountCache) {
            accountCache.put(username, username);
        }
    }

    public void removeFromCache(String username) {
        synchronized (accountCache) {
            accountCache.remove(username);
        }
    }

    public boolean isCached(String username) {
        synchronized (accountCache) {
            return accountCache.containsKey(username);
        }
    }
}
