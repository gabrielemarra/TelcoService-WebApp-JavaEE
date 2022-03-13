package it.polimi.telco_webapp.auxiliary.exceptions;

public class UserNotFoundException extends IllegalArgumentException {
    private String errorCode;

    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
