package hr.java.projekt.exceptions;

public class DuplicateExerciseException extends RuntimeException{
    public DuplicateExerciseException() {
    }

    public DuplicateExerciseException(String message) {
        super(message);
    }
}
