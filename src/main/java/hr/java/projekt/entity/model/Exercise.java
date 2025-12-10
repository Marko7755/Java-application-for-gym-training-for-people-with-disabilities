package hr.java.projekt.entity.model;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.enums.ExerciseBodyEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Exercise extends NamedEntity implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Exercise.class);
    private ExerciseBodyEffect bodyEffect;
    private String description;
    private String videoLink;
    public Exercise(Integer id, String name, ExerciseBodyEffect bodyEffect, String description, String videoLink) {
        super(id, name);
        this.bodyEffect = bodyEffect;
        this.description = description;
        this.videoLink = videoLink;

    }


    public ExerciseBodyEffect getBodyEffect() {
        return bodyEffect;
    }

    public void setBodyEffect(ExerciseBodyEffect bodyEffect) {
        this.bodyEffect = bodyEffect;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public static Optional<Integer> getNextExerciseId(){
        Integer id = null;
        try(Connection connection = Database.connectToDatabase()){
            String nextExerciseIdQuery = "SELECT COALESCE(MAX(ID), 1) AS max_id FROM EXERCISE;";
            //The COALESCE function is used to handle the case where the result of MAX(ID) is NULL,
            // ensuring that the query always returns a value.
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(nextExerciseIdQuery);

            if(rs.next()){
                id = rs.getInt("max_id");
            }

        }
        catch (IOException | SQLException ex){
            String message = "Error while reading the database trying to find next exercise id!";
            logger.error(message, ex);
            System.out.println(message);
        }
        return Optional.ofNullable(id);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
