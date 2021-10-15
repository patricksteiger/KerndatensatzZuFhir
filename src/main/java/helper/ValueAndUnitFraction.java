package helper;

import java.util.Optional;

public class ValueAndUnitFraction {
  private final Fraction value;
  private final Fraction unit;

  private ValueAndUnitFraction(Fraction value, Fraction unit) {
    this.value = value;
    this.unit = unit;
  }

  public static Optional<ValueAndUnitFraction> fromString(
      String valueFraction, String unitFraction) {
    Optional<Fraction> fractionValue = Fraction.fromString(valueFraction);
    Optional<Fraction> fractionUnit = Fraction.fromString(unitFraction);
    return Helper.optionalAnd(fractionValue, () -> fractionUnit, ValueAndUnitFraction::new);
  }

  public String getValueNumerator() {
    return value.getNumerator();
  }

  public String getValueDenominator() {
    return value.getDenominator();
  }

  public String getUnitNumerator() {
    return unit.getNumerator();
  }

  public String getUnitDenominator() {
    return unit.getDenominator();
  }
}
