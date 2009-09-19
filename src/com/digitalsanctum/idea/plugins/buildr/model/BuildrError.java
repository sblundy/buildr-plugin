package com.digitalsanctum.idea.plugins.buildr.model;

/**
 * Created by IntelliJ IDEA.
 * User: shane
 * Date: Nov 26, 2008
 * Time: 8:55:13 AM
 */
public class BuildrError {

    private String cause;
    private String message;

    public BuildrError(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
