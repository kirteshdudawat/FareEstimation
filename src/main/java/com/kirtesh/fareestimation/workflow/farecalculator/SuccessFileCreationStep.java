package com.kirtesh.fareestimation.workflow.farecalculator;

import com.kirtesh.fareestimation.cache.JvmPreviousEvent;
import com.kirtesh.fareestimation.enums.StatusCodes;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.utility.CustomFileWriter;
import com.kirtesh.fareestimation.workflow.framework.WorkflowStep;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowStepResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

public class SuccessFileCreationStep implements WorkflowStep<EventMetadata> {

    private final static Logger logger = LogManager.getLogger(SuccessFileCreationStep.class);

    private String name = "SuccessFileCreationStep";

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public WorkflowStepResponse<EventMetadata> process(EventMetadata metadata) {

        EventMetadata previousMetadata = JvmPreviousEvent.getEventMetadata();
        try {
            CustomFileWriter.writeToSuccessFile(previousMetadata.getRideId() + "," +previousMetadata.getTotalFareIncludingCurrentEvent());
        } catch (IOException e) {
            logger.error("Unable to write to success file");
            e.printStackTrace();
        }

        return generateResponse(metadata, StatusCodes.SUCCESS);
    }

    @Override
    public Optional<String> nextStep(EventMetadata eventMetadata, StatusCodes status) {
        if (status.getStatusCode().equals(StatusCodes.SUCCESS.getStatusCode())) {
            return Optional.of("ResetJvmCacheStep");
        }

        return Optional.of("SkippedFailedFileCreationStep");
    }
}
