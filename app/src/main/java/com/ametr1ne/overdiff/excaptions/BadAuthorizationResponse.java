package com.ametr1ne.overdiff.excaptions;


public class BadAuthorizationResponse extends RuntimeException {

    private int status;

    public BadAuthorizationResponse(int status) {
        this.status = status;
    }

    public BadAuthorizationResponse(String message, int status) {
        super(message+" status: "+ message);
        this.status = status;
    }
}
