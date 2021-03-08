package practice.exception;

public class MoneyOrderException extends Exception {

    public MoneyOrderException(String message) {
        super(message);
    }

    public MoneyOrderException(String message, Exception e) {
        super(message, e);
    }

}
