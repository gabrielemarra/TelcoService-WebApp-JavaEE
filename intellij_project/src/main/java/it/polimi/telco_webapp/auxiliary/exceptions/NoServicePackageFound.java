package it.polimi.telco_webapp.auxiliary.exceptions;

public class NoServicePackageFound extends IllegalArgumentException {
    private String errorCode;

    public NoServicePackageFound(String msg) {
        super(msg);
    }

    public NoServicePackageFound(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
