package hr.java.projekt.exceptions;

public class DuplicatePersonException extends RuntimeException{
    public DuplicatePersonException() {
    }

    public DuplicatePersonException(String message) {
        super(message);
    }
}
