package helper;

public class ValueAndUnitParsed {
  private final String value;
  private final String unit;

  public ValueAndUnitParsed(String value, String unit) {
    this.value = value;
    this.unit = unit;
  }

  // TODO: Maybe define format similar to ParsedCode?
  public static ValueAndUnitParsed fromString(String s) {
    String trimmed = s.trim();
    int index = 0;
    while (index < trimmed.length()) {
      char c = trimmed.charAt(index);
      if (Character.isDigit(c) || c == '.') {
        index++;
      } else {
        break;
      }
    }
    String value = trimmed.substring(0, index);
    String unit = trimmed.substring(index).trim();
    return new ValueAndUnitParsed(value, unit);
  }

  public String getValue() {
    return value;
  }

  public String getUnit() {
    return unit;
  }
}
