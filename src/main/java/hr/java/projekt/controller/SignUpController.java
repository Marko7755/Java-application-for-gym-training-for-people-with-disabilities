package hr.java.projekt.controller;

import hr.java.projekt.entity.login.PasswordHasher;
import hr.java.projekt.entity.login.User;
import hr.java.projekt.entity.login.UserDataFileUtils;
import hr.java.projekt.exceptions.UserAlreadyExistsException;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;

public class SignUpController {

    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

     @FXML
     private TextField nameTextField;
     @FXML
     private TextField surnameTextField;
     @FXML
     private TextField usernameTextField;
     @FXML
     private PasswordField passwordPasswordField;
     @FXML
     private Button loginNowButton;
     @FXML
     private Button goBackButton;
     public void signUp(){

         boolean alreadyExists = false;
         boolean isEmpty = false;
         if(nameTextField.getText().isEmpty()){
             emptyTextFieldWarningAlert("Name");
             isEmpty = true;
         }
         if(surnameTextField.getText().isEmpty()){
             emptyTextFieldWarningAlert("Surname");
             isEmpty = true;
         }
         if(usernameTextField.getText().isEmpty()){
             emptyTextFieldWarningAlert("Username");
             isEmpty = true;
         }
         if(passwordPasswordField.getText().isEmpty()){
             emptyPasswordFieldWarningAlert("Password");
             isEmpty = true;
         }

         /*if(!nameTextField.getText().isEmpty() && !surnameTextField.getText().isEmpty() &&
            !usernameTextField.getText().isEmpty() && !passwordPasswordField.getText().isEmpty()){
             isEmpty = false;
            }*/

         if(!isEmpty){
             String name = nameTextField.getText();
             String surname = surnameTextField.getText();
             String username = usernameTextField.getText();
             String password = passwordPasswordField.getText();
             String hashedPassword = PasswordHasher.hashPassword(password);


             try{
                 checkIfUserAlreadyExists(username);
             }
             catch (UserAlreadyExistsException ex){
                 alreadyExists = true;
                 String message = ex.getMessage();
                 logger.error(message);
                 userAlreadyExistsAlert(message);
                 usernameTextField.clear();
             }

             if(!alreadyExists){
                 User newUser = new User(name, surname, username, hashedPassword, "user");
                 UserDataFileUtils.writeNewUserToFile(newUser);
                 FileUtils.writeANewAttribute("User", newUser.getName() +
                         newUser.getSurname());
                 loginNowButton.setVisible(true);
                 nameTextField.clear();
                 surnameTextField.clear();
                 usernameTextField.clear();
                 passwordPasswordField.clear();
                 goBackButton.setVisible(false);
             }

         }
     }

    private static void userAlreadyExistsAlert(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Invalid input for a new User!");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }


    private void checkIfUserAlreadyExists(String enteredUsername) throws UserAlreadyExistsException{
         Set<User> allUsers = UserDataFileUtils.getUsersFromFile();
         Optional<User> userAlreadyExistsOptional = allUsers.stream()
                 .filter(user -> user.getUsername().equals(enteredUsername))
                 .findAny();

         if(userAlreadyExistsOptional.isPresent()){
             throw new UserAlreadyExistsException("The user " + enteredUsername + " already exists! Please try again.");
         }

     }

     public void loginNow(){
        LoginOrSignUpController.showLoginScreen();
     }

     public void goBackToLoginBtn() {
         LoginOrSignUpController.showLoginScreen();
     }

    public static void emptyTextFieldWarningAlert(String emptyTextField) {
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setTitle("Invalid input");
        warningAlert.setHeaderText("Empty text field!");
        warningAlert.setContentText(emptyTextField + " text field must not be empty!");
        warningAlert.showAndWait();
    }

    public static void emptyPasswordFieldWarningAlert(String emptyPasswordField) {
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setTitle("Invalid input");
        warningAlert.setHeaderText("Empty password field!");
        warningAlert.setContentText(emptyPasswordField + " field must not be empty!");
        warningAlert.showAndWait();
    }

}
