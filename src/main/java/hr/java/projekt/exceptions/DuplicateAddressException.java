package hr.java.projekt.exceptions;

public class DuplicateAddressException extends RuntimeException{
    public DuplicateAddressException() {
    }

    public DuplicateAddressException(String message) {
        super(message);
    }
}
