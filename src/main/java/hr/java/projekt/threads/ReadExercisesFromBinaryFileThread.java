package hr.java.projekt.threads;

import hr.java.projekt.entity.changes.ChangesMade;
import hr.java.projekt.fileUtils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class ReadExercisesFromBinaryFileThread implements Runnable{

    private List<ChangesMade<?>> changesMadeList;

    @Override
    public void run(){
        changesMadeList = readExerciseFromBinaryFile();
    }

    private Boolean isFileOperationInProgress = false;

    public synchronized List<ChangesMade<?>> readExerciseFromBinaryFile(){
        List<ChangesMade<?>> changesMadeList;
        while(isFileOperationInProgress){
            try{
                wait();
            }
            catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }

        isFileOperationInProgress = true;
        changesMadeList = FileUtils.readLastChangeFromBinaryFile();

        isFileOperationInProgress = false;
        notifyAll();

        return changesMadeList;

    }

    public List<ChangesMade<?>> getChangesMadeList() {
        return changesMadeList;
    }
}
