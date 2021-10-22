package helper;

import constants.Constants;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Ratio;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static util.Util.getValueUnitStr;

class ParsedRatioTest {
  @Test
  void testEmpty() {
    Optional<Ratio> result = ParsedRatio.fromString(null);
    assertRatio(null, null, null, "1", null, Constants.QUANTITY_SYSTEM, result);
    result = ParsedRatio.fromString(null);
    assertRatio(null, null, null, "1", null, Constants.QUANTITY_SYSTEM, result);
  }

  @Test
  void testInvalidUnit() {
    String code1 = getValueUnitStr("1/2", "//");
    assertFalse(ParsedRatio.fromString(code1).isPresent());
    String code2 = getValueUnitStr("1/2", "/g/");
    assertFalse(ParsedRatio.fromString(code2).isPresent());
  }

  @Test
  void testDenominatorValueZero() {
    String unit = "mmol/L";
    assertFalse(ParsedRatio.fromString(getValueUnitStr("2/0", unit)).isPresent());
    assertFalse(ParsedRatio.fromString(getValueUnitStr("2/00", unit)).isPresent());
    assertFalse(ParsedRatio.fromString(getValueUnitStr("2/0.0", unit)).isPresent());
  }

  @Test
  void testInvalidValue() {
    String unit = "L/g";
    assertFalse(ParsedRatio.fromString(getValueUnitStr("1/2/3", unit)).isPresent());
  }

  @Test
  void testEmptyValueNumerator() {
    String unit = "L/g";
    Optional<Ratio> result = ParsedRatio.fromString(getValueUnitStr("/3", unit));
    assertRatio(null, null, null, "3", "g", Constants.QUANTITY_SYSTEM, result);
  }

  @Test
  void testBothNumAndDenom() {
    String value = "1/2";
    String unit = "g/l";
    String code = getValueStr(value) + " " + getUnitStr(unit);
    Optional<Ratio> result = ParsedRatio.fromString(code);
    assertRatio("1", "g", "2", "l", result);
  }

  @Test
  void testOnlyNum() {
    String value = "2";
    String unit = "g/l";
    String code = getValueUnitStr(value, unit);
    Optional<Ratio> result = ParsedRatio.fromString(code);
    assertRatio("2", "g", "1", "l", result);
  }

  private void assertRatio(
      String expectedNumeratorValue,
      String expectedNumeratorCode,
      String expectedNumeratorSystem,
      String expectedDenominatorValue,
      String expectedDenominatorCode,
      String expectedDenominatorSystem,
      Optional<Ratio> optionalRatio) {
    assertTrue(optionalRatio.isPresent(), "Should be non-emptyy");
    Ratio ratio = optionalRatio.get();
    assertQuantity(
        expectedNumeratorValue,
        expectedNumeratorCode,
        expectedNumeratorSystem,
        ratio.getNumerator());
    assertQuantity(
        expectedDenominatorValue,
        expectedDenominatorCode,
        expectedDenominatorSystem,
        ratio.getDenominator());
  }

  private void assertRatio(
      String expectedNumeratorValue,
      String expectedNumeratorCode,
      String expectedDenominatorValue,
      String expectedDenominatorCode,
      Optional<Ratio> optionalRatio) {
    assertTrue(optionalRatio.isPresent(), "Should be non-emptyy");
    Ratio ratio = optionalRatio.get();
    assertQuantity(expectedNumeratorValue, expectedNumeratorCode, ratio.getNumerator());
    assertQuantity(expectedDenominatorValue, expectedDenominatorCode, ratio.getDenominator());
  }

  private void assertQuantity(
      String expectedValue, String expectedCode, String expectedSystem, Quantity quantity) {
    assertEquals(expectedValue, quantity.getValueElement().getValueAsString(), "Value");
    assertEquals(expectedCode, quantity.getCode(), "Code");
    assertEquals(expectedSystem, quantity.getSystem(), "System");
  }

  private void assertQuantity(String expectedValue, String expectedCode, Quantity quantity) {
    assertQuantity(expectedValue, expectedCode, Constants.QUANTITY_SYSTEM, quantity);
  }

  private String getValueStr(String value) {
    return "value=\"" + value + "\"";
  }

  private String getUnitStr(String unit) {
    return "unit=\"" + unit + "\"";
  }
}
