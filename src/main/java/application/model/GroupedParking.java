package application.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by henry on 2.05.17.
 */
public class GroupedParking extends Parking {
    @Override
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.paymentAmount = totalAmount;
    }
}
