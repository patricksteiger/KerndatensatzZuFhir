package unit.converter;

import constants.Constants;
import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UnitConverterTest {
  @Test
  void testEmpty() {
    assertFalse(UnitConverter.fromLocalCode(null, null).isPresent());
    assertFalse(UnitConverter.fromLocalCode("", "").isPresent());
    String unit = "ml";
    assertFalse(UnitConverter.fromLocalCode(unit, "").isPresent());
    assertFalse(UnitConverter.fromLocalCode(unit, null).isPresent());
  }

  @Test
  void testNonReducable() {
    String value = "120/80";
    String unit = "mm Hg";
    Optional<Quantity> result = UnitConverter.fromLocalCode(unit, value);
    assertQuantity(value, "mm[Hg]", result);
  }

  private void assertQuantity(
      String expectedValue, String expectedUnit, Optional<Quantity> actualQuantity) {
    assertTrue(actualQuantity.isPresent());
    Quantity quantity = actualQuantity.get();
    assertEquals(expectedValue, quantity.getValueElement().getValueAsString());
    assertEquals(expectedUnit, quantity.getCode());
    assertEquals(Constants.QUANTITY_SYSTEM, quantity.getSystem());
  }
}
