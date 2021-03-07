package unit.mapping;

import org.junit.jupiter.api.Test;
import unit.mapping.mapping.UnitMapping;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UnitMapperTest {
  @Test
  void testEmptyInput() {
    assertFalse(UnitMapper.getUcum(null).isPresent());
    Optional<UnitMapping> result = UnitMapper.getUcum("");
    assertUnitMapping("", BigDecimal.ONE, result);
  }

  @Test
  void testInvalidInput() {
    String input = "abc";
    assertFalse(UnitMapper.getUcum(input).isPresent());
    input = "1abc";
    assertFalse(UnitMapper.getUcum(input).isPresent());
  }

  @Test
  void testUcumUnit() {
    String unit = "mg/l";
    Optional<UnitMapping> result = UnitMapper.getUcum(unit);
    assertUnitMapping(unit, BigDecimal.ONE, result);
    unit = "ml";
    result = UnitMapper.getUcum(unit);
    assertUnitMapping(unit, BigDecimal.ONE, result);
  }

  @Test
  void testLocalUnit() {
    String unit = "µg/l";
    Optional<UnitMapping> result = UnitMapper.getUcum(unit);
    assertUnitMapping("ng/mL", new BigDecimal("1.0"), result);
    unit = "Zell./µl";
    result = UnitMapper.getUcum(unit);
    assertUnitMapping("/uL", new BigDecimal("1.0"), result);
  }

  @Test
  void testNumbers() {
    String input = "12345";
    Optional<UnitMapping> result = UnitMapper.getUcum(input);
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
