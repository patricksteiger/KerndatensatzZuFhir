package helper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ValueAndUnitParsed {
  private final String value;
  private final String unit;
  private final String ucum;

  private ValueAndUnitParsed(String value, String unit, String ucum) {
    this.value = value;
    this.unit = unit;
    this.ucum = ucum;
  }

  public static ValueAndUnitParsed fromString(String s) {
    List<String> splitCode = Helper.splitCode(s);
    String value = Helper.extractCode(splitCode, "value=");
    // TODO: Split into numerator and denominator?
    String unit = Helper.extractCode(splitCode, "unit=");
    return new ValueAndUnitParsed(value, unit, unit);
  }

  public Optional<BigDecimal> getDecimalValue() {
    try {
      return Optional.of(new BigDecimal(this.getValue()));
    } catch (Exception e) {
      return Optional.empty();
    }
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
}
