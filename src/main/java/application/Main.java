package application;

import application.database.DummyDatabase;
import application.model.Customer;
import application.model.PremiumCustomer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

/**
 * Created by henry on 30.04.17.
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        addInitialData();
        SpringApplication.run(Main.class, args);
    }

    /*
    * Dummy data for testing purpose
    * Two users and their respective parking information. Taken from the examples.
    * */
    private static void addInitialData() {
        Customer c = new Customer();
        c.setName("Bob");
        c.setCustomerID(99);
        DummyDatabase.addCustomer(c);
        c.addParkingInfo(LocalDateTime.of(2017, 04, 30, 8, 12),
                LocalDateTime.of(2017, 04, 30, 10, 45), 0);

        c.addParkingInfo(LocalDateTime.of(2017, 04, 15, 19, 40),
                LocalDateTime.of(2017, 04, 15, 20, 35),
                0);

        Customer k = new PremiumCustomer();
        k.setName("Alice");
        k.setCustomerID(99);
        DummyDatabase.addCustomer(k);

        k.addParkingInfo(LocalDateTime.of(2017, 04, 10, 8, 12),
                LocalDateTime.of(2017, 04, 10, 10, 45), 0);


        k.addParkingInfo(LocalDateTime.of(2017, 04, 13, 07, 02),
                LocalDateTime.of(2017, 04, 13, 11, 56), 0);

        k.addParkingInfo(LocalDateTime.of(2017, 03, 13, 05, 02),
                LocalDateTime.of(2017, 03, 13, 11, 56), 0);

        k.addParkingInfo(LocalDateTime.of(2017, 04, 18, 22, 10),
                LocalDateTime.of(2017, 04, 18, 22, 35), 0);



        k.addParkingInfo(LocalDateTime.of(2017, 04, 23, 19, 40),
                LocalDateTime.of(2017, 04, 23, 20, 35), 0);


        k.addParkingInfo(LocalDateTime.of(2017, 05, 02, 19, 40),
                LocalDateTime.of(2017, 05, 02, 20, 35), 0);
    }
}
