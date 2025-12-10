package hr.java.projekt.exceptions;

public class UserAlreadyExistsException extends Exception{

    public UserAlreadyExistsException() {
    }
    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
