package com.kirtesh.fareestimation.workflow.farecalculator;

import com.kirtesh.fareestimation.enums.StatusCodes;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.utility.CustomFileWriter;
import com.kirtesh.fareestimation.workflow.framework.WorkflowStep;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowStepResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

public class DefaultFailureStep implements WorkflowStep<EventMetadata> {

    private final static Logger logger = LogManager.getLogger(DefaultFailureStep.class);

    private String name = "DefaultFailureStep";

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public WorkflowStepResponse<EventMetadata> process(EventMetadata metadata) {

        logger.error("UNABLE TO HANDLE EXCEPTION IN FLOW, " + metadata.toString());
        return generateResponse(metadata, StatusCodes.SUCCESS);
    }

    @Override
    public Optional<String> nextStep(EventMetadata eventMetadata, StatusCodes status) {
        return Optional.empty();
    }
}
