package unit.convert;

import org.junit.jupiter.api.Test;
import unit.convert.mapping.UnitMapping;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UnitConverterTest {
  @Test
  void testEmptyInput() {
    assertFalse(UnitConverter.getUcum(null).isPresent());
    Optional<UnitMapping> result = UnitConverter.getUcum("");
    assertUnitMapping("", BigDecimal.ONE, result);
  }

  @Test
  void testInvalidInput() {
    String input = "abc";
    assertFalse(UnitConverter.getUcum(input).isPresent());
    input = "1abc";
    assertFalse(UnitConverter.getUcum(input).isPresent());
  }

  @Test
  void testUcumUnit() {
    String unit = "mg/l";
    Optional<UnitMapping> result = UnitConverter.getUcum(unit);
    assertUnitMapping(unit, BigDecimal.ONE, result);
    unit = "ml";
    result = UnitConverter.getUcum(unit);
    assertUnitMapping(unit, BigDecimal.ONE, result);
  }

  @Test
  void testLocalUnit() {
    String unit = "µg/l";
    Optional<UnitMapping> result = UnitConverter.getUcum(unit);
    assertUnitMapping("ng/mL", new BigDecimal("1.0"), result);
    unit = "Zell./µl";
    result = UnitConverter.getUcum(unit);
    assertUnitMapping("/uL", new BigDecimal("1.0"), result);
  }

  @Test
  void testNumbers() {
    String input = "12345";
    Optional<UnitMapping> result = UnitConverter.getUcum(input);
    assertUnitMapping(input, BigDecimal.ONE, result);
  }

  private void assertUnitMapping(
      String expectedUcumCode, BigDecimal expectedConversion, Optional<UnitMapping> mapping) {
    assertTrue(mapping.isPresent());
    UnitMapping result = mapping.get();
    assertEquals(expectedUcumCode, result.getUcumCode());
    assertEquals(expectedConversion, result.getConversion());
  }
}
