package helper;

import constants.Constants;
import interfaces.Code;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Helper {
  private Helper() {}

  /**
   * Parses ISO-8601 formatted date to Date-object.
   *
   * @param date formatted according to ISO-8601
   * @return Optional containing ISO-Date if parsable.
   */
  public static Optional<Date> getDateFromISO(String date) {
    if (Helper.checkEmptyString(date)) {
      return Optional.empty();
    }
    String isoDate = date;
    // Example: 2020-07-21
    final int simpleDateLength = 10;
    // Add timestamp if needed to avoid parsing exception
    if (isoDate.length() <= simpleDateLength) isoDate += "T00:00:00";
    try {
      // Use LocalDateTime to properly parse ISO 8601-Date
      LocalDateTime localDateTime = LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME);
      Date iso = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
      return Optional.of(iso);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /**
   * Parses boolean from String. s must be 0, false, 1 or true. false and true are checked
   * case-insensitively.
   *
   * @param s String which should be parsed
   * @return boolean parsed from String s or empty.
   */
  public static Optional<Boolean> booleanFromString(String s) {
    if ("1".equals(s) || "true".equalsIgnoreCase(s)) {
      return Optional.of(true);
    }
    if ("0".equals(s) || "false".equalsIgnoreCase(s)) {
      return Optional.of(false);
    }
    return Optional.empty();
  }

  /**
   * Iterates over list and every element which satisfies predicate gets consumed. If list is null,
   * nothing happens.
   *
   * @param list List of elements to be consumed
   * @param predicate Predicate which chooses elements to consume
   * @param consumer Consumer to consume elements
   * @param <T> Any type
   */
  public static <T> void forFilterEach(
      List<T> list, Predicate<T> predicate, Consumer<? super T> consumer) {
    if (list == null) {
      return;
    }
    for (T elem : list) {
      if (predicate.test(elem)) {
        consumer.accept(elem);
      }
    }
  }

  public static Optional<BigDecimal> parseValue(String value) {
    if (Helper.checkEmptyString(value)) {
      return Optional.empty();
    }
    String[] split = value.split("/");
    if (split.length == 1) {
      return maybeBigDecimal(value);
    } else if (split.length == 2) {
      Optional<BigDecimal> numerator = maybeBigDecimal(split[0]);
      Optional<BigDecimal> denominator = maybeBigDecimal(split[1]);
      if (!denominator.isPresent()) {
        return Optional.empty();
      }
      try {
        return numerator.map(
            n ->
                n.divide(
                    denominator.get(),
                    Constants.BIG_DECIMAL_SCALE,
                    Constants.BIG_DECIMAL_ROUNDING_MODE));
      } catch (Exception e) {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }

  public static Optional<BigDecimal> maybeBigDecimal(String value) {
    try {
      return Optional.of(new BigDecimal(value.trim()));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @SafeVarargs
  public static <T> List<T> listOf(T... values) {
    return Arrays.asList(values);
  }

  public static <T> boolean checkAllNull(List<T> list) {
    for (T elem : list) {
      if (elem != null) {
        return false;
      }
    }
    return true;
  }

  public static boolean checkAllNull(Object... objects) {
    for (Object object : objects) {
      if (object != null) {
        return false;
      }
    }
    return true;
  }

  public static boolean checkNonEmptyString(String s) {
    return !checkEmptyString(s);
  }

  public static boolean checkEmptyString(String s) {
    return s == null || s.isEmpty();
  }

  public static boolean checkAnyEmptyString(String... s) {
    for (String str : s) {
      if (Helper.checkEmptyString(str)) {
        return true;
      }
    }
    return false;
  }

  public static boolean checkAllEmptyString(String... s) {
    for (String str : s) {
      if (Helper.checkNonEmptyString(str)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Iterates over codes and returns first code whose code is equal to string. If no code has given
   * string as code, empty is returned.
   *
   * @param codes Codes containing codes, that are searched for code string
   * @param string String representing code
   * @param <T> Type of Code
   * @return Code if corresponding code was found. Otherwise is empty.
   */
  public static <T extends Code> Optional<T> codeFromString(T[] codes, String string) {
    for (T code : codes) {
      if (code.getCode().equals(string)) {
        return Optional.of(code);
      }
    }
    return Optional.empty();
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
    while (index < CODE_LEN) {
      // Skip whitespaces
      int wordIndex = indexAfterWhitespace(code, index);
      int firstQuoteIndex = indexNextQuote(code, wordIndex);
      // Get index of second index, so code spans from wordIndex to secondQuoteIndex
      int secondQuoteIndex = indexNextQuote(code, firstQuoteIndex + 1);
      // Update index to next character
      index = secondQuoteIndex + 1;
      if (wordIndex < CODE_LEN) {
        // If code has simple format, index > code.length().
        int endIndex = Math.min(index, CODE_LEN);
        String word = code.substring(wordIndex, endIndex);
        codes.add(word);
      }
    }
    return codes;
  }

  public static int indexNextQuote(String s, int from) {
    return indexAfterPredicate(s, from, c -> c != '\"');
  }

  public static int indexAfterWhitespace(String s, int from) {
    return indexAfterPredicate(s, from, Character::isWhitespace);
  }

  public static int indexAfterPredicate(String s, int from, Predicate<Character> predicate) {
    for (int i = from; i < s.length(); i++) {
      if (!predicate.test(s.charAt(i))) {
        return i;
      }
    }
    return s.length();
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
