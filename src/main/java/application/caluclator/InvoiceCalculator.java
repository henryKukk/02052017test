package application.caluclator;

import application.model.Customer;
import application.model.Invoice;
import application.model.Parking;
import application.model.ParkingList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;


public class InvoiceCalculator {
    public ArrayList<Invoice> calculateInvoicesForMonth(Customer customer, LocalDateTime month) {
        ArrayList<Invoice> invoices = new ArrayList<>();
        LocalDateTime lastGeneration = customer.getLastInvoiceRequestDate();
        if (lastGeneration == null) {
            invoices.addAll(generateAllInvoices(customer, month));
            return invoices;
        }
        lastGeneration = lastGeneration.withDayOfMonth(1).plusMonths(1);
        if (lastGeneration.isAfter(month)) {
            invoices.add(generateInvoiceForDate(month, customer));
            return invoices;
        }
        while (ChronoUnit.MONTHS.between(lastGeneration, month) > 1) {
            lastGeneration = lastGeneration.plusMonths(1);
            invoices.add(generateInvoiceForDate(lastGeneration, customer));
        }
        invoices.add(generateInvoiceForDate(lastGeneration, customer));
        if (!DateHelper.isMonthEnded(month)) {
            month = month.minusMonths(1);
        }
        customer.setLastInvoiceGeneration(month.withDayOfMonth(month.toLocalDate().lengthOfMonth()));
        return invoices;
    }

    private ArrayList<Invoice> generateAllInvoices(Customer customer, LocalDateTime month) {
       customer.getAllParkings().sort(Comparator.comparing(Parking::getStartTime));
       Parking first = customer.getAllParkings().get(0);
       ArrayList<Invoice> invoices = new ArrayList<>();
       LocalDateTime startDate = first.getStartTime();
       while (!DateHelper.isInSameMonth(startDate, month)) {
           invoices.add(generateInvoiceForDate(startDate, customer));
           startDate = startDate.plusMonths(1);
       }
        invoices.add(generateInvoiceForDate(startDate, customer));
        if (DateHelper.isMonthEnded(month)) {
           customer.setLastInvoiceGeneration(month);
       } else {
           customer.setLastInvoiceGeneration(month.minusMonths(1));
       }
       return invoices;
    }

    private Invoice generateInvoiceForDate(LocalDateTime month, Customer customer) {
        ParkingList inMonth = customer.getAllParkingsInMonth(month);
        ParkingList groupedInMonth = customer.getAllGroupedParkings(month);
        Invoice invoice = new Invoice();
        invoice.setYearAndMonth(month);
        invoice.setCustomer(customer);
        invoice.setParkings(groupedInMonth);
        BigDecimal total = inMonth.getTotal();
        invoice.setAmount(total);
        if (customer.isPremiumCustomer()) {
            invoice.caluclateMonthlyFee(month);
            double totalAmount = total.doubleValue();
            if (totalAmount + invoice.getMonthlyFee() >= 300) {
                totalAmount = 300 - invoice.getMonthlyFee();
                invoice.setAmount(BigDecimal.valueOf(totalAmount).setScale(2, RoundingMode.HALF_UP));
            }
        }
        return invoice;
    }

}
