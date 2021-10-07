package unit.mapping;

import java.util.Objects;

public class LocalCodeWithUnit {
  private final String code;
  private final String unit;

  public LocalCodeWithUnit(String code, String unit) {
    this.code = code;
    this.unit = unit;
  }

  public String getCode() {
    return code;
  }

  public String getUnit() {
    return unit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LocalCodeWithUnit that = (LocalCodeWithUnit) o;
    return Objects.equals(getCode(), that.getCode()) && Objects.equals(getUnit(), that.getUnit());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCode(), getUnit());
  }
}
