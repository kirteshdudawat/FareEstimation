package com.kirtesh.fareestimation.workflow.framework;


import com.kirtesh.fareestimation.enums.StatusCodes;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowStepResponse;

import java.util.Optional;

public interface WorkflowStep<T> {
    String getName();
    WorkflowStepResponse<T> process(T metadata);
    Optional<String> nextStep(T metadata, StatusCodes status);

    default boolean isSkippable(T metadata) {
        return false;
    }

    default WorkflowStepResponse<T> generateResponse(T metadata, StatusCodes statusCode) {
        WorkflowStepResponse<T> response = new WorkflowStepResponse<>();
        response.setResponseObject(metadata);
        response.setStatusMessage(statusCode.getStatusCode());
        return response;
    }
}
