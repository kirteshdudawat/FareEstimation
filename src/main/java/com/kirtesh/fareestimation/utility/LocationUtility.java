package com.kirtesh.fareestimation.utility;

public class LocationUtility {

    public static boolean verifyLocation(double latitude, double longitude) {
        if (latitude < -90.0 || latitude > 90.0) {
            return false;
        }

        if (longitude < -180.0 || longitude > 180.0) {
            return false;
        }

        return true;
    }

    public static double haversineDistanceInMeters(double lat1, double lon1, double lat2, double lon2)
    {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        // Converting distance from Km to meters
        return rad * c * 1000;
    }
}
