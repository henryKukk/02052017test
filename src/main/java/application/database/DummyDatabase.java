package application.database;

import application.model.Customer;
import application.model.ParkInfo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by henry on 1.05.17.
 */
public class DummyDatabase {
    private static final ArrayList<Customer> customers = new ArrayList();


    public static ArrayList<Customer> getAllCustomers() {
        return customers;
    }

    public static void addCustomer(Customer customer) {
        customers.add(customer);
    }


    public static boolean addParkInfo(ParkInfo info) {
        Optional<Customer> c = getCustomerWithId(info.getCustomerID());
        if (c.isPresent()) {
            LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(info.getStartTime()), ZoneId.systemDefault());
            LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(info.getEndTime()), ZoneId.systemDefault());
            c.get().addParkingInfo(start, end, info.getParkHouseID());
            return true;
        }
        return false;
    }

    public static Optional<Customer> getCustomerWithId(int customerID) {
        return customers.stream().filter(customer -> customer.getCustomerID() == customerID).findFirst();
    }
}
