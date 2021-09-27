package com.kirtesh.fareestimation.workflow.framework.dtos;

import java.time.LocalDateTime;
import java.util.Optional;

public class WorkflowMessage<T> {
    private T data;
    private String requestTrackingId;
    private Optional<String> step = null;
    private int retry = 0;
    private Optional<String> error = null;
    private LocalDateTime localDateTime =  LocalDateTime.now();

    public WorkflowMessage(T data, String requestTrackingId, Optional<String> step, int retry, Optional<String> error) {
        this.data = data;
        this.requestTrackingId = requestTrackingId;
        this.step = step;
        this.retry = retry;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getRequestTrackingId() {
        return requestTrackingId;
    }

    public void setRequestTrackingId(String requestTrackingId) {
        this.requestTrackingId = requestTrackingId;
    }

    public Optional<String> getStep() {
        return step;
    }

    public void setStep(Optional<String> step) {
        this.step = step;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public Optional<String> getError() {
        return error;
    }

    public void setError(Optional<String> error) {
        this.error = error;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("WorkflowMessage{");
        sb.append("data=").append(data);
        sb.append(", requestTrackingId='").append(requestTrackingId).append('\'');
        sb.append(", step=").append(step);
        sb.append(", retry=").append(retry);
        sb.append(", error=").append(error);
        sb.append(", localDateTime=").append(localDateTime);
        sb.append('}');
        return sb.toString();
    }
}
