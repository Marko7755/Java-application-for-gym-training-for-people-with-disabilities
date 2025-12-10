package hr.java.projekt.entity.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    private static final Logger logger = LoggerFactory.getLogger(PasswordHasher.class);
    public static String hashPassword(String password){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            //Convert bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(byte b : hashedBytes){
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        }
        catch (NoSuchAlgorithmException ex){
            String message = "Error while trying to hash a password!";
            logger.error(message, ex);
            System.out.println(message);
            return null;
        }
    }

}
