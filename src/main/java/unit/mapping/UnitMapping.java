package unit.mapping;

import java.math.BigDecimal;

public class UnitMapping {
  private final String ucumCode;
  private final BigDecimal conversion;

  public UnitMapping(String ucumCode, BigDecimal conversion) {
    this.ucumCode = ucumCode;
    this.conversion = conversion;
  }

  public String getUcumCode() {
    return ucumCode;
  }

  public BigDecimal getConversion() {
    return conversion;
  }
}
