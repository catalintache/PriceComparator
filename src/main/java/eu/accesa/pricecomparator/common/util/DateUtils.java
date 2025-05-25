package eu.accesa.pricecomparator.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public static boolean withinLast24Hours(LocalDate date){
        LocalDateTime dateTime = date.atStartOfDay();
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        return dateTime.isAfter(cutoff);
    }

    public static boolean withinLast24Hours(Date date){
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        return dateTime.isAfter(cutoff);
    }
}
