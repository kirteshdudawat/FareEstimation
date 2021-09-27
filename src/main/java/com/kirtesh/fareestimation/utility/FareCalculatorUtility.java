package com.kirtesh.fareestimation.utility;

import com.kirtesh.fareestimation.cache.JvmCache;

import java.time.LocalTime;

public class FareCalculatorUtility {

    public static double calculateFare(double distanceTravelledInMeters, LocalTime localTime, double speedInMeterPerSeconds, long timeDifferenceInSeconds) {

        if (speedInMeterPerSeconds <= JvmCache.minSpeedInMeterPerSeconds) {
            return JvmCache.idleFarePerSeconds * timeDifferenceInSeconds;
        }

        if (TimeUtility.isBetween(JvmCache.nightTimeStart, JvmCache.nightTimeEnd, localTime)) {
            return distanceTravelledInMeters * JvmCache.nightFarePerMeter;
        } else {
            return distanceTravelledInMeters * JvmCache.dayFarePerMeter;
        }
    }

}
