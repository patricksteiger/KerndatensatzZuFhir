package helper;

import java.util.Calendar;
import java.util.Date;

public class Helper {
    public static Date getDateFromGermanTime(String germanDate) {
        Calendar calendar = Calendar.getInstance();
        String[] dates = germanDate.split("\\.");
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[0]));
        // Months are 0-indexed.
        calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(dates[2]));
        return calendar.getTime();
    }

    public static boolean checkNonEmptyString(String s) {
        return s != null && !s.isEmpty();
    }
}
