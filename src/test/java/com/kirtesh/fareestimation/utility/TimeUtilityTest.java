package com.kirtesh.fareestimation.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.kirtesh.fareestimation.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.ZoneId;

public class TimeUtilityTest {

    @DisplayName("Test TimeUtility.getTimeZoneFromGeoCoordinates() for New Delhi Coordinates")
    @Test
    void testGetZoneId() throws ServiceException {
        // India Gate, New Delhi, India (lat -> 28.612894, long -> 77.229446)
        ZoneId zoneId = TimeUtility.getTimeZoneFromGeoCoordinates(28.612894, 77.229446);
        assertEquals(ZoneId.of("Asia/Kolkata"), zoneId);
    }

    @DisplayName("Test TimeUtility.getTimeZoneFromGeoCoordinates() for exception incase of invalid coordinates")
    @Test
    void testExpectedExceptionForInvalid() {
        Assertions.assertThrows(ServiceException.class, () -> {
           TimeUtility.getTimeZoneFromGeoCoordinates(100.612894, 200.229446);
        });
    }

    @DisplayName("Test TimeUtility.getLocalDateTime() from Epoc and zoneId")
    @Test
    void testGetLocalTime() {
        LocalTime expectedTime = LocalTime.of(16,33,04);

        LocalTime epochTime = TimeUtility.getLocalDateTime(1405594984, ZoneId.of("Asia/Kolkata"));
        assertEquals(expectedTime, epochTime);
    }

    @DisplayName("Test TimeUtility.isBetween() for a timestamp")
    @Test
    void testIsBetweenWithNull() {
        LocalTime nightTimeStart = LocalTime.of(00, 00, 00);
        LocalTime nightTimeEnd = LocalTime.of(04, 59, 59);

        assertEquals(false, TimeUtility.isBetween(nightTimeStart, nightTimeEnd, null));
    }

    @DisplayName("Test TimeUtility.isBetween() for a timestamp")
    @Test
    void testIsBetweenWithValidCase() {
        LocalTime nightTimeStart = LocalTime.of(00, 00, 00);
        LocalTime nightTimeEnd = LocalTime.of(04, 59, 59);

        LocalTime timeToTest = LocalTime.of(00, 00, 00);
        LocalTime timeToTest1 = LocalTime.of(02, 00, 00);
        LocalTime timeToTest2 = LocalTime.of(04, 59, 59);
        LocalTime timeToTest3 = LocalTime.of(05, 00, 00);
        LocalTime timeToTest4 = LocalTime.of(23, 29, 59);

        assertEquals(true, TimeUtility.isBetween(nightTimeStart, nightTimeEnd, timeToTest));
        assertEquals(true, TimeUtility.isBetween(nightTimeStart, nightTimeEnd, timeToTest1));
        assertEquals(true, TimeUtility.isBetween(nightTimeStart, nightTimeEnd, timeToTest2));
        assertEquals(false, TimeUtility.isBetween(nightTimeStart, nightTimeEnd, timeToTest3));
        assertEquals(false, TimeUtility.isBetween(nightTimeStart, nightTimeEnd, timeToTest4));
    }
}
