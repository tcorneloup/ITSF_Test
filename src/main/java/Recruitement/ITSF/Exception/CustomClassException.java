package Recruitement.ITSF.Exception;

public class CustomClassException extends RuntimeException {
    private final String errorCode;

    public CustomClassException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
