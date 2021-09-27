package com.kirtesh.fareestimation.workflow.farecalculator;

import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.workflow.framework.Workflow;
import com.kirtesh.fareestimation.workflow.framework.WorkflowStep;

import java.util.HashMap;
import java.util.Map;

public class FareEstimationWorkflow implements Workflow<EventMetadata> {
    public String name = "FareEstimationWorkflow";

    private ParseEventToMessageDataStep parseEventToMessageDataStep = new ParseEventToMessageDataStep();
    private ParseDateTimeStep parseDateTimeStep = new ParseDateTimeStep();
    private IsNewRideStep isNewRideStep = new IsNewRideStep();
    private FinalFareCalculationStep finalFareCalculationStep = new FinalFareCalculationStep();
    private SuccessFileCreationStep successFileCreationStep = new SuccessFileCreationStep();
    private ResetJvmCacheStep resetJvmCacheStep = new ResetJvmCacheStep();
    private CalculateSpeedStep calculateSpeedStep = new CalculateSpeedStep();
    private EventBasedFareCalculatorStep eventBasedFareCalculatorStep = new EventBasedFareCalculatorStep();
    private UpdateJvmCache updateJvmCache = new UpdateJvmCache();
    private SkippedFailedFileCreationStep skippedFailedFileCreationStep = new SkippedFailedFileCreationStep();

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getStartingStep() {
        return parseEventToMessageDataStep.getName();
    }

    @Override
    public String getDefaultFailureStepForUnhandledException() {
        return skippedFailedFileCreationStep.getName();
    }

    @Override
    public Map<String, WorkflowStep<EventMetadata>> getStepDefinitions() {
        Map<String, WorkflowStep<EventMetadata>> map = new HashMap<>();
        map.put(parseEventToMessageDataStep.getName(), parseEventToMessageDataStep);
        map.put(parseDateTimeStep.getName(), parseDateTimeStep);
        map.put(isNewRideStep.getName(), isNewRideStep);
        map.put(finalFareCalculationStep.getName(), finalFareCalculationStep);
        map.put(successFileCreationStep.getName(), successFileCreationStep);
        map.put(resetJvmCacheStep.getName(), resetJvmCacheStep);
        map.put(calculateSpeedStep.getName(), calculateSpeedStep);
        map.put(eventBasedFareCalculatorStep.getName(), eventBasedFareCalculatorStep);
        map.put(updateJvmCache.getName(), updateJvmCache);
        map.put(skippedFailedFileCreationStep.getName(), skippedFailedFileCreationStep);

        return map;
    }
}
