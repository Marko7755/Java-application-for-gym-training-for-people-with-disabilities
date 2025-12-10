package hr.java.projekt.database;

import hr.java.projekt.controller.MenuBarController;
import hr.java.projekt.controller.NewExerciseController;
import hr.java.projekt.entity.model.*;
import hr.java.projekt.entity.enums.ExerciseBodyEffect;
import hr.java.projekt.exceptions.DuplicateAddressException;
import hr.java.projekt.exceptions.DuplicateExerciseException;
import hr.java.projekt.exceptions.DuplicatePersonException;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Database {
    private static final String DATABASE_PROPERTIES_FILE = "databaseConfiguration/database.properties";
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    public static Connection connectToDatabase() throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(DATABASE_PROPERTIES_FILE));
        String databaseUrl = properties.getProperty("databaseUrl");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        Connection connection = DriverManager.getConnection(databaseUrl, username, password);
        return connection;
    }

    public static void saveExercise(Exercise exercise) {
        try (Connection connection = connectToDatabase()) {
            String saveExerciseQuery = "INSERT INTO EXERCISE (NAME, BODY_EFFECT, DESCRIPTION, VIDEO_LINK) " +
                    "VALUES(?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(saveExerciseQuery);
            ps.setString(1, exercise.getName());
            ps.setString(2, exercise.getBodyEffect().name());
            ps.setString(3, exercise.getDescription());
            ps.setString(4, exercise.getVideoLink());
            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Exercise saving");
                alert.setHeaderText("Saving a new Exercise was successful!");
                alert.setContentText("You have successfully saved a " + exercise.getName() + " exercise!");
                alert.showAndWait();*/
                savingSuccessAlert("Exercise", exercise.getName());
            }
        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database for saving a new exercise!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }

    public static List<Exercise> getExercisesFromDatabase() {
        List<Exercise> exerciseList = new ArrayList<>();

        try (Connection connection = connectToDatabase()) {
            Statement st = connection.createStatement();
            String sqlExerciseQuery = "SELECT * FROM EXERCISE";
            ResultSet rs = st.executeQuery(sqlExerciseQuery);

            while (rs.next()) {
                Integer id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String bodyEffect = rs.getString("BODY_EFFECT");
                String description = rs.getString("DESCRIPTION");
                String videoLink = rs.getString("VIDEO_LINK");

                ExerciseBodyEffect exerciseBodyEffect = ExerciseBodyEffect.valueOf(bodyEffect);
                Exercise newExercise = new Exercise(id, name, exerciseBodyEffect, description, videoLink);
                exerciseList.add(newExercise);
            }

        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to get Exercises!";
            logger.error(message, ex);
            System.out.println(message);
        }

        return exerciseList;
    }

    public static List<Exercise> getSearchedExercise(String exerciseNameFromTextField,
                                                     ExerciseBodyEffect exerciseBodyEffectFromComboBox) {

        if (Optional.ofNullable(exerciseBodyEffectFromComboBox).isPresent()) {
            if (exerciseBodyEffectFromComboBox.name().equals("EveryBodyEffect")) { //deselect last selected item from combo
                exerciseBodyEffectFromComboBox = null;
            }
        }

        List<Exercise> exerciseList = new ArrayList<>();
        Map<Integer, String> queryParams = new HashMap<>();
        Integer paramOrderNumber = 1;

        try (Connection connection = connectToDatabase()) {
            String exerciseSearchSqlQuery = "SELECT * FROM EXERCISE WHERE 1=1 ";

            if (!exerciseNameFromTextField.isEmpty()) {
                exerciseSearchSqlQuery += "AND LOWER(NAME) LIKE? ";
                //daje rezultat baziran po slovima, ne cijeloj rijeci(moze i po cijeloj rijeci) i
                //neovisno da li rijec zapocinje velikim ili malim slovom
                queryParams.put(paramOrderNumber, "%" + exerciseNameFromTextField + "%");
                paramOrderNumber++;
            }

            if (Optional.ofNullable(exerciseBodyEffectFromComboBox).isPresent()) {
                exerciseSearchSqlQuery += "AND BODY_EFFECT = ?";
                queryParams.put(paramOrderNumber, exerciseBodyEffectFromComboBox.name());
                paramOrderNumber++;
            }

            PreparedStatement ps = connection.prepareStatement(exerciseSearchSqlQuery);

            for (Integer key : queryParams.keySet()) {
                ps.setString(key, queryParams.get(key));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String bodyEffectString = rs.getString("BODY_EFFECT");
                ExerciseBodyEffect bodyEffect = ExerciseBodyEffect.valueOf(bodyEffectString);
                String description = rs.getString("DESCRIPTION");
                String videoLink = rs.getString("VIDEO_LINK");
                Exercise newExercise = new Exercise(id, name, bodyEffect, description, videoLink);
                exerciseList.add(newExercise);
            }


        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to search for exercises!";
            logger.error(message, ex);
            System.out.println(message);
        }
        return exerciseList;
    }

    public static void editExercise(String exerciseNameToEdit, Integer chosenExerciseId,
                                    List<String> attributesToChangeList, String newName, String newBodyEffect,
                                    String newDescription, String newVideoLink) {

        try (Connection connection = connectToDatabase()) {
            StringBuilder editExerciseQuery = new StringBuilder("UPDATE EXERCISE SET ");
            if (!exerciseNameToEdit.isEmpty()) {
                for (String attributeToChange : attributesToChangeList) {
                    switch (attributeToChange) {
                        case "Name":
                            editExerciseQuery.append("NAME = '").append(newName.replace("'", "''")).append("', ");
                            break;

                        case "Body Effect":
                            editExerciseQuery.append("BODY_EFFECT = '").append(newBodyEffect.replace("'", "''")).append("', ");
                            break;

                        case "Description":
                            editExerciseQuery.append("DESCRIPTION = '").append(newDescription.replace("'", "''")).append("', ");
                            break;

                        case "Video link":
                            editExerciseQuery.append("VIDEO_LINK = '").append(newVideoLink.replace("'", "''")).append("', ");
                            break;

                        default:
                            break;
                    }
                }
                //izbrisi "," i razmak
                editExerciseQuery.delete(editExerciseQuery.length() - 2, editExerciseQuery.length());
                //

                editExerciseQuery.append(" WHERE ID = ").append(chosenExerciseId);

                Statement st = connection.createStatement();
                int row = st.executeUpdate(editExerciseQuery.toString());
                if (row > 0) {
                    Platform.runLater(() -> {
                        /*Alert editSuccessAlert = new Alert(Alert.AlertType.INFORMATION);
                        editSuccessAlert.setTitle("Info");
                        editSuccessAlert.setHeaderText("Edit was successful!");
                        editSuccessAlert.setContentText("Editing an " + exerciseNameToEdit + " exercise was successful!");
                        editSuccessAlert.showAndWait();*/
                        editingSuccessAlert(exerciseNameToEdit, "exercise");
                    });
                }
            }
        } catch (SQLException | IOException ex) {
            String message = "Error while reading the Database trying to edit an Exercise!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }


    public static void checkDuplicateExerciseFromDatabase(String exerciseName) {
        try (Connection connection = connectToDatabase()) {
            String duplicateExerciseQuery = "SELECT * FROM EXERCISE WHERE LOWER(NAME) = LOWER(?)";

            PreparedStatement ps = connection.prepareStatement(duplicateExerciseQuery);
            ps.setString(1, exerciseName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicateExerciseException("You're trying to add an Exercise that already exists, " +
                        "please try again!");
            }


        } catch (SQLException | IOException ex) {
            String message = "Error while reading the Database trying to find a Exercise duplicate!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }

    public static void checkDuplicatePersonFromDatabase(Long personClubId) {
        try (Connection connection = connectToDatabase()) {
            String duplicateExerciseQuery = "SELECT * FROM PERSON WHERE CLUB_ID = ?";

            PreparedStatement ps = connection.prepareStatement(duplicateExerciseQuery);
            ps.setLong(1, personClubId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicatePersonException("You're trying to add a Person that already exists, " +
                        "please try again!");
            }


        } catch (SQLException | IOException ex) {
            String message = "Error while reading the Database trying to find a Person duplicate!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }

    public static void deleteAnExercise(Exercise exerciseToDelete) {

        try (Connection connection = connectToDatabase()) {

            String exerciseInUseSql = "SELECT * FROM PERSON_TRAINING_RECORD WHERE EXERCISE_ID = ?";
            PreparedStatement psExercise = connection.prepareStatement(exerciseInUseSql);
            psExercise.setInt(1, exerciseToDelete.getId());
            ResultSet rs = psExercise.executeQuery();
            if (rs.next()) {
                attributeInUseAlert("Exercise");
            } else {
                String sqlQueryToDeleteAnExercise = "DELETE FROM EXERCISE WHERE ID = ?";
                PreparedStatement ps = connection.prepareStatement(sqlQueryToDeleteAnExercise);
                ps.setInt(1, exerciseToDelete.getId());
                int deleteExerciseRowResult = ps.executeUpdate();

                if (deleteExerciseRowResult != 0) {
                    deletingSuccessAlert("Exercise", exerciseToDelete.getName());
                    FileUtils.deleteAnAttribute("Exercise", exerciseToDelete.getName());
                }
            }
        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to delete an exercise!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }

    public static Optional<Person<LocalDate>> getPersonById(Long clubId) {
        Person<LocalDate> person = null;
        int trainings = 0;
        try (Connection connection = connectToDatabase()) {
            String personByClubIdSql = "SELECT * FROM PERSON WHERE CLUB_ID = ?";

            PreparedStatement ps = connection.prepareStatement(personByClubIdSql);
            ps.setLong(1, clubId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                long readClubId = rs.getLong("CLUB_ID");
                LocalDate membershipDate = rs.getDate("MEMBERSHIP_DATE").toLocalDate();
                Person<LocalDate> personExercises = getPersonWithTheirExercises(id, name, surname,
                        readClubId, membershipDate);

                String setTrainingsSql = "SELECT * FROM COMPLETED_TRAININGS WHERE PERSON_ID = ? AND CLUB_ID = ?";
                PreparedStatement getTrainingsPs = connection.prepareStatement(setTrainingsSql);
                getTrainingsPs.setInt(1, id);
                getTrainingsPs.setLong(2, readClubId);
                ResultSet trainingsRs = getTrainingsPs.executeQuery();
                if (trainingsRs.next()) {
                    trainings = trainingsRs.getInt("COMPLETED_TRAININGS");
                }

                person = new Person<>(id, name, surname, readClubId, membershipDate,
                        personExercises.getExerciseSetAndRepMap());
                person.setCompletedTrainings(trainings);
                person.checkIfConditionsFulfilled();
            }


        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to find a Person by Clud ID!";
            logger.error(message, ex);
            System.out.println(message);
        }
        return Optional.ofNullable(person);
    }


    public static void saveNewAddress(Address newAddress) {

        try (Connection connection = connectToDatabase()) {

            String sqlQueryToSaveAddress = "INSERT INTO ADDRESS (STREET, CITY, HALL) VALUES(?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sqlQueryToSaveAddress);
            ps.setString(1, newAddress.getStreet());
            ps.setString(2, newAddress.getCity());
            ps.setString(3, newAddress.getHall().toString());
            int rowResult = ps.executeUpdate();

            if (rowResult > 0) {
                savingSuccessAlert("Address", newAddress.getStreet());
            }

        } catch (SQLException | IOException ex) {
            String message = "Error while opening the database trying to add a new Address!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }

    public static void checkDuplicateAddress(String addressStreet, String addressCity) {
        try (Connection connection = connectToDatabase()) {
            String duplicateAddressSql = "SELECT * FROM ADDRESS WHERE LOWER(STREET) = LOWER(?) " +
                    "AND LOWER(CITY) = LOWER(?);";
            PreparedStatement ps = connection.prepareStatement(duplicateAddressSql);
            ps.setString(1, addressStreet);
            ps.setString(2, addressCity);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                throw new DuplicateAddressException("You're trying to add an Address that already exists, " +
                        "please try again!");
            }
        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to find a duplicate Address!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }


    public static List<Address> getAddresses() {
        List<Address> addressList = new ArrayList<>();
        try (Connection connection = connectToDatabase()) {
            String getAllExercisesSql = "SELECT * FROM ADDRESS ORDER BY ID DESC";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(getAllExercisesSql);
            addressList = Address.readAddressesFromDatabase(rs);
        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to get all Addresses!";
            logger.error(message, ex);
            System.out.println(message);
        }
        return addressList;
    }

    public static List<Address> getSearchedAddresses(String streetToSearch, String cityToSearch) {

        List<Address> addressList = new ArrayList<>();
        try (Connection connection = connectToDatabase()) {

            if (!streetToSearch.isEmpty() && cityToSearch.isEmpty()) {
                String addressSearchByStreetSql = "SELECT * FROM ADDRESS WHERE LOWER(STREET) LIKE LOWER(?)";
                PreparedStatement ps = connection.prepareStatement(addressSearchByStreetSql);
                ps.setString(1, "%" + streetToSearch + "%");
                ResultSet rs = ps.executeQuery();
                addressList = Address.readAddressesFromDatabase(rs);

            }

            else if (!cityToSearch.isEmpty() && streetToSearch.isEmpty()) {
                String addressSearchByCitySql = "SELECT * FROM ADDRESS WHERE LOWER(CITY) LIKE LOWER(?)";
                PreparedStatement ps = connection.prepareStatement(addressSearchByCitySql);
                ps.setString(1, "%" + cityToSearch + "%");
                ResultSet rs = ps.executeQuery();
                addressList = Address.readAddressesFromDatabase(rs);
            }

            else {
                String addressSearchByCitySql = "SELECT * FROM ADDRESS WHERE LOWER(STREET) LIKE LOWER(?) AND " +
                        "LOWER(CITY) LIKE LOWER(?)";
                PreparedStatement ps = connection.prepareStatement(addressSearchByCitySql);
                ps.setString(1, "%" + streetToSearch + "%");
                ps.setString(2, "%" + cityToSearch + "%");
                ResultSet rs = ps.executeQuery();
                addressList = Address.readAddressesFromDatabase(rs);
            }

            if (streetToSearch.isEmpty() && cityToSearch.isEmpty()) {
                addressList = getAddresses();

            }

        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to read an Address!";
            logger.error(message, ex);
            System.out.println(message);
        }

        return addressList;
    }


    public static void editAnAddress(List<String> attributesToChange, String street, Integer addressId,
                                     String newStreet, String newCity, String newHall) {
        try (Connection connection = connectToDatabase()) {
            StringBuilder editAnAddressSql = new StringBuilder("UPDATE ADDRESS SET ");
            Map<Integer, String> attributesToEditMap = new HashMap<>();
            Integer mapOrdinal = 1;
            for (String attribute : attributesToChange) {
                switch (attribute) {
                    case "Street":
                        editAnAddressSql.append("STREET = ?, ");
                        attributesToEditMap.put(mapOrdinal, newStreet);
                        mapOrdinal++;
                        break;

                    case "City":
                        editAnAddressSql.append("CITY = ?, ");
                        attributesToEditMap.put(mapOrdinal, newCity);
                        mapOrdinal++;
                        break;

                    case "Hall":
                        editAnAddressSql.append("HALL = ?, ");
                        attributesToEditMap.put(mapOrdinal, newHall);
                        mapOrdinal++;
                        break;

                    default:
                        break;
                }
            }

            editAnAddressSql.delete(editAnAddressSql.length() - 2, editAnAddressSql.length());

            editAnAddressSql.append(" WHERE ID = ?");
            attributesToEditMap.put(mapOrdinal, addressId.toString());

            PreparedStatement ps = connection.prepareStatement(editAnAddressSql.toString());

            for (Integer key : attributesToEditMap.keySet()) {
                ps.setString(key, attributesToEditMap.get(key));
            }


            int rowResult = ps.executeUpdate();
            if (rowResult > 0) {
                editingSuccessAlert(street, "address");
            }

        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to edit an Address!";
            logger.error(message, ex);
            System.out.println(message);
        }

    }


    public static void deleteAddress(Address addressIdToDelete) {
        try (Connection connection = connectToDatabase()) {

            String addressCompetitionAddressSql = "SELECT * FROM COMPETITION WHERE ADDRESS_ID = ?";
            PreparedStatement compPs = connection.prepareStatement(addressCompetitionAddressSql);
            compPs.setInt(1, addressIdToDelete.getId());
            ResultSet rs = compPs.executeQuery();
            if (rs.next()) {
                attributeInUseAlert("Address");
            } else {
                String deleteAddressSql = "DELETE FROM ADDRESS WHERE ID = ?";
                PreparedStatement ps = connection.prepareStatement(deleteAddressSql);
                ps.setInt(1, addressIdToDelete.getId());
                int rowResult = ps.executeUpdate();
                if (rowResult > 0) {
                    deletingSuccessAlert("Address", addressIdToDelete.getStreet());
                    FileUtils.deleteAnAttribute("Address", addressIdToDelete.getStreet());
                }
            }
        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to delete an Address!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }


    public static Address getAddressForCompetition(Integer addressId) {
        List<Address> addressList = new ArrayList<>();
        try (Connection connection = connectToDatabase()) {

            String competitionAddressSql = "SELECT * FROM ADDRESS WHERE ID = ?";
            PreparedStatement ps = connection.prepareStatement(competitionAddressSql);
            ps.setInt(1, addressId);
            ResultSet rs = ps.executeQuery();

            addressList = Address.readAddressesFromDatabase(rs);

        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to find an Address for Competition!";
            logger.error(message, ex);
            System.out.println(message);
        }
        return addressList.getFirst();
    }


    public static List<Day> getDays() {
        List<Day> dayList = new ArrayList<>();
        try (Connection connection = connectToDatabase()) {

            String getDayIdsSql = "SELECT * FROM DAY_OF_THE_WEEK";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(getDayIdsSql);

            while (rs.next()) {
                Integer dayId = rs.getInt("DAY_ID");
                String dayName = rs.getString("NAME");
                Day newDay = new Day(dayId, dayName);
                dayList.add(newDay);
            }

        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to get day id-s!";
            logger.error(message, ex);
            System.out.println(message);
        }
        return dayList;
    }


    public static void saveNewPerson(Person<LocalDate> newPerson) {
        try (Connection connection = connectToDatabase()) {

            try {
                checkDuplicatePersonFromDatabase(newPerson.getClubId());

                String addNewPersonSql =
                        "INSERT INTO PERSON(ID, NAME, SURNAME, CLUB_ID, MEMBERSHIP_DATE) VALUES(?, ?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(addNewPersonSql);
                ps.setInt(1, newPerson.getId() + 1);
                ps.setString(2, newPerson.getName());
                ps.setString(3, newPerson.getSurname());
                ps.setLong(4, newPerson.getClubId());
                ps.setDate(5, new Date(newPerson.getMembershipDate().atStartOfDay()
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
                int rowAddResult = ps.executeUpdate();

                if (rowAddResult != 0) {
                    savingSuccessAlert("Person", newPerson.getName());
                }


                List<Day> dayList = getDays();

           /* String addPersonsExercisesSql = "INSERT INTO PERSON_TRAINING_RECORD" +
                    "(PERSON_ID, CLUB_ID, DAY_ID, EXERCISE_ID, REPS, SETS) VALUES(?, ?, ?, ?, ?, ?)";*/

                String addPersonsExercisesSql = "INSERT INTO PERSON_TRAINING_RECORD" +
                        "(PERSON_ID, CLUB_ID, DAY_ID, EXERCISE_ID, REPS, SETS) VALUES(?, ?, ?, ?, ?, ?)";

                PreparedStatement exPs = connection.prepareStatement(addPersonsExercisesSql);

                Map<String, List<ExerciseSetAndRepForEachPerson>> exerciseMap = newPerson.getExerciseSetAndRepMap();
                for (String key : exerciseMap.keySet()) {

                    Optional<Integer> correspondingDayId = dayList.stream()
                            .filter(day -> day.getDayName().equals(key))
                            .map(Day::getDayId)
                            .findFirst();

                    if (correspondingDayId.isPresent()) {
                        for (ExerciseSetAndRepForEachPerson exercise : exerciseMap.get(key)) {
                            exPs.setInt(1, newPerson.getId() + 1);
                            exPs.setLong(2, newPerson.getClubId());
                            exPs.setInt(3, correspondingDayId.get());
                            exPs.setInt(4, exercise.getExercise().getId());
                            if (exercise.getExercise().getName().equals("Rest")) {
                                exPs.setString(5, "-");
                                exPs.setString(6, "-");
                            } else {
                                exPs.setInt(5, exercise.getRep());
                                exPs.setInt(6, exercise.getSet());
                            }

                            exPs.execute();
                        }
                    }
                }
                FileUtils.writeANewAttribute("Person", newPerson.getName() + " " +
                        newPerson.getSurname());
            } catch (DuplicatePersonException ex) {
                NewExerciseController.duplicateElement("Person", ex);
            }


        } catch (SQLException | IOException ex) {
            String message = "Error while writing to the database trying to add a new Person!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }


    public static List<Person<LocalDate>> getPersons() {

        List<Person<LocalDate>> personList = new ArrayList<>();

        try (Connection connection = connectToDatabase()) {

            String readPersonSql = "SELECT * FROM PERSON";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(readPersonSql);

            while (rs.next()) {
                Integer id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                Long clubId = rs.getLong("CLUB_ID");
                LocalDate membershipDate = rs.getDate("MEMBERSHIP_DATE").toLocalDate();

                Person<LocalDate> readPerson = getPersonWithTheirExercises(id, name, surname, clubId, membershipDate);
                personList.add(readPerson);

            }


        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to read person's sub values!";
            logger.error(message, ex);
            System.out.println(message);
        }

        return personList;
    }


    private static Person<LocalDate> getPersonWithTheirExercises(Integer id, String name, String surname,
                                                                 Long clubId, LocalDate membershipDate) {
        Person<LocalDate> readPerson = null;
        try (Connection connection = connectToDatabase()) {


            List<Day> dayList = getDays();
            List<Exercise> exerciseList = getExercisesFromDatabase();
            List<ExerciseSetAndRepForEachPerson> exerciseSetAndRepList;
            Map<String, List<ExerciseSetAndRepForEachPerson>> exerciseSetAndRepMap = new TreeMap<>();


            for (Day day : dayList) {
                exerciseSetAndRepList = new ArrayList<>();
                String everyDayExercisesSql = "SELECT * FROM PERSON_TRAINING_RECORD " +
                        "WHERE PERSON_ID = ? AND CLUB_ID = ? AND DAY_ID = ?";
                PreparedStatement everyDayPs = connection.prepareStatement(everyDayExercisesSql);
                everyDayPs.setInt(1, id);
                everyDayPs.setLong(2, clubId);
                everyDayPs.setInt(3, day.getDayId());

                ResultSet everyDayRs = everyDayPs.executeQuery();
                while (everyDayRs.next()) {
                    Integer readExerciseId = everyDayRs.getInt("EXERCISE_ID");

                    Optional<Exercise> correspondingExerciseOptional = exerciseList.stream()
                            .filter(exercise -> exercise.getId().equals(readExerciseId))
                            .findFirst();

                    if (correspondingExerciseOptional.isPresent()) {
                        Exercise correspondingExercise = correspondingExerciseOptional.get();
                        String readReps = everyDayRs.getString("REPS");
                        String readSets = everyDayRs.getString("SETS");

                        if (correspondingExercise.getName().equals("Rest")) {
                            ExerciseSetAndRepForEachPerson rest = new ExerciseSetAndRepForEachPerson.Builder()
                                    .setExercise(correspondingExercise)
                                    .build();
                            exerciseSetAndRepList.add(rest);
                        } else {
                            Integer readIntReps = Integer.valueOf(readReps);
                            Integer readIntSets = Integer.valueOf(readSets);

                            ExerciseSetAndRepForEachPerson exerciseSetAndRep =
                                    new ExerciseSetAndRepForEachPerson.Builder()
                                            .setExercise(correspondingExercise)
                                            .setRep(readIntReps)
                                            .setSet(readIntSets)
                                            .build();
                            exerciseSetAndRepList.add(exerciseSetAndRep);
                        }
                    }
                }
                exerciseSetAndRepMap.put(day.getDayName(), exerciseSetAndRepList);


            }


            readPerson = new Person<>(id, name, surname, clubId, membershipDate, exerciseSetAndRepMap);


        } catch (SQLException | IOException ex) {
            String message = "Error while reading the database trying to read a Person!";
            logger.error(message, ex);
            System.out.println(message);
        }
        return readPerson;
    }


    public static void writeATraining(Person<LocalDate> person) {
        try (Connection connection = connectToDatabase()) {

            /*String writeATrainingSql = "INSERT INTO COMPLETED_TRAININGS(PERSON_ID, CLUB_ID, COMPLETED_TRAININGS)" +
                    "VALUES(?, ?, ?)";*/

            String writeATrainingSql = "SELECT COUNT(*) FROM COMPLETED_TRAININGS WHERE PERSON_ID = ? AND CLUB_ID = ?";

            PreparedStatement ps = connection.prepareStatement(writeATrainingSql);
            ps.setLong(1, person.getId());
            ps.setLong(2, person.getClubId());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                String updateSql = "UPDATE COMPLETED_TRAININGS SET COMPLETED_TRAININGS = ? WHERE PERSON_ID = ? AND CLUB_ID = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setInt(1, person.getCompletedTrainings());
                updateStatement.setInt(2, person.getId());
                updateStatement.setLong(3, person.getClubId());
                updateStatement.executeUpdate();
            } else {
                String insertSql = "INSERT INTO COMPLETED_TRAININGS (PERSON_ID, CLUB_ID, COMPLETED_TRAININGS) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                insertStatement.setInt(1, person.getId());
                insertStatement.setLong(2, person.getClubId());
                insertStatement.setInt(3, person.getCompletedTrainings());
                insertStatement.executeUpdate();
            }


        } catch (SQLException | IOException e) {
            String message = "Error while writing a new training to the database";
            logger.error(message, e);
            System.out.println(message);
        }
    }


    public static List<Person<LocalDate>> setAndGetPersonsWithTheirCompletedTrainings() {

        List<Person<LocalDate>> persons = getPersons();
        try (Connection connection = connectToDatabase()) {

            String personsCompletedTrainingsSql = "SELECT * FROM COMPLETED_TRAININGS";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(personsCompletedTrainingsSql);

            while (rs.next()) {
                Integer personId = rs.getInt("PERSON_ID");
                Long clubId = rs.getLong("CLUB_ID");
                Integer completedTrainings = rs.getInt("COMPLETED_TRAININGS");
                persons.stream()
                        .filter(person -> person.getId().equals(personId) && person.getClubId().equals(clubId))
                        .forEach(person -> {
                            person.setCompletedTrainings(completedTrainings);
                            person.checkIfConditionsFulfilled();
                        });
            }

        } catch (SQLException | IOException e) {
            String message = "Error while reading the Database trying to get persons completed trainings!";
            logger.error(message, e);
            System.out.println(message);
        }
        return persons;
    }

    //metoda vraca boolean i onda kad stisnem check Competition gumb ako je false a ima vise od 15 treninga onda da pise da nema unesen competition jer jos nije unesen,
    // a ako je false i competitionFullfiled je NO onda da se ispise da nema competition unesen jer nema ispunjene uvjete
    public static void checkIfCompetitionAlreadyExists(Person<LocalDate> person) {
        try (Connection connection = connectToDatabase()) {
            String writeATrainingSql = "SELECT COUNT(*) FROM COMPETITION WHERE PERSON_ID = ? AND CLUB_ID = ?";

            PreparedStatement psCount = connection.prepareStatement(writeATrainingSql);
            psCount.setLong(1, person.getId());
            psCount.setLong(2, person.getClubId());
            ResultSet rs = psCount.executeQuery();
            rs.next();
            int count = rs.getInt(1);


            if (count > 0) {
                Alert competitionAlreadyExistsAlert = new Alert(Alert.AlertType.WARNING);
                competitionAlreadyExistsAlert.setTitle("Error");
                competitionAlreadyExistsAlert.setHeaderText("Competition already exists");
                competitionAlreadyExistsAlert.setContentText("Competition for Person "
                        + person.getClubId() + " already exists!");
                competitionAlreadyExistsAlert.showAndWait();
            } else if (count == 0) {
                showAddCompetitionIfNotAlreadySetScreen(person);
            }
        } catch (SQLException | IOException e) {
            String message = "Error while reading the Database trying to get persons completed trainings!";
            logger.error(message, e);
            System.out.println(message);
        }
    }

    public static void showAddCompetitionIfNotAlreadySetScreen(Person<LocalDate> person) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Competition conditions fulfilled but not set");
        alert.setContentText(person.getName() + " " + person.getSurname()
                + " has conditions, but the Competition is not set yet! \n" +
                "Do you want to set the Competition now?");
        ButtonType buttonYes = new ButtonType("Yes", ButtonType.YES.getButtonData());
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(buttonYes, buttonCancel);
        Optional<ButtonType> buttonTypeOptional = alert.showAndWait();

        if (buttonTypeOptional.isPresent()) {
            if (buttonTypeOptional.get() == buttonYes) {
                MenuBarController.newCompetitionScreen(person);
            }
        }
    }

    public static void saveNewCompetition(Person<LocalDate> person, Competition<Address, LocalDate> competition) {

        try (Connection connection = connectToDatabase()) {

            String competitionSql = "INSERT INTO COMPETITION" +
                    "(PERSON_ID, CLUB_ID, ADDRESS_ID, COMPETITION_DATE, COMPETITION_TIME) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(competitionSql);
            ps.setInt(1, person.getId());
            ps.setLong(2, person.getClubId());
            ps.setInt(3, competition.getAddress().getId());
            ps.setDate(4, new Date(competition.getDate().atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            ps.setString(5, competition.getTime());
            int rowResult = ps.executeUpdate();
            if (rowResult != 0) {
                savingSuccessAlert("Competition", "new");
                FileUtils.writeANewAttribute("Competition for " + person.getName() + " " +
                        person.getSurname(), competition.getDate().toString());
            }

        } catch (SQLException | IOException e) {
            String message = "Error while writing a new Competition to the Database";
            logger.error(message, e);
            System.out.println(message);
        }

    }


    public static List<Person<LocalDate>> getPersonsWithEverything() {

        List<Person<LocalDate>> personsWithTrainings = setAndGetPersonsWithTheirCompletedTrainings();
        try (Connection connection = connectToDatabase()) {

            String competitionsSql = "SELECT * FROM COMPETITION";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(competitionsSql);

            while (rs.next()) {
                Integer personId = rs.getInt("PERSON_ID");
                Long clubId = rs.getLong("CLUB_ID");
                Integer competitionAddressId = rs.getInt("ADDRESS_ID");
                LocalDate competitionDate = rs.getDate("COMPETITION_DATE").toLocalDate();
                String competitionTime = rs.getString("COMPETITION_TIME");
                Address competitionAddress = getAddressForCompetition(competitionAddressId);
                Competition<Address, LocalDate> competition =
                        new Competition<>(competitionAddress, competitionDate, competitionTime);


                personsWithTrainings.stream()
                        .filter(person -> person.getId().equals(personId) && person.getClubId().equals(clubId))
                        .forEach(person -> {
                            /*if(!checkIfCompetitionFinished(person, competitionDate)) {*/
                                person.setCompetition(competition);
                            /*}*/
                        });


            }

        } catch (SQLException | IOException e) {
            String message = "Error while reading the Database trying to get Competitions!";
            logger.error(message, e);
            System.out.println(message);
        }
        return personsWithTrainings;
    }

    public static void checkIfCompetitionFinished(Person<LocalDate> person) {

            try (Connection connection = connectToDatabase()) {

                if(Optional.ofNullable(person.getCompetition()).isPresent()) {
                    if(person.getCompetition().getDate().isBefore(LocalDate.now())) {
                        String deleteCompetition = "DELETE FROM COMPETITION WHERE CLUB_ID = ?";
                        PreparedStatement ps = connection.prepareStatement(deleteCompetition);
                        ps.setLong(1, person.getClubId());
                        ps.executeUpdate();

                        String resetCompletedTrainings = "UPDATE COMPLETED_TRAININGS SET COMPLETED_TRAININGS = 0 WHERE" +
                                " CLUB_ID = ?";
                        PreparedStatement ps2 = connection.prepareStatement(resetCompletedTrainings);
                        ps2.setLong(1, person.getClubId());
                        ps2.executeUpdate();

                        person.setConditionsFulfilled("NO");
                        person.setCompletedTrainings(0);

                    }
                }




            } catch (SQLException | IOException e) {
                String message = "Error while reading the Database trying to get Competitions!";
                logger.error(message, e);
                System.out.println(message);
            }

    }



    public static void editPerson(List<String> attributesToChange, Integer personId, String newName,
                                  String newSurname, /*String newClubId,*/ LocalDate newMembershipDate) {

        try (Connection connection = connectToDatabase()) {

            StringBuilder editAPersonSql = new StringBuilder("UPDATE PERSON SET ");
            Map<Integer, Object> attributesMap = new HashMap<>();
            Integer paramOrdinal = 1;

            for (String attributeToChange : attributesToChange) {
                switch (attributeToChange) {
                    case "Name":
                        editAPersonSql.append("NAME = ?, ");
                        attributesMap.put(paramOrdinal, newName);
                        paramOrdinal++;
                        break;

                    case "Surname":
                        editAPersonSql.append("SURNAME = ?, ");
                        attributesMap.put(paramOrdinal, newSurname);
                        paramOrdinal++;
                        break;


                    case "Membership Date":
                        editAPersonSql.append("MEMBERSHIP_DATE = ?, ");
                        Date date = new Date(newMembershipDate.atStartOfDay()
                                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

                        attributesMap.put(paramOrdinal, date);
                        paramOrdinal++;
                        break;

                    default:
                        break;
                }
            }

            editAPersonSql.delete(editAPersonSql.length() - 2, editAPersonSql.length());
            editAPersonSql.append(" WHERE ID = ?");
            attributesMap.put(paramOrdinal, personId);

            PreparedStatement ps = connection.prepareStatement(editAPersonSql.toString());

            for (Integer key : attributesMap.keySet()) {
                if (attributesMap.get(key) instanceof Integer iPs) {
                    ps.setInt(key, iPs);
                }
                if (attributesMap.get(key) instanceof String sPs) {
                    ps.setString(key, sPs);
                } else if (attributesMap.get(key) instanceof Date dPs) {
                    ps.setDate(key, dPs);
                }
            }

            int rowResult = ps.executeUpdate();
            if (rowResult > 0) {
                editingSuccessAlert(newName, "person");
            }


        } catch (SQLException | IOException ex) {
            String message = "Error while reading the Database trying to edit a Person!";
            logger.error(message, ex);
            System.out.println(message);
        }

    }


    public static void deleteChosenPerson(Person<LocalDate> personToDelete) {
        try (Connection connection = connectToDatabase()) {

            String deletePersonsExercises = "DELETE FROM PERSON_TRAINING_RECORD WHERE PERSON_ID = ? AND CLUB_ID = ?";
            PreparedStatement deletePersonsExercisesPs = connection.prepareStatement(deletePersonsExercises);
            deletePersonsExercisesPs.setInt(1, personToDelete.getId());
            deletePersonsExercisesPs.setLong(2, personToDelete.getClubId());
            int deletedExercises = deletePersonsExercisesPs.executeUpdate();

            if (deletedExercises > 0) {
                String deletePersonsCompletedTrainings = "DELETE FROM COMPLETED_TRAININGS " +
                        "WHERE PERSON_ID = ? AND CLUB_ID = ?";
                PreparedStatement deletePersonsTrainingsPs = connection.prepareStatement(deletePersonsCompletedTrainings);
                deletePersonsTrainingsPs.setInt(1, personToDelete.getId());
                deletePersonsTrainingsPs.setLong(2, personToDelete.getClubId());
                deletePersonsTrainingsPs.executeUpdate();


                String deletePersonsCompetitions = "DELETE FROM COMPETITION " +
                        "WHERE PERSON_ID = ? AND CLUB_ID = ?";
                PreparedStatement deletePersonsCompetitionsPs = connection.prepareStatement(deletePersonsCompetitions);
                deletePersonsCompetitionsPs.setInt(1, personToDelete.getId());
                deletePersonsCompetitionsPs.setLong(2, personToDelete.getClubId());
                deletePersonsCompetitionsPs.executeUpdate();


                String deletePerson = "DELETE FROM PERSON " +
                        "WHERE ID = ? AND CLUB_ID = ?";
                PreparedStatement deletePersonPs = connection.prepareStatement(deletePerson);
                deletePersonPs.setInt(1, personToDelete.getId());
                deletePersonPs.setLong(2, personToDelete.getClubId());
                int rowResult = deletePersonPs.executeUpdate();
                if (rowResult > 0) {
                    deletingSuccessAlert("Name",
                            personToDelete.getName() + " " + personToDelete.getSurname());
                    FileUtils.deleteAnAttribute("Person", personToDelete.getName()
                            + " " + personToDelete.getSurname());
                }


            }


        } catch (SQLException | IOException ex) {
            String message = "Error while reading the Database trying to delete a Person!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }


    public static Map<String, List<ExerciseSetAndRepForEachPerson>> getExercisesForEachDay(Long clubId) {
        List<ExerciseSetAndRepForEachPerson> exerciseList;
        Map<String, List<ExerciseSetAndRepForEachPerson>> eachDayExerciseMap = new HashMap<>();
        Exercise readExercise;
        String dayName;
        try (Connection connection = connectToDatabase()) {
            String findDayIdSql = "SELECT * FROM DAY_OF_THE_WEEK";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(findDayIdSql);

            while (rs.next()) {
                int dayId = rs.getInt("DAY_ID");
                dayName = rs.getString("NAME");

                String eachDayExerciseSql = "SELECT * FROM PERSON_TRAINING_RECORD WHERE CLUB_ID = ? AND DAY_ID = ?";
                PreparedStatement eachDayExercisePs = connection.prepareStatement(eachDayExerciseSql);
                eachDayExercisePs.setLong(1, clubId);
                eachDayExercisePs.setInt(2, dayId);
                ResultSet eachDayExerciseRs = eachDayExercisePs.executeQuery();

                exerciseList = new ArrayList<>();
                while (eachDayExerciseRs.next()) {

                    int exerciseId = eachDayExerciseRs.getInt("EXERCISE_ID");

                    String findExerciseByIdSql = "SELECT * FROM EXERCISE WHERE ID = ?";
                    PreparedStatement findExercisePs = connection.prepareStatement(findExerciseByIdSql);
                    findExercisePs.setInt(1, exerciseId);
                    ResultSet foundExerciseRs = findExercisePs.executeQuery();
                    if (foundExerciseRs.next()) {
                        int readExerciseId = foundExerciseRs.getInt("ID");
                        String readExerciseName = foundExerciseRs.getString("NAME");
                        String readExerciseBodyEffect = foundExerciseRs.getString("BODY_EFFECT");
                        ExerciseBodyEffect bodyEffect = ExerciseBodyEffect.valueOf(readExerciseBodyEffect);
                        String readExerciseDescription = foundExerciseRs.getString("DESCRIPTION");
                        String readExerciseVideoLink = foundExerciseRs.getString("VIDEO_LINK");
                        readExercise = new Exercise(readExerciseId, readExerciseName, bodyEffect,
                                readExerciseDescription, readExerciseVideoLink);

                        if (readExercise.getName().equals("Rest")) {
                            ExerciseSetAndRepForEachPerson newRestExercise = new ExerciseSetAndRepForEachPerson.Builder()
                                    .setExercise(readExercise)
                                    .build();
                            exerciseList.add(newRestExercise);
                        } else {
                            int exerciseReps = eachDayExerciseRs.getInt("REPS");
                            int exerciseSets = eachDayExerciseRs.getInt("SETS");
                            ExerciseSetAndRepForEachPerson newExercise = new ExerciseSetAndRepForEachPerson.Builder()
                                    .setExercise(readExercise)
                                    .setRep(exerciseReps)
                                    .setSet(exerciseSets)
                                    .build();
                            exerciseList.add(newExercise);
                        }

                    }
                }
                eachDayExerciseMap.put(dayName, exerciseList);

            }

        } catch (SQLException | IOException ex) {
            String message = "Error while reading the Database trying to find Person's exercises for each day!";
            logger.error(message, ex);
            System.out.println(message);
        }
        return eachDayExerciseMap;
    }

    public static void membershipRenewal(LocalDate renewalDate, Integer id, Long clubId) {
        try (Connection connection = connectToDatabase()) {

            String membershipRenewalSql = "UPDATE PERSON SET MEMBERSHIP_DATE = ? WHERE ID = ? AND CLUB_ID = ?";
            PreparedStatement ps = connection.prepareStatement(membershipRenewalSql);
            ps.setDate(1, new Date(renewalDate.atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            ps.setInt(2, id);
            ps.setLong(3, clubId);
            int rowRes = ps.executeUpdate();
            if (rowRes > 0) {
                Alert membershipRenewalAlert = new Alert(Alert.AlertType.INFORMATION);
                membershipRenewalAlert.setTitle("Info");
                membershipRenewalAlert.setHeaderText("Membership renewal");
                membershipRenewalAlert.setContentText("Membership renewal was successful!");
                membershipRenewalAlert.showAndWait();
            }

        } catch (SQLException | IOException ex) {
            String message = "Error while reading the Database trying to do Membership renewal!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }


    public static void editPersonCompetition(List<String> attributesToChange, Integer addressId,
                                             LocalDate competitionDate, String competitionTime, String personName,
                                             String personSurname, Integer personId, Long clubId) {
        try (Connection connection = connectToDatabase()) {

            StringBuilder editAPersonSql = new StringBuilder("UPDATE COMPETITION SET ");
            Map<Integer, Object> attributesMap = new HashMap<>();
            Integer paramOrdinal = 1;

            for (String attributeToChange : attributesToChange) {
                switch (attributeToChange) {
                    case "Address":
                        editAPersonSql.append("ADDRESS_ID = ?, ");
                        attributesMap.put(paramOrdinal, addressId);
                        paramOrdinal++;
                        break;

                    case "Date":
                        editAPersonSql.append("COMPETITION_DATE = ?, ");
                        Date date = new Date(competitionDate.atStartOfDay()
                                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                        attributesMap.put(paramOrdinal, date);
                        paramOrdinal++;
                        break;

                    case "Time":
                        editAPersonSql.append("COMPETITION_TIME = ?, ");
                        attributesMap.put(paramOrdinal, competitionTime);
                        paramOrdinal++;
                        break;

                    default:
                        break;
                }
            }

            editAPersonSql.delete(editAPersonSql.length() - 2, editAPersonSql.length());
            editAPersonSql.append(" WHERE PERSON_ID = ? AND CLUB_ID = ?");
            attributesMap.put(paramOrdinal, personId);
            paramOrdinal++;
            attributesMap.put(paramOrdinal, clubId);

            PreparedStatement ps = connection.prepareStatement(editAPersonSql.toString());

            for (Integer key : attributesMap.keySet()) {
                if (attributesMap.get(key) instanceof Integer iPs) {
                    ps.setInt(key, iPs);
                }
                if (attributesMap.get(key) instanceof String sPs) {
                    ps.setString(key, sPs);
                } else if (attributesMap.get(key) instanceof Date dPs) {
                    ps.setDate(key, dPs);
                } else if (attributesMap.get(key) instanceof Long lPs) {
                    ps.setLong(key, lPs);
                }
            }

            int rowResult = ps.executeUpdate();
            if (rowResult > 0) {
                changingSuccessAlert("Competition", "for " +
                        personName + " " + personSurname);
            }


        } catch (SQLException | IOException ex) {
            String message = "Error while reading the Database trying to edit a Person Competition!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }

    public static void changeExercise(Person<LocalDate> chosenPerson, Day day,
                                      ExerciseSetAndRepForEachPerson oldExercise, Exercise newExercise,
                                      Integer sets, Integer reps) {
        try (Connection connection = connectToDatabase()) {

            try {
                checkDuplicateExerciseForPerson(chosenPerson.getClubId(), newExercise.getId(), day.getDayId());

                String changeExerciseSql = "UPDATE PERSON_TRAINING_RECORD SET EXERCISE_ID = ?, SETS = ?, REPS = ?" +
                        "WHERE PERSON_ID = ? AND CLUB_ID = ? AND EXERCISE_ID = ? AND DAY_ID = ?";

                PreparedStatement ps = connection.prepareStatement(changeExerciseSql);
                ps.setInt(1, newExercise.getId());
                if (newExercise.getName().equals("Rest")) {
                    ps.setString(2, "-");
                    ps.setString(3, "-");
                } else {
                    ps.setInt(2, sets);
                    ps.setInt(3, reps);
                }

                ps.setInt(4, chosenPerson.getId());
                ps.setLong(5, chosenPerson.getClubId());
                ps.setInt(6, oldExercise.getExercise().getId());
                ps.setInt(7, day.getDayId());

                int rowResult = ps.executeUpdate();
                if (rowResult > 0) {
                    changingSuccessAlert("Exercise", " for " +
                            chosenPerson.getName() + " " + chosenPerson.getSurname());
                    FileUtils.exerciseChange(chosenPerson.getName(), chosenPerson.getSurname(), oldExercise,
                            newExercise, sets, reps);
                }
            } catch (DuplicateExerciseException ex) {
                NewExerciseController.duplicateElement("Exercise", ex);
            }


        } catch (IOException | SQLException ex) {
            String message = "Error while reading the Database trying to write a change for Person's Exercise!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }

    private static void checkDuplicateExerciseForPerson(Long clubId, Integer exerciseId, Integer dayId) {
        try (Connection connection = connectToDatabase()) {
            String duplicateExerciseQuery = "SELECT * FROM PERSON_TRAINING_RECORD WHERE " +
                    "CLUB_ID = ? AND EXERCISE_ID = ? AND DAY_ID = ?";

            PreparedStatement ps = connection.prepareStatement(duplicateExerciseQuery);
            ps.setLong(1, clubId);
            ps.setInt(2, exerciseId);
            ps.setInt(3, dayId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                throw new DuplicateExerciseException("You're trying to add an Exercise that already exists, " +
                        "please try again!");
            }


        } catch (SQLException | IOException ex) {
            String message = "Error while reading the Database trying to find a Exercise duplicate!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }

    public static void changeOnlySetsAndRepsForExercise(Person<LocalDate> person,
                                                        ExerciseSetAndRepForEachPerson exercise,
                                                        Integer sets, Integer reps, Integer dayId) {

        try (Connection connection = connectToDatabase()) {
            String setsAndRepsOnlySql = "UPDATE PERSON_TRAINING_RECORD SET SETS = ?, REPS = ? " +
                    "WHERE PERSON_ID = ? AND CLUB_ID = ? AND EXERCISE_ID = ? AND DAY_ID = ?";

            PreparedStatement ps = connection.prepareStatement(setsAndRepsOnlySql);
            ps.setInt(1, sets);
            ps.setInt(2, reps);
            ps.setInt(3, person.getId());
            ps.setLong(4, person.getClubId());
            ps.setInt(5, exercise.getExercise().getId());
            ps.setInt(6, dayId);

            int rowResult = ps.executeUpdate();
            if (rowResult > 0) {
                changingSuccessAlert(exercise.getExercise().getName() + " Set And Rep",
                        " for " + person.getName() + " " + person.getSurname());
                FileUtils.exerciseSetsAndRepsChangeOnly(person.getName(), person.getSurname(), exercise, sets, reps);
            }

        } catch (SQLException | IOException ex) {
            String message = "Error while reading the Database trying to write only Sets And Reps for Exercise!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }

    public static void addNewExerciseToAlreadyExistingExercises(Person<LocalDate> chosenPerson, Day day,
                                                                Exercise newExercise, Integer sets, Integer reps) {

        try (Connection connection = connectToDatabase()) {

            try {
                checkDuplicateExerciseForPerson(chosenPerson.getClubId(), newExercise.getId(), day.getDayId());

                String newExerciseSql = "INSERT INTO PERSON_TRAINING_RECORD " +
                        "(PERSON_ID, CLUB_ID, DAY_ID, EXERCISE_ID, REPS, SETS) VALUES(?, ?, ?, ?, ?, ?)";

                PreparedStatement ps = connection.prepareStatement(newExerciseSql);
                ps.setInt(1, chosenPerson.getId());
                ps.setLong(2, chosenPerson.getClubId());
                ps.setInt(3, day.getDayId());
                ps.setInt(4, newExercise.getId());
                ps.setInt(5, reps);
                ps.setInt(6, sets);

                int rowResult = ps.executeUpdate();
                if (rowResult > 0) {
                    savingSuccessAlert(newExercise.getName(), "Exercise");
                    FileUtils.writeANewAttribute("Exercise for " + chosenPerson.getName() + " "
                            + chosenPerson.getSurname(), newExercise.getName()
                            + ", Sets: " + sets + ", Reps: " + reps);
                }
            } catch (DuplicateExerciseException ex) {
                NewExerciseController.duplicateElement("Exercise", ex);
            }

        } catch (IOException | SQLException ex) {
            String message = "Error while reading the Database trying to write a new Exercise to already" +
                    " existing Exercises!";
            logger.error(message, ex);
            System.out.println(message);
        }
    }


    public static void changingSuccessAlert(String valueToChange, String valueName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(valueToChange + " changing");
        alert.setHeaderText("Changing a " + valueToChange + " was successful!");
        alert.setContentText("You have successfully changed an " + valueToChange + valueName + "!");
        alert.showAndWait();
    }

    public static void savingSuccessAlert(String valueToSave, String valueName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(valueToSave + " saving");
        alert.setHeaderText("Saving a new" + valueToSave + " was successful!");
        alert.setContentText("You have successfully saved a " + valueName + " " + valueToSave + "!");
        alert.showAndWait();
    }

    public static void editingSuccessAlert(String editAttributeName, String editAttribute) {
        Alert editSuccessAlert = new Alert(Alert.AlertType.INFORMATION);
        editSuccessAlert.setTitle("Info");
        editSuccessAlert.setHeaderText("Edit was successful!");
        editSuccessAlert.setContentText("Editing an " + editAttributeName + " " + editAttribute + " was successful!");
        editSuccessAlert.showAndWait();
    }

    public static void deletingSuccessAlert(String deletedAttribute, String deletedAttributeName) {
        Alert deleteSuccessAlert = new Alert(Alert.AlertType.INFORMATION);
        deleteSuccessAlert.setTitle("Info");
        deleteSuccessAlert.setHeaderText("You've successfully deleted an " + deletedAttribute + "!");
        deleteSuccessAlert.setContentText("You've successfully deleted a " +
                deletedAttributeName + " " + deletedAttribute + "!");
        deleteSuccessAlert.showAndWait();
    }

    private static void attributeInUseAlert(String attributeInUse) {
        Alert exerciseInUseAlert = new Alert(Alert.AlertType.ERROR);
        exerciseInUseAlert.setTitle("Error");
        exerciseInUseAlert.setHeaderText("Trying to delete an " + attributeInUse + " that is in use");
        exerciseInUseAlert.setContentText("You may not delete an " + attributeInUse + " that is being in use!");
        exerciseInUseAlert.showAndWait();
    }

}
