package com.ohdelivery.service.delivery.application.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocationBroadcaster implements Subject {

    private static final List<Observer> observers = new CopyOnWriteArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String location) {
        for (Observer observer : observers) {
            log.info("Observer Location: {}", location);
            observer.update(location);
        }
    }

    public boolean isEmpty() {
        return observers.isEmpty();
    }
}
