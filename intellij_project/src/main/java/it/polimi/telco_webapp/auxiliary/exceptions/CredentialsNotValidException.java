package it.polimi.telco_webapp.auxiliary.exceptions;

public class CredentialsNotValidException extends IllegalArgumentException {
    private String errorCode;
    private boolean employeeRequest;

    public CredentialsNotValidException(String msg) {
        super(msg);
    }

    public CredentialsNotValidException(String errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.employeeRequest = false;
    }

    public CredentialsNotValidException(String errorCode, String msg, boolean employeeRequest) {
        super(msg);
        this.errorCode = errorCode;
        this.employeeRequest = employeeRequest;
    }


    public String getErrorCode() {
        return errorCode;
    }


    public void setEmployeeRequest(boolean employeeRequest) {
        this.employeeRequest = employeeRequest;
    }
}
