package helper;

import java.util.Optional;

public class Fraction {
  private static final String BREAK_LINE = "/";

  private final String numerator;
  private final String denominator;

  private Fraction(String numerator, String denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  public static Optional<Fraction> fromString(String fraction) {
    String[] splitFraction = fraction.split(BREAK_LINE);
    boolean fractionHasWrongNumberOfElements = splitFraction.length < 1 || splitFraction.length > 2;
    if (fractionHasWrongNumberOfElements || Helper.checkAnyEmptyString(splitFraction)) {
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
}
