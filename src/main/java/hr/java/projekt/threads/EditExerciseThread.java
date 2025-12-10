package hr.java.projekt.threads;

import hr.java.projekt.database.Database;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.List;

public class EditExerciseThread implements Runnable{

    private final String exerciseToEdit;
    private final Integer chosenExerciseId;
    private final List<String> attributesToChangeList;
    private final String newName;
    private final String newBodyEffect;
    private final String newDescription;
    private final String newVideoLink;

    public EditExerciseThread(String exerciseToEdit, Integer chosenExerciseId, List<String> attibutesToChangeList, String newName, String newBodyEffect, String newDescription, String newVideoLink) {
        this.exerciseToEdit = exerciseToEdit;
        this.chosenExerciseId = chosenExerciseId;
        this.attributesToChangeList = attibutesToChangeList;
        this.newName = newName;
        this.newBodyEffect = newBodyEffect;
        this.newDescription = newDescription;
        this.newVideoLink = newVideoLink;
    }


    @Override
    public void run() {
        editExercise();
    }

    private Boolean isDatabaseOperationInProgress = false;
    public synchronized void editExercise(){
        while(isDatabaseOperationInProgress){
            try{
                wait();
            }
            catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        Database.editExercise(exerciseToEdit, chosenExerciseId, attributesToChangeList, newName,
                newBodyEffect, newDescription, newVideoLink);

        isDatabaseOperationInProgress = false;
        notifyAll();



    }


}
