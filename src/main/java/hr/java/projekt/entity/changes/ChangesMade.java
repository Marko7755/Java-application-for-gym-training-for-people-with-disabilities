package hr.java.projekt.entity.changes;

import hr.java.projekt.controller.LoginOrSignUpController;
import hr.java.projekt.entity.interfaces.UserChecker;
import hr.java.projekt.entity.login.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public non-sealed class ChangesMade<T> implements Serializable, UserChecker {

    //Maintain Serialization Compatibility: Ensure that the serialVersionUID in your class (ChangesMade)
    // matches the one used when the data was serialized.
    // You can do this by explicitly declaring the serialVersionUID in your class:
    @Serial
    private static final long serialVersionUID = -625135877758012820L;
    //

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final String attributeName;

    private final T oldValue;

    private final T newValue;

    private String userRole;

    private final String dateAndTimeOfEdition;



   /* public ChangesMade(String attributeName, T oldValue, T newValue) {
        this.attributeName = attributeName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.dateAndTimeOfEdition = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        this.userRole = LoginOrSignUpController.enteredUser.getRole();
    }*/

    public ChangesMade(String attributeName, T oldValue, T newValue) {
        this.attributeName = attributeName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        //this.userRole = LoginOrSignUpController.enteredUser.getRole();
        checkUser();
        this.dateAndTimeOfEdition = LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }



    public String getAttributeName() {
        return attributeName;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }

        public String  getUser() {
        return userRole;
    }

    public String getDateAndTimeOfEdition() {
        return dateAndTimeOfEdition;
    }


    @Override
    public void checkUser(){
        //ako se user izlogira i ako se registrira novi korisnik, u promjenama ce pisati da je promjenu za registraciju
        //napravio admin, jer da ovog ispod nema, ne bi se znalo koji user je napravio promjenu i ne bi se ispravno
        // ispisala promjena u Changes Made
        if(Optional.ofNullable(LoginOrSignUpController.enteredUser).isEmpty()){
            this.userRole = "admin";
        }
        else{
            this.userRole = LoginOrSignUpController.enteredUser.getRole();
        }
    }

    public String toCustomString() {
        return attributeName + "\n" +
                "Old Value: " + oldValue + "\n" +
                "New Value: " + newValue + "\n" +
                "User Role: " + userRole + "\n" +
                "Date And Time Of Edition: " + dateAndTimeOfEdition;
    }
}



