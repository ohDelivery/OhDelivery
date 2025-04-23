package com.ohdelivery.service.delivery.application.observer;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class BroadcasterManager {

    private final ConcurrentHashMap<String, LocationBroadcaster> broadcasterMap = new ConcurrentHashMap<>();

    public void addBroadcaster(String riderId) {
        broadcasterMap.putIfAbsent(riderId, new LocationBroadcaster());
    }

    public LocationBroadcaster getBroadcaster(String riderId) {
        return broadcasterMap.computeIfAbsent(riderId, id -> new LocationBroadcaster());
    }

    public void removeBroadcaster(String riderId) {
        LocationBroadcaster broadcaster = broadcasterMap.get(riderId);
        if (broadcaster != null && broadcaster.isEmpty()) {
            broadcasterMap.remove(riderId);
        }
    }
}
