package hr.java.projekt.threads;

import hr.java.projekt.entity.model.Exercise;
import hr.java.projekt.fileUtils.FileUtils;

import java.util.List;

public class WriteExerciseToBinaryFileThread implements Runnable{

    private final List<String> attributesToChange;
    private final Exercise oldExercise;
    private final String newExerciseName;
    private final String newBodyEffect;
    private final String newDescription;
    private final String newVideoLink;

    public WriteExerciseToBinaryFileThread(List<String> attributesToChange, Exercise oldExercise, String newName, String newBodyEffect, String newDescription, String newVideoLink) {
        this.attributesToChange = attributesToChange;
        this.oldExercise = oldExercise;
        this.newExerciseName = newName;
        this.newBodyEffect = newBodyEffect;
        this.newDescription = newDescription;
        this.newVideoLink = newVideoLink;
    }

    @Override
    public void run(){
        writeExercisesToBinaryFile();
    }

    private Boolean isFileOperationInProgress = false;

    public synchronized void writeExercisesToBinaryFile(){
        while (isFileOperationInProgress){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isFileOperationInProgress = true;
        FileUtils.writeExerciseChangesToBinaryFile(attributesToChange, oldExercise, newExerciseName,
                newBodyEffect, newDescription, newVideoLink);

        isFileOperationInProgress = false;
        notifyAll();

    }


    public List<String> getAttributesToChange() {
        return attributesToChange;
    }

    public Exercise getOldExercise() {
        return oldExercise;
    }

    public String getNewExerciseName() {
        return newExerciseName;
    }

    public String getNewBodyEffect() {
        return newBodyEffect;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public String getNewVideoLink() {
        return newVideoLink;
    }
}
