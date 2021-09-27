package com.kirtesh.fareestimation.workflow.farecalculator;

import com.kirtesh.fareestimation.enums.StatusCodes;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.workflow.framework.WorkflowStep;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowStepResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Optional;

public class SkippedFailedFileCreationStep implements WorkflowStep<EventMetadata> {

    private final static Logger skippedLogger = LogManager.getLogger("skipped");

    private String name = "SkippedFailedFileCreationStep";

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public WorkflowStepResponse<EventMetadata> process(EventMetadata metadata) {

        skippedLogger.info(metadata.getEventString());
        return generateResponse(metadata, StatusCodes.SUCCESS);
    }

    @Override
    public Optional<String> nextStep(EventMetadata eventMetadata, StatusCodes status) {
        return Optional.empty();
    }
}
