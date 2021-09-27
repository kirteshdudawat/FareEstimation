package com.kirtesh.fareestimation.workflow.farecalculator;

import com.kirtesh.fareestimation.cache.JvmCache;
import com.kirtesh.fareestimation.cache.JvmPreviousEvent;
import com.kirtesh.fareestimation.enums.StatusCodes;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.utility.LocationUtility;
import com.kirtesh.fareestimation.workflow.framework.WorkflowStep;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowStepResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Optional;

public class CalculateSpeedStep implements WorkflowStep<EventMetadata> {

    private final static Logger logger = LogManager.getLogger(CalculateSpeedStep.class);

    private String name = "CalculateSpeedStep";

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public WorkflowStepResponse<EventMetadata> process(EventMetadata metadata) {

        if (metadata.isNewRideEvent()) {
            return generateResponse(metadata, StatusCodes.SUCCESS);
        }

        EventMetadata previousLocation = JvmPreviousEvent.getEventMetadata();
        double distanceTravelledInMeters = LocationUtility.haversineDistanceInMeters(
                previousLocation.getLatitude(),
                previousLocation.getLongitude(),
                metadata.getLatitude(),
                metadata.getLongitude());

        long timeInSeconds = Math.abs(metadata.getEpochTime() - previousLocation.getEpochTime());

        double speed = timeInSeconds > 0 ? (distanceTravelledInMeters / timeInSeconds) : Long.MAX_VALUE;

        metadata.setSpeedInMetersPerSecond(speed);
        return generateResponse(metadata, StatusCodes.SUCCESS);
    }

    @Override
    public Optional<String> nextStep(EventMetadata eventMetadata, StatusCodes status) {
        if (status.getStatusCode().equals(StatusCodes.SUCCESS.getStatusCode())
                && eventMetadata.getSpeedInMetersPerSecond() <= JvmCache.maxAllowedSpeedInMeterPerSeconds) {
            return Optional.of("EventBasedFareCalculatorStep");
        }

        return Optional.of("SkippedFailedFileCreationStep");
    }
}
