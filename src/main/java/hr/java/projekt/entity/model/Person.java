package hr.java.projekt.entity.model;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.enums.ConditionsFulfilled;
import hr.java.projekt.entity.interfaces.ConditionsChecker;
import hr.java.projekt.entity.interfaces.TrainingSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public non-sealed class Person<T> extends NamedEntity implements ConditionsChecker, TrainingSetter {
    private static final Logger logger = LoggerFactory.getLogger(Person.class);
    private String surname;
    private Long clubId;
    private T membershipDate;
    private Map<String, List<ExerciseSetAndRepForEachPerson>> exerciseSetAndRepMap;
    private Integer completedTrainings = 0;
    private String conditionsFulfilled = "NO";
    private Competition<Address, LocalDate> competition;

    public Person(Integer id, String name, String surname, Long clubId, T membershipDate,
                  Map<String, List<ExerciseSetAndRepForEachPerson>> exerciseSetAndRepMap){

        super(id, name);
        this.surname = surname;
        this.clubId = clubId;
        this.membershipDate = membershipDate;
        this.exerciseSetAndRepMap = exerciseSetAndRepMap;
    }

    @Override
    public void checkIfConditionsFulfilled() {
        if(completedTrainings >= 15){
            conditionsFulfilled = ConditionsFulfilled.YES.toString();
        }
        else{
            conditionsFulfilled = ConditionsFulfilled.NO.toString();
        }

    }

    public void addNumberOfTrainings(){
        this.completedTrainings++;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public Long getClubId() {
        return clubId;
    }
    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public T getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(T membershipDate) {
        this.membershipDate = membershipDate;
    }

    public Integer getCompletedTrainings() {
        return completedTrainings;
    }

    @Override
    public void setCompletedTrainings(Integer completedTrainings) {
        this.completedTrainings = completedTrainings;
    }

    public Map<String, List<ExerciseSetAndRepForEachPerson>> getExerciseSetAndRepMap() {
        return exerciseSetAndRepMap;
    }

    public void setExerciseSetAndRepMap(Map<String, List<ExerciseSetAndRepForEachPerson>> exerciseSetAndRepMap) {
        this.exerciseSetAndRepMap = exerciseSetAndRepMap;
    }


    public Competition<Address, LocalDate> getCompetition() {
        return competition;
    }

    public void setCompetition(Competition<Address, LocalDate> competition) {
        this.competition = competition;
    }

   /* @Override
    public void checkCompetitionRight() {
        if(this.competitionCondition.getConditionsFulfilled().name().equals("NO")){
            Address address = new Address.Builder()
                    .setStreet("-")
                    .build();
             String date = "-";
            this.competition = new Competition<>(address, date);
        }
        else if(this.competitionCondition.getConditionsFulfilled().name().equals("YES")){
            //mora pokupit vrijednost za adresu i datum iz textBox-a
        }
    }*/


    public String getConditionsFulfilled() {
        return conditionsFulfilled;
    }

    public void setConditionsFulfilled(String conditionsFulfilled) {
        this.conditionsFulfilled = conditionsFulfilled;
    }

    public static Optional<Integer> getNextPersonId(){
        Integer id = null;

        try(Connection connection = Database.connectToDatabase()){
            String nextPersonIdQuery = "SELECT COALESCE(MAX(ID), 1) AS max_id FROM PERSON;";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(nextPersonIdQuery);

            if(rs.next()){
                id = rs.getInt("max_id");
            }

        }
        catch (SQLException | IOException ex){
            String message = "Error while reading the database trying to find next person id!";
            logger.error(message, ex);
            System.out.println(message);
        }
        return Optional.ofNullable(id);
    }


    @Override
    public String toString() {
        return getName() + " " + getSurname() + "\n" + "ClubID: "
                + getClubId() + "\n" + "Membership Date: " +  getMembershipDate();
    }

}
