package helper;

import constants.Constants;
import interfaces.CharPredicate;
import interfaces.Code;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.*;

public class Helper {
  private static final String FRACTION = "/";

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
   * Checks if a string only contains '0' and '.'.
   *
   * @param s
   * @return true if s only contains character equal to '0' and '.'
   */
  public static boolean isZero(String s) {
    if (Helper.checkEmptyString(s)) {
      return true;
    }
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c != '0' && c != '.') {
        return false;
      }
    }
    return true;
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
      return Optional.of(Boolean.TRUE);
    }
    if ("0".equals(s) || "false".equalsIgnoreCase(s)) {
      return Optional.of(Boolean.FALSE);
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
  public static <T> void filterForEach(List<T> list, Predicate<T> predicate, Consumer<T> consumer) {
    if (list == null) {
      return;
    }
    for (T elem : list) {
      if (predicate.test(elem)) {
        consumer.accept(elem);
      }
    }
  }

  public static OptionalInt parseInt(String number) {
    if (Helper.checkEmptyString(number)) {
      return OptionalInt.empty();
    }
    try {
      int n = Integer.parseInt(number);
      return OptionalInt.of(n);
    } catch (Exception e) {
      return OptionalInt.empty();
    }
  }

  public static Optional<BigDecimal> parseBigDecimalFromQuantity(String value) {
    if (Helper.checkEmptyString(value)) {
      return Optional.empty();
    }
    String[] split = value.split(FRACTION);
    return switch (split.length) {
      case 1 -> maybeBigDecimal(value);
      case 2 -> fraction(split[0], split[1]);
      default -> Optional.empty();
    };
  }

  public static Optional<BigDecimal> fraction(String numerator, String denominator) {
    Optional<BigDecimal> num = maybeBigDecimal(numerator);
    Optional<BigDecimal> den = maybeBigDecimal(denominator);
    return (num.isPresent() && den.isPresent()) ? safeDiv(num.get(), den.get()) : Optional.empty();
  }

  public static Optional<BigDecimal> safeDiv(BigDecimal numerator, BigDecimal denominator) {
    return optionalOfException(() -> numerator.divide(
        denominator, Constants.BIG_DECIMAL_SCALE, Constants.BIG_DECIMAL_ROUNDING_MODE));
  }

  public static Optional<BigDecimal> maybeBigDecimal(String value) {
    return optionalOfException(() -> new BigDecimal(value.trim()));
  }

  public static <T> Optional<T> optionalOfException(Supplier<T> supplier) {
    try {
      return Optional.ofNullable(supplier.get());
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public static <T, U, R> Optional<R> optionalAnd(Optional<T> x, Supplier<Optional<U>> ySupplier, BiFunction<T, U, R> f) {
    if (x.isEmpty()) {
      return Optional.empty();
    }
    Optional<U> y = ySupplier.get();
    if (y.isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(f.apply(x.get(), y.get()));
  }

  @SafeVarargs
  public static <T> List<T> listOf(T... values) {
    return Arrays.asList(values);
  }

  /**
   * Splits the given name into multiple names. Splits by spaces. Name without spaces result in it
   * being the only element in the list.
   *
   * @param name Name to split into multiple names
   * @return List containing all single names
   */
  public static List<String> splitNames(String name) {
    if (Helper.checkEmptyString(name)) {
      return Helper.listOf();
    }
    return Helper.listOf(name.trim().split("[ ]+"));
  }

  public static <T, R> List<R> listMap(List<T> list, Function<T, R> mapper) {
    List<R> result = new ArrayList<>(list.size());
    list.forEach(e -> result.add(mapper.apply(e)));
    return result;
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
   * Splits code into different values. Example: 'system="http://loinc.org" code="20570-8"' gets
   * split into [system="http://loinc.org", code="20570-8"]. Quotes can't be within the values, they
   * need to wrap around as seen in the example. If the code is in simple format, e.g. "20570-8",
   * then it will be the only element in the list.
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
      // Get index of second quote, so code spans from wordIndex to secondQuoteIndex
      int secondQuoteIndex = indexNextQuote(code, firstQuoteIndex + 1);
      // Update index to next character after current code
      index = secondQuoteIndex + 1;
      if (wordIndex < CODE_LEN) {
        // If code has simple format, a quote might not be present, which means
        // secondQuoteIndex == CODE_LEN and therefore index > CODE_LEN.
        int endIndex = Math.min(index, CODE_LEN);
        String word = code.substring(wordIndex, endIndex);
        codes.add(word);
      }
    }
    return codes;
  }

  public static int indexNextQuote(String s, int startIndex) {
    return indexNextPredicate(s, startIndex, c -> c == '\"');
  }

  public static int indexAfterWhitespace(String s, int startIndex) {
    return indexNextPredicate(s, startIndex, c -> !Character.isWhitespace(c));
  }

  public static int indexNextPredicate(String s, int startIndex, CharPredicate predicate) {
    for (int i = startIndex; i < s.length(); i++) {
      if (predicate.test(s.charAt(i))) return i;
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
