package application.model;

import application.constants.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by henry on 30.04.17.
 */
public class Parking {
    private LocalDateTime startTime;
    protected LocalDateTime endTime;
    private int groupID;
    private boolean isDiscountPeriod;
    // It is important to know whether the user was a premium or regular customer at the time of this parking
    // Can be that the customer has moved from regular to premium or vice versa.
    private CustomerType type;
    protected BigDecimal paymentAmount;
    private int parkHouseId;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        calculateAmount();
    }


    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public int getParkHouseId() {
        return parkHouseId;
    }

    public void setParkHouseId(int parkHouseId) {
        this.parkHouseId = parkHouseId;
    }

    public CustomerType getUserType() {
        return type;
    }

    public void setUserType(CustomerType type) {
        this.type = type;
    }

    /*
    * Basic money amount calculator;
    * */
    private void calculateAmount() {
        long minutes = ChronoUnit.MINUTES.between(startTime, endTime);
        int parkingIntervals = (int) Math.ceil(minutes / Constants.TIME_UNIT);
        double multiplier = getMultiplier();
        paymentAmount = BigDecimal.valueOf(parkingIntervals * multiplier).setScale(2, RoundingMode.HALF_UP);;
    }



    public double getMultiplier() {
        if (isDiscountPeriod && type == CustomerType.PREMIUM_CUSTOMER) return Constants.PREMIUM_DISCOUNT_TIME;
        if (isDiscountPeriod && type == CustomerType.REGULAR_CUSTOMER) return Constants.REGULAR_DISCOUNT_TIME;
        if (!isDiscountPeriod && type == CustomerType.PREMIUM_CUSTOMER) return Constants.PREMIUM_NORMAL_TIME;
        return Constants.REGULAR_NORMAL_TIME;
    }

    public void setIsDiscount(boolean isDiscount) {
        this.isDiscountPeriod = isDiscount;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getGroupID() {
        return groupID;
    }
}
