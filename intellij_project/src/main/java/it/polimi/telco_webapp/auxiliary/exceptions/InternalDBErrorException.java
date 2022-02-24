package it.polimi.telco_webapp.auxiliary.exceptions;

public class InternalDBErrorException extends IllegalArgumentException {
    private String errorCode;

    public InternalDBErrorException(String msg) {
        super(msg);
    }

    public InternalDBErrorException(String errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
