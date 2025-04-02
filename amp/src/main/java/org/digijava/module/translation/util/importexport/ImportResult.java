package org.digijava.module.translation.util.importexport;

public class ImportResult {
    private String key;
    private boolean success;
    private String error;

    public ImportResult(String key) {
        this.key = key;
        this.success = true;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}