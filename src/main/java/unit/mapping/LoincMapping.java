package unit.mapping;

import java.math.BigDecimal;

public class LoincMapping {
  private final String code;
  private final String display;
  private final BigDecimal conversion;
  private final String ucumCode;

  public LoincMapping(String code, String display, BigDecimal conversion, String ucumCode) {
    this.code = code;
    this.display = display;
    this.conversion = conversion;
    this.ucumCode = ucumCode;
  }

  public String getCode() {
    return code;
  }

  public String getDisplay() {
    return display;
  }

  public BigDecimal getConversion() {
    return conversion;
  }

  public String getUcumCode() {
    return ucumCode;
  }
}
