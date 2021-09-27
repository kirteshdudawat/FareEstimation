package com.kirtesh.fareestimation.factory;

import com.kirtesh.fareestimation.enums.EventSource;
import com.kirtesh.fareestimation.events.EventReader;
import com.kirtesh.fareestimation.events.reader.csv.CsvEventReader;

public class RoutingFactory {

    private String csvPath;
    private EventReader csvEventReader = null;

    public RoutingFactory(String csvPath) {
        this.csvPath = csvPath;
    }

    public EventReader getEventReader(EventSource eventSource) {
        switch (eventSource) {
            case CSV:
                if (csvEventReader == null) {
                    synchronized (this) {
                        if (csvEventReader == null) {
                            csvEventReader = new CsvEventReader(csvPath);
                        }
                    }
                }
                return csvEventReader;
        }
        return null;
    }

}
