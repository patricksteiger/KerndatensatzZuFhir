package unit.converter;

import constants.Constants;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UnitConverterTest {
  @Test
  void testEmpty() {
    assertFalse(UnitConverter.fromLocalUnit(null, null).isPresent());
    assertFalse(UnitConverter.fromLocalUnit("", "").isPresent());
    assertFalse(UnitConverter.fromLocalUnit(null, "").isPresent());
    assertFalse(UnitConverter.fromLocalUnit("", null).isPresent());
    String unit = "ml";
    assertFalse(UnitConverter.fromLocalUnit(unit, "").isPresent());
    assertFalse(UnitConverter.fromLocalUnit(unit, null).isPresent());
  }

  @Test
  void testNonReducible() {
    String value = "120/80";
    String unit = "mm Hg";
    Optional<Quantity> result = UnitConverter.fromLocalUnit(unit, value);
    assertQuantity(value, "mm[Hg]", result);
  }

  @Test
  void testInvalidUnit() {
    String value = "1";
    String unit = "invalid";
    assertFalse(UnitConverter.fromLocalUnit(unit, value).isPresent());
  }

  @Test
  void testInvalidValue() {
    String value = "invalid";
    String unit = "mL";
    assertFalse(UnitConverter.fromLocalUnit(unit, value).isPresent());
  }

  @Test
  void testSimpleValue() {
    String value = "12";
    String unit = "mL";
    Optional<Quantity> result = UnitConverter.fromLocalUnit(unit, value);
    assertQuantity(value, unit, result);
  }

  @Test
  void testFractionValue() {
    String value = "1/2";
    String unit = "mL";
    Optional<Quantity> result = UnitConverter.fromLocalUnit(unit, value);
    assertQuantity("0.5", unit, result);
  }

  private void assertQuantity(
      String expectedValue, String expectedUnit, Optional<Quantity> actualQuantity) {
    assertTrue(actualQuantity.isPresent());
    Quantity quantity = actualQuantity.get();
    assertValueEquals(expectedValue, quantity.getValueElement().getValueAsString());
    assertEquals(expectedUnit, quantity.getCode());
    assertEquals(Constants.QUANTITY_SYSTEM, quantity.getSystem());
  }

  private void assertValueEquals(String expected, String actual) {
    if (expected.length() != actual.length() && !expected.contains(".")) {
      assertEquals(expected, actual);
    }
    int len = Math.min(expected.length(), actual.length());
    // Check if shared indices are equal
    for (int i = 0; i < len; i++) {
      if (expected.charAt(i) != actual.charAt(i)) {
        assertEquals(expected, actual);
      }
    }
    // Check if excess indices are equal
    for (int i = expected.length(); i < actual.length(); i++) {
      if (actual.charAt(i) != '0') {
        assertEquals(expected, actual);
      }
    }
    for (int i = actual.length(); i < expected.length(); i++) {
      if (expected.charAt(i) != '0') {
        assertEquals(expected, actual);
      }
    }
  }
}
