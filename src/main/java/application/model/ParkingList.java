package application.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by henry on 30.04.17.
 */
public class ParkingList extends ArrayList<Parking> {
    /*
    * Used to get all parkings which are in this given month. If the start and end are not in the same month
    * it is presumed that the parking will be invoiced on the enddate (e.g. start 31. Jan end 02. Feb, will be invoiced on the
    * 2nd of february and on the invoice for February.
    * */
    ParkingList getAllInCertainMonth(LocalDateTime timeToCheck) {
        int monthToGet = timeToCheck.getMonthValue();
        int yearToGet = timeToCheck.getYear();
        return this.stream().filter(parking -> parking.getEndTime().getYear() == yearToGet
                && parking.getEndTime().getMonthValue() == monthToGet)
                .collect(Collectors.toCollection(ParkingList::new));
    }

    public ParkingList getAllWithID(int groupID) {
        return this.stream().filter(parking -> parking.getGroupID() == groupID).collect(Collectors.toCollection(ParkingList::new));
    }

    public BigDecimal getTotal() {
        if (this.size() == 0) {
            return BigDecimal.ZERO;
        }
        return this.stream()
                .map(parking -> parking.getPaymentAmount())
                .reduce((x , t) -> x.add(t)).get();
    }
}
