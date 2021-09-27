package com.kirtesh.fareestimation.utility;

import com.kirtesh.fareestimation.cache.JvmCache;
import com.kirtesh.fareestimation.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FareCalculatorUtilityTest {

    @DisplayName("Test FareCalculatorUtility.calculateFare() should return idle fair if speed is less than 10 KM / hr.")
    @Test
    void testCalculateFareForIdleHour() throws ServiceException {
        JvmCache.loadCache();

        LocalTime nightTime = LocalTime.of(02, 00, 00);
        LocalTime dayTime = LocalTime.of(14, 00, 00);

        double calculatedNightFare = FareCalculatorUtility.calculateFare(5000, nightTime, 2.77777777778, 1800);
        double calculatedDayFare = FareCalculatorUtility.calculateFare(5000, dayTime, 2.77777777778, 1800);

        assertTrue(calculatedDayFare > 5.94 && calculatedDayFare < 5.95);
        assertTrue(calculatedNightFare > 5.94 && calculatedNightFare < 5.95);
    }

    @DisplayName("Test FareCalculatorUtility.calculateFare() should return actual fair.")
    @Test
    void testCalculateFareForNightHour() throws ServiceException {
        JvmCache.loadCache();

        LocalTime nightTime = LocalTime.of(02, 00, 00);
        LocalTime dayTime = LocalTime.of(14, 00, 00);
        LocalTime dayTime1 = LocalTime.of(05, 00, 00);

        double calculatedNightFare = FareCalculatorUtility.calculateFare(20000, nightTime, 11.1111, 1800);
        double calculatedDayFare = FareCalculatorUtility.calculateFare(20000, dayTime, 11.1111, 1800);
        double calculatedDayFare1 = FareCalculatorUtility.calculateFare(20000, dayTime1, 11.1111, 1800);

        assertTrue(calculatedDayFare > 14.79 && calculatedDayFare < 14.80);
        assertTrue(calculatedDayFare1 > 14.79 && calculatedDayFare1 < 14.80);
        assertEquals(26.0, calculatedNightFare);
    }
}
