package com.kirtesh.fareestimation.utility;

import com.kirtesh.fareestimation.FareEstimation;
import com.kirtesh.fareestimation.enums.ErrorCodes;
import com.kirtesh.fareestimation.exception.ServiceException;
import net.iakovlev.timeshape.TimeZoneEngine;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

public class TimeUtility {
    public final static Logger logger = LogManager.getLogger(TimeUtility.class);
    private static final TimeZoneEngine engine = TimeZoneEngine.initialize();

    // Ref: https://github.com/RomanIakovlev/timeshape
    public static ZoneId getTimeZoneFromGeoCoordinates(double latitude, double longitude) throws ServiceException {
        return engine.query(latitude, longitude).orElseThrow(() -> new ServiceException(ErrorCodes.UNABLE_TO_FETCH_TIMEZONE));
    }

    public static LocalTime getLocalDateTime(long timeInEpochSeconds, ZoneId zoneId) {
        return Instant.ofEpochSecond(timeInEpochSeconds).atZone(zoneId).toLocalTime();
    }

    public static boolean isBetween(LocalTime startTime, LocalTime endTime, LocalTime timeToBeCompared) {
        if (timeToBeCompared == null) {
            logger.error("Null passed on in timeToBeCompared");
            return false;
        }

        return (timeToBeCompared.compareTo(startTime) >= 0) && (endTime.compareTo(timeToBeCompared) >= 0);
    }
}
