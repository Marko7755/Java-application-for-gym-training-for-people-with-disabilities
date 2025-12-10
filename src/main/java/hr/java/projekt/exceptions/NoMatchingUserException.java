package hr.java.projekt.exceptions;

public class NoMatchingUserException extends Exception{

    public NoMatchingUserException() {
    }
    public NoMatchingUserException(String message) {
        super(message);
    }

}
