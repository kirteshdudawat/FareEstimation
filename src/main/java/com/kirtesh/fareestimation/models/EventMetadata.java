package com.kirtesh.fareestimation.models;

import java.time.LocalTime;

public class EventMetadata {

    private String eventString;
    private long rideId;
    private double latitude;
    private double longitude;
    private long epochTime;
    private LocalTime localTime;
    private double totalFareIncludingCurrentEvent = 1.30;
    private boolean isNewRideEvent = false;
    private double speedInMetersPerSecond = 0.0;

    public EventMetadata() { }

    public EventMetadata(String eventString) {
        this.eventString = eventString;
    }

    public String getEventString() {
        return eventString;
    }

    public void setEventString(String eventString) {
        this.eventString = eventString;
    }

    public long getRideId() {
        return rideId;
    }

    public void setRideId(long rideId) {
        this.rideId = rideId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(long epochTime) {
        this.epochTime = epochTime;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public double getTotalFareIncludingCurrentEvent() {
        return totalFareIncludingCurrentEvent;
    }

    public void setTotalFareIncludingCurrentEvent(double totalFareIncludingCurrentEvent) {
        this.totalFareIncludingCurrentEvent = totalFareIncludingCurrentEvent;
    }

    public boolean isNewRideEvent() {
        return isNewRideEvent;
    }

    public void setNewRideEvent(boolean newRideEvent) {
        isNewRideEvent = newRideEvent;
    }

    public double getSpeedInMetersPerSecond() {
        return speedInMetersPerSecond;
    }

    public void setSpeedInMetersPerSecond(double speedInMetersPerSecond) {
        this.speedInMetersPerSecond = speedInMetersPerSecond;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("EventMetadata{");
        sb.append("eventString='").append(eventString).append('\'');
        sb.append(", rideId=").append(rideId);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", epocTime=").append(epochTime);
        sb.append(", localTime=").append(localTime);
        sb.append(", totalFareIncludingCurrentEvent=").append(totalFareIncludingCurrentEvent);
        sb.append(", isNewRideEvent=").append(isNewRideEvent);
        sb.append(", speedInMetersPerSecond=").append(speedInMetersPerSecond);
        sb.append('}');
        return sb.toString();
    }
}
