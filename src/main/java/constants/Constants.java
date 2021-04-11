package constants;

import java.math.BigDecimal;

public class Constants {
  public static final String VERSION_2020 = "2020";
  public static final String QUANTITY_SYSTEM = "http://unitsofmeasure.org";
  public static final int BIG_DECIMAL_SCALE = 20;
  public static final int BIG_DECIMAL_ROUNDING_MODE = BigDecimal.ROUND_HALF_DOWN;

  private Constants() {}

  /**
   * Since Types in org.hl7.fhir.r4 use null as an empty value, null is used to represent values
   * that are skipped/optional.
   *
   * @param <T> Any value which empty value is null
   * @return empty value: null
   */
  public static <T> T getEmptyValue() {
    return null;
  }

  /**
   * Checks whether given value represents empty value.
   *
   * @param value Any value
   * @param <T> Any type
   * @return true if value is empty, false otherwise
   */
  public static <T> boolean isEmptyValue(T value) {
    return value == null;
  }
}
