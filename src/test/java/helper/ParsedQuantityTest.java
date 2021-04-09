package helper;

import constants.Constants;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ParsedQuantityTest {

  @Test
  void testFromStringSimple() {
    String value = "75";
    String unit = "mg";
    String toTest = getValueStr(value) + getUnitStr(unit);
    Optional<Quantity> parsed = ParsedQuantity.fromString(toTest);
    assertQuantity(value, unit, parsed);
  }

  @Test
  void testFromStringEmpty() {
    assertFalse(ParsedQuantity.fromString(null).isPresent());
    assertFalse(ParsedQuantity.fromString("").isPresent());
    assertFalse(ParsedQuantity.fromString("   ").isPresent());
  }

  @Test
  void testFromStringSpaces() {
    String value = "75";
    String unit = "mg";
    String toTest = "  " + getValueStr(value) + "   " + getUnitStr(unit) + " ";
    Optional<Quantity> parsed = ParsedQuantity.fromString(toTest);
    assertQuantity(value, unit, parsed);
  }

  @Test
  void testFromStringFloat() {
    String value = "12.7";
    String unit = "g/ml";
    String toTest = getValueStr(value) + getUnitStr(unit);
    Optional<Quantity> parsed = ParsedQuantity.fromString(toTest);
    assertQuantity(value, unit, parsed);
  }

  @Test
  void testFromStringNoUnit() {
    String value = getValueStr("123");
    Optional<Quantity> parsed = ParsedQuantity.fromString(value);
    assertQuantity("123", "1", parsed);
  }

  @Test
  void testFromStringNoValue() {
    assertFalse(ParsedQuantity.fromString(getUnitStr("g")).isPresent());
  }

  private void assertQuantity(
      String expectedValue, String expectedUnit, Optional<Quantity> actualQuantity) {
    assertTrue(actualQuantity.isPresent());
    Quantity quantity = actualQuantity.get();
    assertEquals(expectedValue, quantity.getValueElement().getValueAsString());
    assertEquals(expectedUnit, quantity.getCode());
    assertEquals(Constants.QUANTITY_SYSTEM, quantity.getSystem());
  }

  private String getValueStr(String value) {
    return "value=\"" + value + "\"";
  }

  private String getUnitStr(String unit) {
    return "unit=\"" + unit + "\"";
  }
}
