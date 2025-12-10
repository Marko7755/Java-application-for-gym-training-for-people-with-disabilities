package hr.java.projekt.entity.login;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private String name;
    private String surname;
    private String username;
    private String hashPassword;
    private String role;

    public User(String name, String surname, String username, String hashPassword, String role) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.hashPassword = hashPassword;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(hashPassword, user.hashPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, hashPassword);
    }
}
