package application.controller;

import application.caluclator.InvoiceCalculator;
import application.database.DummyDatabase;
import application.model.Customer;
import application.model.Invoice;
import application.model.ParkInfo;
import application.model.ParkingList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by henry on 1.05.17.
 */
@Controller
@RequestMapping(value = "rest/")
public class RestController {
    @RequestMapping(value = "get_all_customers", method = RequestMethod.GET)
    public @ResponseBody
    ArrayList<Customer> getAllCustomers() {
        return DummyDatabase.getAllCustomers();
    }
    @RequestMapping(value = "get_all_parkhouses", method = RequestMethod.GET)
    public @ResponseBody ArrayList<Integer> getAllParkHouses() {
        return new ArrayList<>(0);
    }
    @RequestMapping(value = "add_parking", method = RequestMethod.POST)
    public @ResponseBody boolean addParkingInfo(@RequestBody ParkInfo info) {
        return DummyDatabase.addParkInfo(info);
    }
    @RequestMapping(value = "get_user_parkings", method = RequestMethod.GET)
    public @ResponseBody
    ParkingList getUserParkings(@RequestParam int userID) {
        Optional<Customer> customer = DummyDatabase.getCustomerWithId(userID);
        if (customer.isPresent()) {
            return customer.get().getGroupedParkings();
        }
        return null;
    }

    @RequestMapping(value = "add_customer", method = RequestMethod.POST)
    public @ResponseBody void addCustomer(@RequestBody Customer toAdd) {
        DummyDatabase.addCustomer(toAdd);
    }

    @RequestMapping(value = "get_user_parkings_details", method = RequestMethod.GET)
    public @ResponseBody
    ParkingList getUserParkingsDetails(@RequestParam int userID) {
        Optional<Customer> customer = DummyDatabase.getCustomerWithId(userID);
        if (customer.isPresent()) {
            return customer.get().getAllParkings();
        }
        return null;
    }

    @RequestMapping(value = "get_invoice", method = RequestMethod.GET)
    public @ResponseBody ArrayList<Invoice> geInvoices(@RequestParam int userID, @RequestParam long timestamp) {
        Optional<Customer> customer = DummyDatabase.getCustomerWithId(userID);
        if (customer.isPresent()) {
            return getInvoices(customer.get(), timestamp);
        }
        return null;
    }

    private ArrayList<Invoice> getInvoices(Customer customer, long timestamp) {
        InvoiceCalculator invoiceCalculator = new InvoiceCalculator();
        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        ArrayList<Invoice> invoices = invoiceCalculator.calculateInvoicesForMonth(customer, start);
        return invoices;
    }

}
