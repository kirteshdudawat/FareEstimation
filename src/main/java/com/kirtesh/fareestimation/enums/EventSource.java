package com.kirtesh.fareestimation.enums;

import java.util.ArrayList;
import java.util.List;

public enum EventSource {
    CSV("CSV");

    private static List<String> eventSourceList = new ArrayList<>();
    private String _eventSource;

    EventSource(String _eventSource) {
        this._eventSource = _eventSource;
    }

    public String getEventSource() {
        return _eventSource;
    }

    static {
        for(EventSource eventSource : EventSource.values()) {
            eventSourceList.add(eventSource._eventSource);
        }
    }

    public static List<String> getEventSourceList() {
        List<String> eventSourceList = new ArrayList<>();
        eventSourceList.addAll(eventSourceList);
        return eventSourceList;
    }

    public static boolean isValidEventSource(String eventSource) {
        eventSource = eventSource.toUpperCase();
        return eventSourceList.contains(eventSource);
    }
}