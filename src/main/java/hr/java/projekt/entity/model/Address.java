package hr.java.projekt.entity.model;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.enums.Hall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Address implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Address.class);
    private Integer id;
    private String street;
    private String city;
    private Hall hall;

    private Address() {
    }

    public static class Builder {
        private Integer id;
        private String street;
        private String city;
        private Hall hall;

        public Builder() {
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setHall(Hall hall) {
            this.hall = hall;
            return this;
        }

        public Address build() {
            Address address = new Address();
            address.id = id;
            address.street = street;
            address.city = city;
            address.hall = hall;
            return address;
        }
    }

    public Integer getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public Hall getHall() {
        return hall;
    }


    public static Optional<Integer> getNextAddressId() {
        Integer id = null;
        try (Connection connection = Database.connectToDatabase()) {
            String nextExerciseIdQuery = "SELECT COALESCE(MAX(ID), 1) AS max_id FROM ADDRESS;";
            //The COALESCE function is used to handle the case where the result of MAX(ID) is NULL,
            // ensuring that the query always returns a value.
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(nextExerciseIdQuery);

            if (rs.next()) {
                id = rs.getInt("max_id");
            }

        } catch (IOException | SQLException ex) {
            String message = "Error while reading the database trying to find next adress id!";
            logger.error(message, ex);
            System.out.println(message);
        }
        return Optional.ofNullable(id);
    }

    public static List<Address> readAddressesFromDatabase(ResultSet rs) throws SQLException {
        List<Address> addressList = new ArrayList<>();
        while(rs.next()){
            Integer id = rs.getInt("ID");
            String street = rs.getString("STREET");
            String city = rs.getString("CITY");
            String hallString = rs.getString("HALL");
            Hall hall = Hall.valueOf(hallString);

            Address address = new Address.Builder()
                    .setId(id)
                    .setStreet(street)
                    .setCity(city)
                    .setHall(hall)
                    .build();
            addressList.add(address);
        }
        return  addressList;
    }


    @Override
    public String toString() {
        return this.getStreet();
    }
}