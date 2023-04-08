package de.glawleschkoff.scannerapp.model;

public class ResponseWrapper <T> {

    private T response;
    private String errorMessage;

    public ResponseWrapper(T response, String errorMessage){
        this.response = response;
        this.errorMessage = errorMessage;
    }
    public ResponseWrapper(){
        this.response = null;
        this.errorMessage = null;
    }

    public T getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
