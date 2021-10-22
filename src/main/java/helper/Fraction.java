package helper;

import java.util.Objects;
import java.util.Optional;

public class Fraction {
  private static final String BREAK_LINE = "/";
  private static final char SEPARATOR = '/';

  private final String numerator;
  private final String denominator;

  private Fraction(String numerator, String denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  public static Optional<Fraction> fromString(String fraction) {
    if (Helper.checkEmptyString(fraction)) {
      return Optional.of(new Fraction("", ""));
    }
    int separatorIndex = fraction.indexOf(SEPARATOR);
    if (separatorIndex < 0) {
      return Optional.of(new Fraction(fraction.trim(), ""));
    }
    boolean denominatorHasSeparator = fraction.indexOf(SEPARATOR, separatorIndex + 1) >= 0;
    if (denominatorHasSeparator) {
      return Optional.empty();
    }
    String numerator = fraction.substring(0, separatorIndex).trim();
    String denominator = fraction.substring(separatorIndex + 1).trim();
    return Optional.of(new Fraction(numerator, denominator));
  }

  public static Optional<Fraction> fromString2(String fraction) {
    if (Helper.checkEmptyString(fraction)) {
      return Optional.empty();
    }
    String[] splitFraction = fraction.split(BREAK_LINE);
    boolean fractionHasWrongNumberOfElements = splitFraction.length < 1 || splitFraction.length > 2;
    boolean emptyDenominator =
        splitFraction.length == 2 && Helper.checkEmptyString(splitFraction[1]);
    if (fractionHasWrongNumberOfElements || emptyDenominator) {
      return Optional.empty();
    }
    String numerator = splitFraction[0];
    String denominator = splitFraction.length == 2 ? splitFraction[1] : "1";
    if (Helper.isZero(denominator)) {
      return Optional.empty();
    }
    return Optional.of(new Fraction(numerator, denominator));
  }

  public String getNumerator() {
    return numerator;
  }

  public String getDenominator() {
    return denominator;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Fraction fraction = (Fraction) o;
    return Objects.equals(getNumerator(), fraction.getNumerator())
        && Objects.equals(getDenominator(), fraction.getDenominator());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getNumerator(), getDenominator());
  }

  @Override
  public String toString() {
    return "Fraction{"
        + "numerator='"
        + numerator
        + '\''
        + ", denominator='"
        + denominator
        + '\''
        + '}';
  }
}
