package com.kirtesh.fareestimation.workflow.farecalculator;

import com.kirtesh.fareestimation.cache.JvmPreviousEvent;
import com.kirtesh.fareestimation.enums.StatusCodes;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.workflow.framework.WorkflowStep;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowStepResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Optional;

public class SuccessFileCreationStep implements WorkflowStep<EventMetadata> {

    private final static Logger successLogger = LogManager.getLogger("success");

    private String name = "SuccessFileCreationStep";

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public WorkflowStepResponse<EventMetadata> process(EventMetadata metadata) {

        EventMetadata previousMetadata = JvmPreviousEvent.getEventMetadata();
        successLogger.info(previousMetadata.getRideId() + "," +previousMetadata.getTotalFareIncludingCurrentEvent());

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
