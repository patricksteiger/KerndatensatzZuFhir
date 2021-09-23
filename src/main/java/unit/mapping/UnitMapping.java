package unit.mapping;

import java.math.BigDecimal;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UnitMapping that = (UnitMapping) o;
    return Objects.equals(getUcumCode(), that.getUcumCode())
        && Objects.equals(getConversion(), that.getConversion());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUcumCode(), getConversion());
  }
}
