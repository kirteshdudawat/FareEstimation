package com.kirtesh.fareestimation.workflow.farecalculator;

import com.kirtesh.fareestimation.cache.JvmPreviousEvent;
import com.kirtesh.fareestimation.enums.StatusCodes;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.workflow.framework.WorkflowStep;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowStepResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Optional;

public class IsNewRideStep implements WorkflowStep<EventMetadata> {

    private final static Logger logger = LogManager.getLogger(IsNewRideStep.class);

    private String name = "IsNewRideStep";

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public WorkflowStepResponse<EventMetadata> process(EventMetadata metadata) {

        if (JvmPreviousEvent.isRideProcessedPreviously(metadata.getRideId())) {
            logger.info("Ride processed previously, going in normal flow.");
        } else {
            metadata.setNewRideEvent(true);
        }
        return generateResponse(metadata, StatusCodes.SUCCESS);
    }

    @Override
    public Optional<String> nextStep(EventMetadata eventMetadata, StatusCodes status) {
        if (status.getStatusCode().equals(StatusCodes.SUCCESS.getStatusCode())) {
            if (eventMetadata.isNewRideEvent() && !JvmPreviousEvent.isEmpty()) {
                return Optional.of("FinalFareCalculationStep");
            } else {
                return Optional.of("CalculateSpeedStep");
            }
        }

        return Optional.of("SkippedFailedFileCreationStep");
    }
}
