package com.kirtesh.fareestimation.workflow.farecalculator;

import com.kirtesh.fareestimation.cache.JvmCache;
import com.kirtesh.fareestimation.cache.JvmPreviousEvent;
import com.kirtesh.fareestimation.enums.StatusCodes;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.utility.FareCalculatorUtility;
import com.kirtesh.fareestimation.utility.LocationUtility;
import com.kirtesh.fareestimation.workflow.framework.WorkflowStep;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowStepResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Optional;

public class EventBasedFareCalculatorStep implements WorkflowStep<EventMetadata> {

    private final static Logger logger = LogManager.getLogger(EventBasedFareCalculatorStep.class);

    private String name = "EventBasedFareCalculatorStep";

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public WorkflowStepResponse<EventMetadata> process(EventMetadata metadata) {

        if (metadata.isNewRideEvent()) {
            metadata.setTotalFareIncludingCurrentEvent(JvmCache.startingBaseFare);
            return generateResponse(metadata, StatusCodes.SUCCESS);
        }

        EventMetadata previousLocation = JvmPreviousEvent.getEventMetadata();
        double distanceTravelledInMeters = LocationUtility.haversineDistanceInMeters(
                previousLocation.getLatitude(),
                previousLocation.getLongitude(),
                metadata.getLatitude(),
                metadata.getLongitude());

        long timeInSeconds = Math.abs(metadata.getEpochTime() - previousLocation.getEpochTime());

        double fareTillCurrentEvent = previousLocation.getTotalFareIncludingCurrentEvent() + FareCalculatorUtility.calculateFare(
                distanceTravelledInMeters,
                metadata.getLocalTime(),
                metadata.getSpeedInMetersPerSecond(),
                timeInSeconds);

        metadata.setTotalFareIncludingCurrentEvent(fareTillCurrentEvent);
        return generateResponse(metadata, StatusCodes.SUCCESS);
    }

    @Override
    public Optional<String> nextStep(EventMetadata eventMetadata, StatusCodes status) {
        if (status.getStatusCode().equals(StatusCodes.SUCCESS.getStatusCode())) {
            return Optional.of("UpdateJvmCache");
        }

        logger.info(eventMetadata.toString());
        return Optional.of("SkippedFailedFileCreationStep");
    }
}
