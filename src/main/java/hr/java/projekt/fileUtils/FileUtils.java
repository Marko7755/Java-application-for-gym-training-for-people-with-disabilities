package hr.java.projekt.fileUtils;

import hr.java.projekt.entity.changes.ChangesMade;
import hr.java.projekt.entity.model.Address;
import hr.java.projekt.entity.model.Exercise;
import hr.java.projekt.entity.model.ExerciseSetAndRepForEachPerson;
import hr.java.projekt.entity.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String CHANGES_MADE_FILE = "files/changes/changesMade.txt";
    private static final String ALL_CHANGES_MADE = "files/changes/allChangesMade.txt";
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);


    public static void writeANewAttribute(String newAttribute, String newAttributeName){
        List<ChangesMade<?>> changesMadeList = new ArrayList<>();

        ChangesMade<?> newExerciseAdd = new ChangesMade<>("New " + newAttribute,
                " ", newAttributeName);
        changesMadeList.add(newExerciseAdd);

        List<ChangesMade<?>> previousChangesList = readAllChangesFromBinaryFile();
        previousChangesList.addAll(changesMadeList);


        try (ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(ALL_CHANGES_MADE))) {
            /*for (ChangesMade<?> changesMade : changesMadeList) {
                oos2.writeObject(changesMade);
            }*/
            oos2.writeObject(previousChangesList);
        } catch (IOException e) {
            String message = "Error while trying to write a new " + newAttribute +
                    " addition to allChangesMade binary file!";
            logger.error(message, e);
            System.out.println(message);
        }
    }


    public static void membershipRenewal(String oldDate, String newDate, String personName, String personSurname){
        List<ChangesMade<?>> changesMadeList = new ArrayList<>();

        ChangesMade<?> newExerciseAdd = new ChangesMade<>("New membership renewal for " +
                personName + " " + personSurname, oldDate, newDate);
        changesMadeList.add(newExerciseAdd);

        List<ChangesMade<?>> previousChangesList = readAllChangesFromBinaryFile();
        previousChangesList.addAll(changesMadeList);


        try (ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(ALL_CHANGES_MADE))) {
            /*for (ChangesMade<?> changesMade : changesMadeList) {
                oos2.writeObject(changesMade);
            }*/
            oos2.writeObject(previousChangesList);
        } catch (IOException e) {
            String message = "Error while trying to write a new Membership renewal to allChangesMade binary file!";
            logger.error(message, e);
            System.out.println(message);
        }
    }

    public static void exerciseChange(String personName, String personSurname,
                                      ExerciseSetAndRepForEachPerson oldExercise, Exercise newExercise,
                                      Integer sets, Integer reps){
        List<ChangesMade<?>> changesMadeList = new ArrayList<>();
        ChangesMade<?> newExerciseAdd;

        if(oldExercise.getExercise().getName().equals("Rest")){
           newExerciseAdd = new ChangesMade<>("New Exercise Change for " +
                    personName + " " + personSurname,
                    oldExercise.getExercise().getName() + ", " + "Sets: -" + ", " +
                            "Reps: -",newExercise.getName() + ", " + "Sets: " + sets + ", Reps: " + reps);
        }
        else if(newExercise.getName().equals("Rest")){
            newExerciseAdd = new ChangesMade<>("New Exercise Change for " +
                    personName + " " + personSurname,
                    oldExercise.getExercise().getName() + ", " + "Sets: " + oldExercise.getSet() + ", " +
                            "Reps: " + oldExercise.getRep()
                    ,newExercise.getName() + ", " + "Sets: -" + ", " + "Reps: -");
        }

        else{
            newExerciseAdd = new ChangesMade<>("New Exercise Change for " +
                    personName + " " + personSurname,
                    oldExercise.getExercise().getName() + ", " + "Sets: " + oldExercise.getSet() + ", " +
                            "Reps: " + oldExercise.getRep(),newExercise.getName() + ", " +
                    "Sets:" + sets + ", " + "Reps:" + reps);
        }

        changesMadeList.add(newExerciseAdd);
        List<ChangesMade<?>> previousChangesList = readAllChangesFromBinaryFile();
        previousChangesList.addAll(changesMadeList);


        try (ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(ALL_CHANGES_MADE))) {
            /*for (ChangesMade<?> changesMade : changesMadeList) {
                oos2.writeObject(changesMade);
            }*/
            oos2.writeObject(previousChangesList);
        } catch (IOException e) {
            String message = "Error while trying to write a new Exercise Change to allChangesMade binary file!";
            logger.error(message, e);
            System.out.println(message);
        }
    }

    public static void exerciseSetsAndRepsChangeOnly(String personName, String personSurname,
                                                     ExerciseSetAndRepForEachPerson exercise,
                                                     Integer sets, Integer reps){
        List<ChangesMade<?>> changesMadeList = new ArrayList<>();

        ChangesMade<?> newExerciseAdd = new ChangesMade<>("New " + exercise.getExercise().getName()
                + " Sets and Reps for " + personName + " " + personSurname,"Sets: " + exercise.getSet()
                + ", Reps: " + exercise.getRep(), "Sets: " + sets + ", Reps: " + reps);
        changesMadeList.add(newExerciseAdd);

        List<ChangesMade<?>> previousChangesList = readAllChangesFromBinaryFile();
        previousChangesList.addAll(changesMadeList);


        try (ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(ALL_CHANGES_MADE))) {
            /*for (ChangesMade<?> changesMade : changesMadeList) {
                oos2.writeObject(changesMade);
            }*/
            oos2.writeObject(previousChangesList);
        } catch (IOException e) {
            String message = "Error while trying to write a new Set And Rep to allChangesMade binary file!";
            logger.error(message, e);
            System.out.println(message);
        }
    }

    public static void deleteAnAttribute(String attributeToDelete, String attributeNameToDelete){
        List<ChangesMade<?>> changesMadeList = new ArrayList<>();

        ChangesMade<?> newExerciseAdd =
                new ChangesMade<>(attributeToDelete + " delete", attributeNameToDelete, " ");
        changesMadeList.add(newExerciseAdd);

        List<ChangesMade<?>> previousChangesList = readAllChangesFromBinaryFile();
        previousChangesList.addAll(changesMadeList);


        try (ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(ALL_CHANGES_MADE))) {
            /*for (ChangesMade<?> changesMade : changesMadeList) {
                oos2.writeObject(changesMade);
            }*/
            oos2.writeObject(previousChangesList);
        } catch (IOException e) {
            String message = "Error while trying to write a new" + attributeToDelete +
                    " deletion to allChangesMade binary file!";
            logger.error(message, e);
            System.out.println(message);
        }
    }

    public static void writeExerciseChangesToBinaryFile(List<String> attributesToChange,
                                                        Exercise oldExercise, String newName,
                                                        String newBodyEffect, String newDescription,
                                                        String newVideoLink) {
        List<ChangesMade<?>> changesMadeList = new ArrayList<>();

        for (String attributeToChange : attributesToChange) {
            switch (attributeToChange) {
                case "Name":
                    changesMadeList.add(new ChangesMade<>("Exercise " + oldExercise.getName()
                            + " " + attributeToChange, oldExercise.getName(), newName));
                    break;

                case "Body Effect":
                    changesMadeList.add(new ChangesMade<>("Exercise " + oldExercise.getName()
                            + " " + attributeToChange, oldExercise.getBodyEffect().name(), newBodyEffect));
                    break;

                case "Description":
                    changesMadeList.add(new ChangesMade<>("Exercise " + oldExercise.getName()
                            + " " + attributeToChange, oldExercise.getDescription(), newDescription));
                    break;

                case "Video link":
                    changesMadeList.add(new ChangesMade<>("Exercise " + oldExercise.getName()
                            + " " + attributeToChange, oldExercise.getVideoLink(), newVideoLink));
                    break;

                default:
                    break;
            }
        }


        List<ChangesMade<?>> previousChangesList = readAllChangesFromBinaryFile();
        previousChangesList.addAll(changesMadeList);


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CHANGES_MADE_FILE))) {
           /* for (ChangesMade<?> changesMade : changesMadeList) {
                oos.writeObject(changesMade);
            }*/
            oos.writeObject(changesMadeList); //zapisuje samo zadnju promjenu
        } catch (IOException e) {
            String message = "Error while trying to write changesMade to a binary file!";
            logger.error(message, e);
            System.out.println(message);
        }

        try (ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(ALL_CHANGES_MADE))) {
            /*for (ChangesMade<?> changesMade : changesMadeList) {
                oos2.writeObject(changesMade);
            }*/
            oos2.writeObject(previousChangesList); //zapisuje sve promjene do sada + zadnju
        } catch (IOException e) {
            String message = "Error while trying to write a new Exercise to allChangesMade binary file!";
            logger.error(message, e);
            System.out.println(message);
        }
    }


    public static List<ChangesMade<?>> readLastChangeFromBinaryFile() {
        List<ChangesMade<?>> changesMadeList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CHANGES_MADE_FILE))) {
            try {
                //ne moze se citati objekt po objekt, vec sve kao jedna lista(prije citanja,
                // podatci u dat su pisani kao lista tj sve je spremljeno u jednu listu)
                changesMadeList = (ArrayList<ChangesMade<?>>) ois.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            catch (EOFException ex){
                return new ArrayList<>();
            }
           /* while (true) {
                try {
                    ChangesMade<?> changesMade = (ChangesMade<?>) ois.readObject();
                    changesMadeList.add(changesMade);
                } catch (EOFException e) {
                    // Reached end of file, break the loop
                    break;
                } catch (ClassNotFoundException e) {
                    String message = e.getMessage();
                    logger.error(message, e);
                    System.out.println(message);
                }
            }*/
        } catch (IOException e) {
            String message = "Error while trying to read changesMade from a changesMade binary file!";
            logger.error(message, e);
            System.out.println(message);
            e.printStackTrace();
        }
        return changesMadeList;
    }


    public static List<ChangesMade<?>> readAllChangesFromBinaryFile() {
        List<ChangesMade<?>> allChangesMadeList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ALL_CHANGES_MADE))) {
            try {
                allChangesMadeList = (ArrayList<ChangesMade<?>>) ois.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            catch (EOFException ex){
                return new ArrayList<>();
            }

            /*while (true) {
                try {
                    ChangesMade<?> changesMade = (ChangesMade<?>) ois.readObject();
                    allChangesMadeList.add(changesMade);
                } catch (EOFException e) {
                    // Reached end of file, break the loop
                    break;
                } catch (ClassNotFoundException e) {
                    String message = e.getMessage();
                    logger.error(message, e);
                    System.out.println(message);
                }
            }*/
        }
        catch (EOFException ex) {
           return new ArrayList<>();
        }
        catch (IOException e) {
            String message = "Error while trying to read changesMade from a allChangesMade binary file!";
            logger.error(message, e);
            System.out.println(message);
            e.printStackTrace();
        }
        return allChangesMadeList;
    }

    public static void writeAddressEditChangeToBinaryFile(List<String> attributesToChange, Address oldAddress,
                                                          String newStreet, String newCity, String newHall) {
        List<ChangesMade<?>> changesMadeList = new ArrayList<>();

        for (String attributeToChange : attributesToChange) {
            switch (attributeToChange) {
                case "Street":
                    changesMadeList.add(new ChangesMade<>("Address " + attributeToChange,
                            oldAddress.getStreet(), newStreet));
                    break;

                case "City":
                    changesMadeList.add(new ChangesMade<>("Address " + attributeToChange,
                            oldAddress.getCity(), newCity));
                    break;

                case "Hall":
                    changesMadeList.add(new ChangesMade<>("Address " + attributeToChange,
                            oldAddress.getHall().getName(), newHall));
                    break;

                default:
                    break;
            }
        }


        List<ChangesMade<?>> previousChangesList = readAllChangesFromBinaryFile();
        previousChangesList.addAll(changesMadeList);


        try (ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(ALL_CHANGES_MADE))) {
            /*for (ChangesMade<?> changesMade : changesMadeList) {
                oos2.writeObject(changesMade);
            }*/
            oos2.writeObject(previousChangesList);
        } catch (IOException e) {
            String message = "Error while trying to write a new Address change to allChangesMade binary file!";
            logger.error(message, e);
            System.out.println(message);
        }
    }



    public static void writeEditPersonToBinaryFile(List<String> attributesToChange, Person<LocalDate> oldPerson,
                                                   String newName, String newSurname, String newMembershipDate) {
        List<ChangesMade<?>> changesMadeList = new ArrayList<>();

        for (String attributeToChange : attributesToChange) {
            switch (attributeToChange) {
                case "Name":
                    changesMadeList.add(new ChangesMade<>(oldPerson.getName() + " " +
                            oldPerson.getSurname() + " " + attributeToChange, oldPerson.getName(), newName));
                    break;

                case "Surname":
                    changesMadeList.add(new ChangesMade<>(oldPerson.getName() + " " +
                            oldPerson.getSurname() + " " + attributeToChange,
                            oldPerson.getSurname(), newSurname));
                    break;

                case "Membership Date":
                    changesMadeList.add(new ChangesMade<>(oldPerson.getName() + " " +
                            oldPerson.getSurname() + " " + attributeToChange,
                            oldPerson.getMembershipDate().toString(), newMembershipDate));
                    break;

                default:
                    break;
            }
        }


        List<ChangesMade<?>> previousChangesList = readAllChangesFromBinaryFile();
        previousChangesList.addAll(changesMadeList);


        try (ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(ALL_CHANGES_MADE))) {
            /*for (ChangesMade<?> changesMade : changesMadeList) {
                oos2.writeObject(changesMade);
            }*/
            oos2.writeObject(previousChangesList);
        } catch (IOException e) {
            String message = "Error while trying to write a new Person change to allChangesMade binary file!";
            logger.error(message, e);
            System.out.println(message);
        }
    }

    public static void writePersonCompetitionChangeToBinaryFile(List<String> attributesToChange,
                                                                Person<LocalDate> oldPersonCompetition,
                                                                Address newAddress, LocalDate newDate,
                                                                String newTime) {
        List<ChangesMade<?>> changesMadeList = new ArrayList<>();

        for (String attributeToChange : attributesToChange) {
            switch (attributeToChange) {
                case "Address":
                    changesMadeList.add(new ChangesMade<>("Competition " + attributeToChange +
                            " for " + oldPersonCompetition.getName() + " " + oldPersonCompetition.getSurname(),
                            oldPersonCompetition.getCompetition().getAddress().getStreet() + ", " +
                                    oldPersonCompetition.getCompetition().getAddress().getCity() + ", " +
                                    oldPersonCompetition.getCompetition().getAddress().getHall(),
                            newAddress.getStreet() + ", " + newAddress.getCity() + ", "
                                    + newAddress.getHall()));
                    break;

                case "Date":
                    changesMadeList.add(new ChangesMade<>("Competition " + attributeToChange +
                            " for " + oldPersonCompetition.getName() + " " + oldPersonCompetition.getSurname(),
                            oldPersonCompetition.getCompetition().getDate().toString(), newDate.toString()));
                    break;

                case "Time":
                    changesMadeList.add(new ChangesMade<>("Competition " + attributeToChange +
                            " for " + oldPersonCompetition.getName() + " " + oldPersonCompetition.getSurname(),
                            oldPersonCompetition.getCompetition().getTime(), newTime));
                    break;

                default:
                    break;
            }
        }


        List<ChangesMade<?>> previousChangesList = readAllChangesFromBinaryFile();
        previousChangesList.addAll(changesMadeList);


        try (ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(ALL_CHANGES_MADE))) {
            /*for (ChangesMade<?> changesMade : changesMadeList) {
                oos2.writeObject(changesMade);
            }*/
            oos2.writeObject(previousChangesList);
        } catch (IOException e) {
            String message = "Error while trying to write a new Competition Edition for a Person " +
                    "to allChangesMade binary file!";
            logger.error(message, e);
            System.out.println(message);
        }
    }



}
