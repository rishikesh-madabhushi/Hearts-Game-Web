package com.llwantedll.webhearts.models.messages;

public class GameRoomWorkflowMessage {

    private String workflowState;

    public GameRoomWorkflowMessage(String workflowState) {
        this.workflowState = workflowState;
    }

    public String getWorkflowState() {
        return workflowState;
    }

    public void setWorkflowState(String workflowState) {
        this.workflowState = workflowState;
    }
}
