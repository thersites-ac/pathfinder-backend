package cogbog.exception;

public class BadPathParametersException extends Exception {
    public BadPathParametersException(String s) {
        super(s);
    }

    public BadPathParametersException(Exception ex) {
        super(ex);
    }
}
