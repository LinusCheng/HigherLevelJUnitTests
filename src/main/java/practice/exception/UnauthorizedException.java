package practice.exception;

public class UnauthorizedException extends Exception{

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Exception e) {
        super(message, e);
    }

}
