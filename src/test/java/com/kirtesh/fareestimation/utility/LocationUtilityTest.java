package com.kirtesh.fareestimation.utility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationUtilityTest {

    @DisplayName("Test LocationUtility.verifyLocation() for a geolocation")
    @Test
    void testVerifyLocation() {
        assertEquals(true, LocationUtility.verifyLocation(90.0, 90.0));
        assertEquals(true, LocationUtility.verifyLocation(-90.0, 90.0));
        assertEquals(true, LocationUtility.verifyLocation(88.0, 180.0));
        assertEquals(true, LocationUtility.verifyLocation(88.0, -180.0));
        assertEquals(false, LocationUtility.verifyLocation(-90.01, 90.0));
        assertEquals(false, LocationUtility.verifyLocation(90.01, 90.0));
        assertEquals(false, LocationUtility.verifyLocation(-88.0, 180.01));
        assertEquals(false, LocationUtility.verifyLocation(88.0, -180.01));
    }

    @DisplayName("Test LocationUtility.haversineDistanceInMeters() for a geolocation")
    @Test
    void testHaversineDistanceInMeters() {
        // India Gate, New Delhi (Lat -> 28.612894, Long -> 77.229446)
        // Red Fort, New Delhi (Lat -> 28.656473, Long -> 77.242943)

        double distanceInMeters = LocationUtility.haversineDistanceInMeters(28.612894, 77.229446, 28.656473, 77.242943);
        assertTrue(distanceInMeters > 5021 && distanceInMeters < 5022);
    }
}
