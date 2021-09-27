package com.kirtesh.fareestimation.cache;

import com.kirtesh.fareestimation.models.EventMetadata;

public class JvmPreviousEvent {
    private static EventMetadata metadata = null;

    public static void setEventMetadata(EventMetadata _eventMetadata) {
        metadata = _eventMetadata;
    }

    public static EventMetadata getEventMetadata() {
        return metadata;
    }

    public static boolean isEmpty() {
        return metadata == null;
    }

    public static boolean isRideProcessedPreviously(long rideId) {
        if (isEmpty()) {
            return false;
        }

        return metadata.getRideId() == rideId;
    }

    public static void resetCache() {
        metadata = null;
    }
}
