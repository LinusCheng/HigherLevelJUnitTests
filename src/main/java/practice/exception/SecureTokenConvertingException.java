package practice.exception;

public class SecureTokenConvertingException extends Exception{

    public SecureTokenConvertingException(String message) {
        super(message);
    }

    public SecureTokenConvertingException(String message, Exception e) {
        super(message, e);
    }

}
