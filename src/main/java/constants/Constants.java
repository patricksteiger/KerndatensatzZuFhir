package constants;

public class Constants {
  public static final String EMPTY_IDENTIFIER_SYSTEM = "";
  public static final String VERSION_2020 = "2020";
  public static final String QUANTITY_SYSTEM = "http://unitsofmeasure.org";

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
}
