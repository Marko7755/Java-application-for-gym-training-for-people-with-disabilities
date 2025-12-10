package hr.java.projekt.entity.login;

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class UserDataFileUtils {

    private static final String FILE_PATH = "files/login/loginData.txt";
    private static final Logger logger = LoggerFactory.getLogger(UserDataFileUtils.class);
    public static Set<User> getUsersFromFile(){
        Set<User> allUsers = new HashSet<>();
        try(BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))){
            Optional<String> optionalLine;
            while((optionalLine = Optional.ofNullable(br.readLine())).isPresent()){
                String line = optionalLine.get();
                List<String> seperatedWords = Arrays.asList(line.split(","));
                String name = seperatedWords.get(0);
                String surname = seperatedWords.get(1);
                String username = seperatedWords.get(2);
                String password = seperatedWords.get(3);
                String role = seperatedWords.get(4);

                User readUser = new User(name, surname, username, password, role);
                allUsers.add(readUser);
            }

        }
        catch (IOException ex){
            String message = "Error while trying to read the loginData.txt file!";
            logger.error(message, ex);
            System.out.println(message);
        }
        return allUsers;
    }
    public static void writeNewUserToFile(User user){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))){
             bw.write(user.getName() + "," + user.getSurname() + "," + user.getUsername()
            + "," + user.getHashPassword() + "," + user.getRole());
             bw.newLine();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Saving a new user");
            alert.setHeaderText("Saving a new user was successful!");
            alert.setContentText("Saving a new user " + user.getName() + " was successful!");
            alert.showAndWait();

        }
        catch (IOException ex){
            String message = "Error while trying to write to the loginData.txt file!";
            logger.error(message, ex);
            System.out.println(message);
        }

    }

}
