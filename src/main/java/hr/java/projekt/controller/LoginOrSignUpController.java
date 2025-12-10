package hr.java.projekt.controller;

import hr.java.projekt.HelloApplication;
import hr.java.projekt.entity.login.PasswordHasher;
import hr.java.projekt.entity.login.User;
import hr.java.projekt.entity.login.UserDataFileUtils;
import hr.java.projekt.exceptions.NoMatchingUserException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public class LoginOrSignUpController {

    private static final Logger logger = LoggerFactory.getLogger(LoginOrSignUpController.class);

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    public static User enteredUser;
    public void loginClicked(){

        boolean isEmpty = false;
        if(usernameTextField.getText().isEmpty()){
            SignUpController.emptyTextFieldWarningAlert("Username");
            isEmpty = true;
        }
        if(passwordPasswordField.getText().isEmpty()){
            SignUpController.emptyPasswordFieldWarningAlert("Password");
            isEmpty = true;
        }

       if(!usernameTextField.getText().isEmpty() && !passwordPasswordField.getText().isEmpty()){
           isEmpty = false;
       }

       if(!isEmpty){
            String username = usernameTextField.getText();
            String password = passwordPasswordField.getText();
            String hashedPassword = PasswordHasher.hashPassword(password);

            try{
                checkIfUserMatches(username, hashedPassword);
            }
            catch (NoMatchingUserException ex){
                String message = ex.getMessage();
                logger.error(message);
                wrongUsernameOrPasswordAlert(message);
            }


        }

    }

    private void checkIfUserMatches(String username, String hashedPassword) throws NoMatchingUserException{
        Set<User> allUsersFromFile = UserDataFileUtils.getUsersFromFile();
        Optional<User> matchingUserOptional = allUsersFromFile.stream()
                .filter((user) -> user.getUsername().equals(username) && user.getHashPassword().equals(hashedPassword))
                .findFirst();
        if(matchingUserOptional.isPresent()){
            enteredUser = matchingUserOptional.get();
            MenuBarController.showMainMenuScreen();
            successfulSignUpAlert();
        }
        else{
            throw new NoMatchingUserException("Wrong username or password, please try again or " +
                    "sign up!");
        }
    }

    private static void wrongUsernameOrPasswordAlert(String message) {
        Alert noMatchingUserAlert = new Alert(Alert.AlertType.ERROR);
        noMatchingUserAlert.setTitle("Error");
        noMatchingUserAlert.setHeaderText("Wrong input");
        noMatchingUserAlert.setContentText(message);
        noMatchingUserAlert.showAndWait();
    }

    private static void successfulSignUpAlert() {
        Alert loginSuccess = new Alert(Alert.AlertType.INFORMATION);
        loginSuccess.setTitle("Success");
        loginSuccess.setHeaderText("Login successful!");
        loginSuccess.setContentText("Login for user " + enteredUser.getName() + " was successful!");
        loginSuccess.showAndWait();
    }

    public void signUpClicked(){
        showSignUpScreen();
    }


    public static void showSignUpScreen(){
        FXMLLoader signUpFXMLLoader = new FXMLLoader(HelloApplication.class.getResource("signUp.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(signUpFXMLLoader.load(), 600, 444);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public static void showLoginScreen(){
        FXMLLoader signUpFXMLLoader = new FXMLLoader(HelloApplication.class.getResource("loginOrRegister.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(signUpFXMLLoader.load(), 600, 359);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

}
