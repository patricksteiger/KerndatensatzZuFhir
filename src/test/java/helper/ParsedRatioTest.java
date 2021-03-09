package helper;

import constants.Constants;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Ratio;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ParsedRatioTest {
  @Test
  void testEmpty() {
    assertFalse(ParsedRatio.fromString(null).isPresent());
    assertFalse(ParsedRatio.fromString("").isPresent());
  }

  @Test
  void testInvalidUnit() {
    String code1 = getValueStr("1/2");
    assertFalse(ParsedRatio.fromString(code1).isPresent());
    String code2 = getValueStr("1/2") + " " + getUnitStr("g");
    assertFalse(ParsedRatio.fromString(code2).isPresent());
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
    String code = getValueStr(value) + " " + getUnitStr(unit);
    Optional<Ratio> result = ParsedRatio.fromString(code);
    assertRatio("2", "g", "1", "l", result);
  }

  private void assertRatio(
      String expectedNumeratorValue,
      String expectedNumeratorCode,
      String expectedDenominatorValue,
      String expectedDenominatorCode,
      Optional<Ratio> optionalRatio) {
    assertTrue(optionalRatio.isPresent());
    Ratio ratio = optionalRatio.get();
    assertQuantity(expectedNumeratorValue, expectedNumeratorCode, ratio.getNumerator());
    assertQuantity(expectedDenominatorValue, expectedDenominatorCode, ratio.getDenominator());
  }

  private void assertQuantity(String expectedValue, String expectedCode, Quantity quantity) {
    assertEquals(expectedValue, quantity.getValueElement().getValueAsString());
    assertEquals(expectedCode, quantity.getCode());
    assertEquals(Constants.QUANTITY_SYSTEM, quantity.getSystem());
  }

  private String getValueStr(String value) {
    return "value=\"" + value + "\"";
  }

  private String getUnitStr(String unit) {
    return "unit=\"" + unit + "\"";
  }
}
