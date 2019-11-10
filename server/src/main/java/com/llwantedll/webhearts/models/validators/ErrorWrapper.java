package com.llwantedll.webhearts.models.validators;

public class ErrorWrapper {
    private String error;

    public ErrorWrapper(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
