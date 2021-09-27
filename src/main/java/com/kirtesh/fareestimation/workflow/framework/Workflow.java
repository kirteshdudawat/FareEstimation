package com.kirtesh.fareestimation.workflow.framework;

import java.util.Map;

public interface Workflow<T> {
    String getName();
    String getStartingStep();
    String getDefaultFailureStepForUnhandledException();
    Map<String, WorkflowStep<T>> getStepDefinitions();
}
