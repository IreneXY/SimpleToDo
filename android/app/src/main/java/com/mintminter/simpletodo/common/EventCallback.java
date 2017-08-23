package com.mintminter.simpletodo.common;

/**
 * Created by Irene on 8/21/17.
 */

public interface EventCallback {
    void addEvent(Event event);
    void deleteEvent(Event event);
    void updateEvent(Event oldEvent, Event newEvent);
    int countAllEvents();
}
