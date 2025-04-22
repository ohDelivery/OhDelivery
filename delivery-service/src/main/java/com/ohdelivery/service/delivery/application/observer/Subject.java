package com.ohdelivery.service.delivery.application.observer;

public interface Subject {

    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(String message);
}
