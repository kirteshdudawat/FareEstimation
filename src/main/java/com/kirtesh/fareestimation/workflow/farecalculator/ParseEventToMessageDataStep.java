package com.kirtesh.fareestimation.workflow.farecalculator;

import com.kirtesh.fareestimation.enums.StatusCodes;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.utility.LocationUtility;
import com.kirtesh.fareestimation.workflow.framework.WorkflowStep;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowStepResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Optional;

public class ParseEventToMessageDataStep implements WorkflowStep<EventMetadata> {

    public final static Logger logger = LogManager.getLogger(ParseEventToMessageDataStep.class);

    private String name = "ParseEventToMessageDataStep";

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public WorkflowStepResponse<EventMetadata> process(EventMetadata metadata) {

        String line = metadata.getEventString();
        String[] values = line.split(",");
        if (values.length != 4) {
            logger.error("CSV file has invalid format, number of column less than 4");
            return generateResponse(metadata, StatusCodes.PARSING_ERROR__MISSING_EVENT_METADATA);
        } else {
            return parseResult(metadata, values);
        }
    }

    private WorkflowStepResponse<EventMetadata> parseResult(EventMetadata metadata, String[] values) {
        try {
            metadata.setRideId(Long.parseLong(values[0]));
            metadata.setLatitude(Double.parseDouble(values[1]));
            metadata.setLongitude(Double.parseDouble(values[2]));
            metadata.setEpochTime(Long.parseLong(values[3]));
        } catch (NumberFormatException e) {
            logger.error("CSV file data not in parsable format", e);
            return generateResponse(metadata, StatusCodes.PARSING_ERROR__INVALID_EVENT_METADATA_FORMAT);
        }

        if (!LocationUtility.verifyLocation(metadata.getLatitude(), metadata.getLongitude())) {
            logger.error("Location provided is invalid");
            return generateResponse(metadata, StatusCodes.INVALID_LOCATION_PROVIDED);
        }

        return generateResponse(metadata, StatusCodes.SUCCESS);
    }

    @Override
    public Optional<String> nextStep(EventMetadata eventMetadata, StatusCodes status) {
        if (status.getStatusCode().equals(StatusCodes.SUCCESS.getStatusCode())) {
            return Optional.of("ParseDateTimeStep");
        }

        return Optional.of("SkippedFailedFileCreationStep");
    }
}
