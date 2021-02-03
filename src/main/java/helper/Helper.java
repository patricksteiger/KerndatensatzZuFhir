package helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Supplier;

public class Helper {
  private Helper() {}

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

  public static boolean checkEmptyString(String s) {
    return !checkNonEmptyString(s);
  }

  public static boolean checkAllEmptyString(String... s) {
    return Arrays.stream(s).allMatch(Helper::checkEmptyString);
  }

  /**
   * Extracts the first word that starts with code and returns string without prefix code and
   * removes quotes.
   *
   * @param words
   * @param code
   * @return Substring of first word starting with code
   */
  public static String extractCode(List<String> words, String code) {
    for (String word : words) {
      if (word.startsWith(code)) {
        String extractedCode = word.substring(code.length());
        return Helper.trimQuotes(extractedCode);
      }
    }
    return "";
  }

  /**
   * Splits code into different values. Example: system="http://loinc.org" code="20570-8" gets split
   * into [system="http://loinc.org", code="20570-8"]. Quotes can't be within the values, they need
   * to wrap around as seen in the example.
   *
   * @param code Code containing values
   * @return Split values from code
   */
  public static List<String> splitCode(String code) {
    List<String> codes = new ArrayList<>();
    if (Helper.checkEmptyString(code)) return codes;
    final int CODE_LEN = code.length();
    int index = 0;
    // Example: code="1234"
    while (index < CODE_LEN) {
      // Skip whitespaces before parsed words
      while (index < CODE_LEN && Character.isWhitespace(code.charAt(index))) index++;
      // return codes if no new code was parsed
      if (index >= CODE_LEN) return codes;
      StringBuilder sb = new StringBuilder();
      // Parse every non-whitespace character, e.g. code=
      while (index < CODE_LEN && code.charAt(index) != '\"') {
        sb.append(code.charAt(index));
        index++;
      }
      // If index is valid, add quote
      if (index < CODE_LEN) {
        sb.append(code.charAt(index));
        index++;
      }
      // Parse everything until next quote, e.g. 1234
      while (index < CODE_LEN && code.charAt(index) != '\"') {
        sb.append(code.charAt(index));
        index++;
      }
      // If index is valid, add quote and parsed code to codes
      if (index < CODE_LEN) {
        sb.append(code.charAt(index));
        index++;
      }
      if (sb.length() > 0) codes.add(sb.toString());
    }
    return codes;
  }

  public static String trimQuotes(String s) {
    return trimCharacter(s, '\"');
  }

  public static String trimCharacter(String s, char c) {
    if (checkEmptyString(s)) return s;
    int lo = 0;
    while (lo < s.length() && s.charAt(lo) == c) lo++;
    // return empty string if all characters are trimmed
    if (lo >= s.length()) return "";
    int hi = s.length() - 1;
    while (hi >= 0 && s.charAt(hi) == c) hi--;
    // If string doesn't start or end with given character return itself
    if (lo == 0 && hi == s.length() - 1) return s;
    // Consider the fact that lo and hi are indices that are not equal to given character
    return s.substring(lo, hi + 1);
  }
}
