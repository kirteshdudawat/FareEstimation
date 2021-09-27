package com.kirtesh.fareestimation.workflow.framework.dtos;

public class ExecutorResponse<T> {
    private String stepName;
    private String status;
    private T metadata;

    public ExecutorResponse() {
    }

    public ExecutorResponse(String stepName, String status, T metadata) {
        this.stepName = stepName;
        this.status = status;
        this.metadata = metadata;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getMetadata() {
        return metadata;
    }

    public void setMetadata(T metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ExecutorResponse{");
        sb.append("stepName='").append(stepName).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", metadata=").append(metadata);
        sb.append('}');
        return sb.toString();
    }
}
