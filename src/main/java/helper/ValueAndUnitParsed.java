package helper;

import constants.Constants;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ValueAndUnitParsed {
  private final String value;
  private final String unit;
  private final Optional<BigDecimal> decimalValue;
  private final String ucum;

  private ValueAndUnitParsed(
      String value, String unit, Optional<BigDecimal> decimalValue, String ucum) {
    this.value = value;
    this.unit = unit;
    this.decimalValue = decimalValue;
    this.ucum = ucum;
  }

  public static ValueAndUnitParsed fromString(String s) {
    List<String> splitCode = Helper.splitCode(s);
    String value = Helper.extractCode(splitCode, "value=");
    String unit = Helper.extractCode(splitCode, "unit=");
    Optional<BigDecimal> decimalValue = Helper.parseValue(value);
    // TODO: UCUM Conversion
    return new ValueAndUnitParsed(value, unit, decimalValue, unit);
  }

  public Optional<BigDecimal> getDecimalValue() {
    return decimalValue;
  }

  public String getValue() {
    return value;
  }

  public String getUnit() {
    return unit;
  }

  public String getUcum() {
    return ucum;
  }

  public String getSystem() {
    return Constants.QUANTITY_SYSTEM;
  }
}
