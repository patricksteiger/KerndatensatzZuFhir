package helper;

import java.util.List;

public class ParsedValueAndUnit {

  private final String value;
  private final String unit;

  private ParsedValueAndUnit(String value, String unit) {
    this.value = value;
    this.unit = unit;
  }

  public static ParsedValueAndUnit fromString(String s) {
    List<String> splitCode = Helper.splitCode(s);
    String value = Helper.extractCodeWithPrefix(splitCode, "value=");
    String unit = Helper.extractCodeWithPrefix(splitCode, "unit=");
    return new ParsedValueAndUnit(value, unit);
  }

  public String getValue() {
    return value;
  }

  public String getUnit() {
    return unit;
  }
}
