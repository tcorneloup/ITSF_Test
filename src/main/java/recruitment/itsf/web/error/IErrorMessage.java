package recruitment.itsf.web.error;

public interface IErrorMessage extends Comparable<IErrorMessage>{

    String getCode();
    String getLabel();
}
