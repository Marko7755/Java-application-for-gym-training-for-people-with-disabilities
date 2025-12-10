package hr.java.projekt.entity.interfaces;

import hr.java.projekt.entity.changes.ChangesMade;

public sealed interface UserChecker permits ChangesMade {

    void checkUser();

}
