package de.glawleschkoff.scannerapp.model;

public class ResponseWrapper <T> {

    private T response;
    private String errorMessage;

    public ResponseWrapper(T response, String errorMessage){
        this.response = response;
        this.errorMessage = errorMessage;
    }

    public T getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
