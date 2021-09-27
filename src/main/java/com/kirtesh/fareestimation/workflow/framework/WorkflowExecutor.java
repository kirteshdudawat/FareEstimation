package com.kirtesh.fareestimation.workflow.framework;

import com.kirtesh.fareestimation.enums.StatusCodes;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.workflow.framework.dtos.ExecutorResponse;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowMessage;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowStepResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class WorkflowExecutor<T> {
    private Workflow<T> workflow;
    public final static Logger logger = LogManager.getLogger(WorkflowExecutor.class);

    public WorkflowExecutor(Workflow<T> workflow) {
        this.workflow = workflow;
    }

    // Return value to this method is required for testing only.
    public List<ExecutorResponse<EventMetadata>> executeWorkflow(WorkflowMessage<T> metadata) {
        List<ExecutorResponse<EventMetadata>> _response = new LinkedList<>();
        Optional<String> executingStep = metadata.getStep().isPresent()? metadata.getStep(): Optional.of(workflow.getStartingStep());

        WorkflowStep<T> stepDefinition;
        WorkflowStepResponse<T> response = new WorkflowStepResponse<T>(metadata.getData());

        do {
            try {
                stepDefinition = workflow.getStepDefinitions().get(executingStep.get());
                response = stepDefinition.process(response.getResponseObject());
                _response.add(new ExecutorResponse(executingStep.get(), response.getStatusMessage(), response.getResponseObject()));
                logger.info("Response from executingStep " + executingStep.get() + " is " + response);
                executingStep = stepDefinition.nextStep(response.getResponseObject(), StatusCodes.fromString(response.getStatusMessage()));
            } catch (Exception e) {
                logger.error("Unhandled exception in some step, skipping event.");
                executingStep = Optional.of(workflow.getDefaultFailureStepForUnhandledException());
            }
        } while (executingStep.isPresent());

        return _response;
    }
}
