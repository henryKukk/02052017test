package application.model;

import application.caluclator.DateHelper;
import application.constants.Constants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * Created by henry on 30.04.17.
 */
public class Customer {
    protected CustomerType type;
    protected final ParkingList parkings;
    private int customerID;
    private String name;
    private int lastGroupID;
    private LocalDateTime lastInvoiceRequestTime;
    public Customer() {
        type = CustomerType.REGULAR_CUSTOMER;
        this.parkings = new ParkingList();
    }

    protected Customer(CustomerType type) {
        this.type = type;
        this.parkings = new ParkingList();
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public boolean isPremiumCustomer() {
        return type == CustomerType.PREMIUM_CUSTOMER;
    }

    public ParkingList getAllParkingsInMonth(LocalDateTime month) {
        return parkings.getAllInCertainMonth(month);
    }

    public CustomerType getType() {
        return type;
    }



    public ParkingList getAllParkings() {
        return parkings;
    }


    /*
    * Group together different parkings created by the system in order to make the information more graspable
    * */

    public ParkingList getGroupedParkings() {
        ParkingList grouped = new ParkingList();
        GroupedParking groupedObject;
        for (int i = 0; i < lastGroupID; i++) {
            groupedObject = new GroupedParking();
            ParkingList withID = parkings.getAllWithID(i);
            withID.sort(Comparator.comparing(Parking::getStartTime));
            LocalDateTime earliestStart = withID.get(0).getStartTime();
            withID.sort(Comparator.comparing(Parking::getEndTime));
            LocalDateTime latestEnd = withID.get(withID.size() - 1).getEndTime();
            groupedObject.setStartTime(earliestStart);
            groupedObject.setEndTime(latestEnd);
            groupedObject.setTotalAmount(withID.getTotal());
            groupedObject.setGroupID(i);
            grouped.add(groupedObject);
        }
        grouped.sort(Comparator.comparing(Parking::getStartTime));
        return grouped;
    }

    public void setLastInvoiceGeneration(LocalDateTime lastInvoiceGeneration) {
        this.lastInvoiceRequestTime = lastInvoiceGeneration;
    }

    public LocalDateTime getLastInvoiceRequestDate() {
        return lastInvoiceRequestTime;
    }


    public ParkingList getAllGroupedParkings(LocalDateTime month) {
        return getGroupedParkings().getAllInCertainMonth(month);
    }

    /*
    * Adding parking information to customer
    * */

    public BigDecimal addParkingInfo(LocalDateTime start, LocalDateTime end, int parkHouseID) {
        BigDecimal total = BigDecimal.ZERO;
        if (DateHelper.singleDayParking(start, end)) {
            total = total.add(addSingleDayParking(start, end, parkHouseID));
        } else {
            while (!DateHelper.singleDayParking(start, end)) {
                LocalDateTime newEnd = start.withHour(23).withMinute(59);
                total = total.add(addSingleDayParking(start, newEnd, parkHouseID));
                start = start.plusDays(1).withHour(00).withMinute(00);
            }
            total = total.add(addSingleDayParking(start, end, parkHouseID));
        }
        lastGroupID++;
        return total;
    }

    private BigDecimal addSingleDayParking(LocalDateTime start, LocalDateTime end, int parkHouseID) {
        LocalDateTime discountStart = start.withHour(Constants.DISCOUNT_START_HOURS).withMinute(0);
        LocalDateTime discountEndMorning = start.withHour(Constants.DISCOUNT_END_HOURS).withMinute(0);
        BigDecimal total = BigDecimal.ZERO;
        if (start.isBefore(discountEndMorning)) {
            if (end.isAfter(discountEndMorning)) {
                total = total.add(addParkingPeriod(start, discountEndMorning, true, parkHouseID));
                if (end.isAfter(discountStart)) {
                    total = total.add(addParkingPeriod(discountEndMorning, discountStart, false, parkHouseID));
                    total = total.add(addParkingPeriod(discountStart, end, true, parkHouseID));
                } else {
                    total = total.add(addParkingPeriod(discountEndMorning, end, false, parkHouseID));
                }
                return total;
            } else {
                total = total.add(addParkingPeriod(start, end, true, parkHouseID));
                return total;
            }
        } else {
            if (end.isAfter(discountStart)) {
                if (start.isBefore(discountStart)) {
                    total = total.add(addParkingPeriod(start, discountStart, false, parkHouseID));
                    total = total.add(addParkingPeriod(discountStart, end, true, parkHouseID));
                    return total;
                } else {
                    total = total.add(addParkingPeriod(start, end, true, parkHouseID));
                    return total;
                }
            } else {
                total = total.add(addParkingPeriod(start, end, false, parkHouseID));
                return total;
            }
        }

    }

    private BigDecimal addParkingPeriod(LocalDateTime start, LocalDateTime end, boolean isDiscount, int parkHouseID) {
        Parking p = new Parking();
        p.setParkHouseId(parkHouseID);
        p.setUserType(type);
        p.setStartTime(start);
        p.setIsDiscount(isDiscount);
        p.setGroupID(lastGroupID);
        p.setEndTime(end);
        parkings.add(p);
        return p.getPaymentAmount();
    }
    /*
    * End of adding parking information to customer
    * */
}
