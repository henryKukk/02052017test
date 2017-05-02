package application.model;

import application.caluclator.DateHelper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by henry on 30.04.17.
 */
@JsonSerialize
public class Invoice {
    private ParkingList parkings;
    private BigDecimal amount;
    private Customer customer;
    private int year;
    private int month;
    private double monthlyFee;
    public void setYearAndMonth(LocalDateTime yearAndMonth) {
        this.month = yearAndMonth.getMonthValue();
        this.year = yearAndMonth.getYear();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setParkings(ParkingList parkings) {
        this.parkings = parkings;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ParkingList getParkings() {
        return parkings;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public void caluclateMonthlyFee(LocalDateTime month) {
        if (DateHelper.isMonthEnded(month)) {
            setMonthlyFee(20);
        } else {
            int daysInMonth = month.toLocalDate().lengthOfMonth();
            int currentDate = LocalDate.now().getDayOfMonth();
            double newValue = 20.0 / daysInMonth;
            newValue = newValue * currentDate;
            newValue = BigDecimal.valueOf(newValue).setScale(2, RoundingMode.HALF_UP).doubleValue();
            setMonthlyFee(newValue);
        }
    }
}
