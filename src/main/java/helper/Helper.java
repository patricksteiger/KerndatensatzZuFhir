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
   * @throws NullPointerException if date is null
   */
  public static Date getDateFromISO(String date) {
    String isoDate = date;
    // Example: 2020-07-21
    final int simpleDateLength = 10;
    // Add timestamp if needed to avoid parsing exception
    if (isoDate.length() <= simpleDateLength) isoDate += "T00:00:00";
    // Use LocalDateTime to properly parse ISO 8601-Date
    LocalDateTime localDateTime = LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME);
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date getDateFromGermanTime(String germanDate) {
    String[] dates = germanDate.split("\\.");
    if (dates.length != 3)
      throw new IllegalArgumentException(
          "Date '" + germanDate + "' doesn't conform with format 'DD.MM.YYYY'.");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[0]));
    // Months are 0-indexed.
    calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]) - 1);
    calendar.set(Calendar.YEAR, Integer.parseInt(dates[2]));
    return calendar.getTime();
  }

  public static Supplier<IllegalArgumentException> illegalCode(String code, String codeName) {
    return () -> new IllegalArgumentException("Code \"" + code + "\" is not a valid " + codeName);
  }

  /**
   * Parses boolean from String. s must be 0, false, 1 or true. false and true are checked
   * case-insensitively.
   *
   * @param s String which should be parsed
   * @return boolean parsed from String s
   * @throws IllegalArgumentException if s is not 0, false, 1 or true.
   */
  public static boolean booleanFromString(String s) {
    if ("1".equals(s) || "true".equalsIgnoreCase(s)) return true;
    if ("0".equals(s) || "false".equalsIgnoreCase(s)) return false;
    throw new IllegalArgumentException("Couldn't read boolean from String: \"" + s + "\"");
  }

  @SafeVarargs
  public static <T> List<T> listOf(T... values) {
    return Arrays.asList(values);
  }

  public static boolean checkAnyNonEmptyStrings(String... s) {
    return Arrays.stream(s).anyMatch(Helper::checkNonEmptyString);
  }

  public static boolean checkNonEmptyString(String s) {
    return s != null && !s.isEmpty();
  }
}
