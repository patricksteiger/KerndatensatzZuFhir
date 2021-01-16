package helper;

import java.math.BigDecimal;

public class ValueAndUnitParsed {
  private BigDecimal value;
  private String unit;

  public ValueAndUnitParsed(BigDecimal value, String unit) {
    this.value = value;
    this.unit = unit;
  }

  public static ValueAndUnitParsed fromString(String s) {
    String trimmed = s.trim();
    StringBuilder sb = new StringBuilder();
    int index = 0;
    for (; index < trimmed.length(); index++) {
      char c = trimmed.charAt(index);
      if (Character.isDigit(c) || c == '.') sb.append(c);
      else break;
    }
    BigDecimal value = new BigDecimal(sb.toString());
    String unit = trimmed.substring(index).trim();
    return new ValueAndUnitParsed(value, unit);
  }

  public BigDecimal getValue() {
    return value;
  }

  public String getUnit() {
    return unit;
  }
}
