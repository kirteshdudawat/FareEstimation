package com.kirtesh.fareestimation.cache;

import com.kirtesh.fareestimation.enums.ErrorCodes;
import com.kirtesh.fareestimation.exception.ServiceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.time.LocalTime;
import java.util.Properties;

public class JvmCache {

    private final static Logger logger = LogManager.getLogger(JvmCache.class);

    public static LocalTime nightTimeStart;
    public static LocalTime nightTimeEnd;

    public static double dayFarePerMeter;
    public static double nightFarePerMeter;
    public static double idleFarePerSeconds;
    public static double minSpeedInMeterPerSeconds;

    public static double maxAllowedSpeedInMeterPerSeconds;
    public static double startingBaseFare;
    public static double minimumRideFare;

    public static void loadCache() throws ServiceException {
        try (InputStream input = JvmCache.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                logger.error("Sorry, unable to find application.properties");
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            // Set configurations
            JvmCache.nightTimeStart = LocalTime.parse(prop.getProperty("nightTimeStart"));
            JvmCache.nightTimeEnd = LocalTime.parse(prop.getProperty("nightTimeEnd"));
            JvmCache.dayFarePerMeter = Double.parseDouble(prop.getProperty("dayFarePerMeter"));
            JvmCache.nightFarePerMeter = Double.parseDouble(prop.getProperty("nightFarePerMeter"));
            JvmCache.idleFarePerSeconds = Double.parseDouble(prop.getProperty("idleFarePerSeconds"));
            JvmCache.minSpeedInMeterPerSeconds = Double.parseDouble(prop.getProperty("minSpeedInMeterPerSeconds"));
            JvmCache.maxAllowedSpeedInMeterPerSeconds = Double.parseDouble(prop.getProperty("maxAllowedSpeedInMeterPerSeconds"));
            JvmCache.startingBaseFare = Double.parseDouble(prop.getProperty("startingBaseFare"));
            JvmCache.minimumRideFare = Double.parseDouble(prop.getProperty("minimumRideFare"));
        } catch (Exception ex) {
            logger.error("Exception occurred during configuration load, Please fix and retry.");
            throw new ServiceException(ErrorCodes.INVALID_CONFIG_FILE);
        }
    }

}
