package cogbog.exception;

public class BadHttpMethodException extends RuntimeException {
    public BadHttpMethodException(String httpMethod) {
        super(httpMethod);
    }
}
