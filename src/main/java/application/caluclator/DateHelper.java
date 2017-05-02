package application.caluclator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class DateHelper {
    public static boolean singleDayParking(LocalDateTime start, LocalDateTime end) {
        return start.getYear() == end.getYear() && start.getDayOfYear() == end.getDayOfYear();
    }

    public static boolean isMonthEnded(LocalDateTime month) {
        LocalDateTime now = LocalDateTime.now();
        if (now.getYear() > month.getYear()) return true;
        if (now.getMonthValue() > month.getMonthValue() &&
                now.getYear() >= month.getYear()) return true;
        return false;
    }

    static boolean isInSameMonth(LocalDateTime startDate, LocalDateTime month) {
        return startDate.getYear() == month.getYear() && startDate.getMonthValue() == month.getMonthValue();
    }
}
