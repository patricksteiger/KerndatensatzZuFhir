package helper;

import java.util.Optional;

public class ValueAndUnitFraction {
  private final Fraction value;
  private final Fraction unit;

  private ValueAndUnitFraction(Fraction value, Fraction unit) {
    this.value = value;
    this.unit = unit;
  }

  // TODO: Can a value or unit fraction be empty?
  public static Optional<ValueAndUnitFraction> fromString(
      String valueFraction, String unitFraction) {
    Optional<Fraction> fractionValue =
        Fraction.fromString(valueFraction).filter(value -> !Helper.isZero(value.getDenominator()));
    return Helper.optionalAnd(
        fractionValue, () -> Fraction.fromString(unitFraction), ValueAndUnitFraction::new);
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
