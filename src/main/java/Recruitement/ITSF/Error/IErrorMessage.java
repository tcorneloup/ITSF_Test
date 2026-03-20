package Recruitement.ITSF.Error;

public interface IErrorMessage extends Comparable<IErrorMessage>{

    String getCode();
    String getLabel();
}
