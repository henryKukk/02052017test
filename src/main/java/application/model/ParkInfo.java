package application.model;

/**
 * Created by henry on 1.05.17.
 */
public class ParkInfo {
    private int customerID;
    private long startTime;
    private long endTime;
    private int parkHouseID;
    private int groupID;


    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getParkHouseID() {
        return parkHouseID;
    }

    public void setParkHouseID(int parkHouseID) {
        this.parkHouseID = parkHouseID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getCustomerID() {
        return customerID;
    }
}
