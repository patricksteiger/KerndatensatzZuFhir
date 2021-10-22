package helper;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FractionTest {

  private static void assertInvalidFraction(String fraction) {
    Optional<Fraction> result = Fraction.fromString(fraction);
    assertEquals(Optional.empty(), result);
  }

  private static void assertValidFraction(
      String fraction, String expectedNumerator, String expectedDenominator) {
    Optional<Fraction> result = Fraction.fromString(fraction);
    assertTrue(result.isPresent());
    Fraction actual = result.get();
    assertEquals(expectedNumerator, actual.getNumerator(), "Numerator");
    assertEquals(expectedDenominator, actual.getDenominator(), "Denominator");
  }

  @Test
  void wrongNumberOfNumbers() {
    assertInvalidFraction("1/2/3");
    assertInvalidFraction("//");
    assertInvalidFraction("1//2");
  }

  @Test
  void emptyString() {
    assertValidFraction("", "", "");
  }

  @Test
  void whitespacesOnly() {
    assertValidFraction("    ", "", "");
  }

  @Test
  void nullString() {
    assertValidFraction(null, "", "");
  }

  @Test
  void fractionOnly() {
    assertValidFraction("/", "", "");
  }

  @Test
  void emptyDenominator() {
    assertValidFraction("g/", "g", "");
  }

  @Test
  void noFractionLine() {
    assertValidFraction(" 12 ", "12", "");
  }

  @Test
  void validUcumUnit() {
    assertValidFraction("g/uL", "g", "uL");
  }

  @Test
  void validUcumUnitWithSpaces() {
    assertValidFraction(" g  /  uL   ", "g", "uL");
  }

  @Test
  void validUcumUnitWithoutNum() {
    assertValidFraction("/uL", "", "uL");
  }
}
