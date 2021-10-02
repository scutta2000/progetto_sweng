package org.pietroscuttari.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date now() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date years(int n) {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, n);
        return calendar.getTime();
    }

    public static Date oneDay() {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date oneWeek() {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        return calendar.getTime();
    }

    public static Duration sub(Date start, Date end) {
        return Duration.ofMillis(end.getTime() - start.getTime());
    }
}
