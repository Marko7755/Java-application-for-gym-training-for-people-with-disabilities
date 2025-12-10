package hr.java.projekt.entity.interfaces;

import hr.java.projekt.entity.model.Person;

import java.time.LocalDate;

public sealed interface TrainingSetter permits Person {

    void setCompletedTrainings(Integer completedTrainings);

}
