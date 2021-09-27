package com.kirtesh.fareestimation.workflow.framework.dtos;

public class WorkflowStepResponse<T> {
    private T responseObject;
    private String statusMessage;

    public WorkflowStepResponse() { }

    public WorkflowStepResponse(T responseObject) {
        this.responseObject = responseObject;
    }

    public WorkflowStepResponse(T responseObject, String statusMessage) {
        this.responseObject = responseObject;
        this.statusMessage = statusMessage;
    }

    public T getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(T responseObject) {
        this.responseObject = responseObject;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("WorkflowStepResponse{");
        sb.append("responseObject=").append(responseObject);
        sb.append(", statusMessage='").append(statusMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
