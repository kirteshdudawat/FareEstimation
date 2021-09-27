package com.kirtesh.fareestimation.workflow.farecalculator;

import com.kirtesh.fareestimation.cache.JvmCache;
import com.kirtesh.fareestimation.cache.JvmPreviousEvent;
import com.kirtesh.fareestimation.enums.StatusCodes;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.workflow.framework.WorkflowStep;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowStepResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Optional;

public class FinalFareCalculationStep implements WorkflowStep<EventMetadata> {

    private final static Logger logger = LogManager.getLogger(FinalFareCalculationStep.class);

    private String name = "FinalFareCalculationStep";

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public WorkflowStepResponse<EventMetadata> process(EventMetadata metadata) {

        EventMetadata previousMetadata = JvmPreviousEvent.getEventMetadata();

        previousMetadata.setTotalFareIncludingCurrentEvent(
                Math.max(previousMetadata.getTotalFareIncludingCurrentEvent(), JvmCache.minimumRideFare)
        );

        return generateResponse(metadata, StatusCodes.SUCCESS);
    }

    @Override
    public Optional<String> nextStep(EventMetadata eventMetadata, StatusCodes status) {
        if (status.getStatusCode().equals(StatusCodes.SUCCESS.getStatusCode())) {
            return Optional.of("SuccessFileCreationStep");
        }

        return Optional.of("SkippedFailedFileCreationStep");
    }
}
