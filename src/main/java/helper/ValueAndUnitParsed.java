package helper;

import java.util.List;

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
