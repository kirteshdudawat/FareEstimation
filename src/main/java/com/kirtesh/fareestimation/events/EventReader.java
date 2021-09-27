package com.kirtesh.fareestimation.events;

import com.kirtesh.fareestimation.workflow.framework.WorkflowExecutor;

public interface EventReader<T> {
    void readEvents(WorkflowExecutor<T> executor);
}
