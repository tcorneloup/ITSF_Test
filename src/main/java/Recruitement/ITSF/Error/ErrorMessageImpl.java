package Recruitement.ITSF.Error;

public final class ErrorMessageImpl implements IErrorMessage {
    private final String code;
    private final String label;

    public ErrorMessageImpl(String code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public String getCode() { return code; }

    @Override
    public String getLabel() { return label; }

    @Override
    public int compareTo(IErrorMessage o) {
        return this.code.compareTo(o.getCode());
    }

    @Override
    public String toString() {
        return "ErrorMessage{code='" + code + "', label='" + label + "'}";
    }
}
