package com.kirtesh.fareestimation.workflow.farecalculator;

import com.kirtesh.fareestimation.enums.StatusCodes;
import com.kirtesh.fareestimation.exception.ServiceException;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.utility.TimeUtility;
import com.kirtesh.fareestimation.workflow.framework.WorkflowStep;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowStepResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

public class ParseDateTimeStep implements WorkflowStep<EventMetadata> {
    private String name = "ParseDateTimeStep";
    private final static Logger logger = LogManager.getLogger(ParseDateTimeStep.class);

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public WorkflowStepResponse<EventMetadata> process(EventMetadata metadata) {
        try {
            ZoneId zoneId = TimeUtility.getTimeZoneFromGeoCoordinates(metadata.getLatitude(), metadata.getLongitude());
            LocalTime time = TimeUtility.getLocalDateTime(metadata.getEpochTime(), zoneId);
            metadata.setLocalTime(time);
            return generateResponse(metadata, StatusCodes.SUCCESS);
        } catch (ServiceException e) {
            logger.error("Exception occurred in ParseDateTimeStep, for event " + metadata.getEventString(), e);
            return generateResponse(metadata, StatusCodes.UNABLE_TO_FETCH_ZONE_ID);
        }
    }

    @Override
    public Optional<String> nextStep(EventMetadata eventMetadata, StatusCodes status) {
        if (status.getStatusCode().equals(StatusCodes.SUCCESS.getStatusCode())) {
            return Optional.of("IsNewRideStep");
        }

        return Optional.of("SkippedFailedFileCreationStep");
    }
}
