package helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

public class Helper {

    /**
     * Parses ISO-8601 formatted date to Date-object.
     *
     * @param date formatted according to ISO-8601
     * @return Date object at given date
     * @throws DateTimeParseException if date is not ISO-8601 formatted
     * @throws NullPointerException   if date is null
     */
    public static Date getDateFromISO(String date) {
        String isoDate = date;
        // Example: 2020-07-21
        final int simpleDateLength = 10;
        // Add timestamp if needed to avoid parsing exception
        if (isoDate.length() <= simpleDateLength)
            isoDate += "T00:00:00";
        // Use LocalDateTime to properly parse ISO 8601-Date
        LocalDateTime localDateTime = LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getDateFromGermanTime(String germanDate) {
        String[] dates = germanDate.split("\\.");
        if (dates.length != 3)
            throw new IllegalArgumentException("Date '" + germanDate + "' doesn't conform with format 'DD.MM.YYYY'.");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[0]));
        // Months are 0-indexed.
        calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(dates[2]));
        return calendar.getTime();
    }

    public static Supplier<IllegalArgumentException> illegalArgument(String code, String codeName) {
        return () -> new IllegalArgumentException("Code \"" + code + "\" is not a valid " + codeName);
    }

    @SafeVarargs
    public static <T> List<T> listOf(T... values) {
        return Arrays.asList(values);
    }

    public static boolean checkNonEmptyString(String s) {
        return s != null && !s.isEmpty();
    }
}
