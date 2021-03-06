package converter;

import converter.mapping.UnitMapping;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UnitConverterTest {
  @Test
  void testEmptyInput() {
    assertFalse(UnitConverter.getUcum(null).isPresent());
    assertFalse(UnitConverter.getUcum("").isPresent());
  }

  @Test
  void testInvalidInput() {
    String input = "abc";
    assertFalse(UnitConverter.getUcum(input).isPresent());
  }

  @Test
  void testUcumUnit() {
    String unit = "mg/l";
    Optional<UnitMapping> result = UnitConverter.getUcum(unit);
    assertTrue(result.isPresent());
    UnitMapping resultMapping = result.get();
    assertEquals(unit, resultMapping.getUcumCode());
    assertNotNull(resultMapping.getUcumUnit());
    assertEquals(BigDecimal.ONE, resultMapping.getConversion());
    unit = "ml";
    result = UnitConverter.getUcum(unit);
    assertTrue(result.isPresent());
    resultMapping = result.get();
    assertEquals(unit, resultMapping.getUcumCode());
    assertNotNull(resultMapping.getUcumUnit());
    assertEquals(BigDecimal.ONE, resultMapping.getConversion());
  }

  @Test
  void testLocalUnit() {
    String unit = "µg/l";
    Optional<UnitMapping> result = UnitConverter.getUcum(unit);
    assertTrue(result.isPresent());
    UnitMapping resultMapping = result.get();
    assertEquals("ng/mL", resultMapping.getUcumCode());
    assertNotNull(resultMapping.getUcumUnit());
    assertEquals(new BigDecimal("1.0"), resultMapping.getConversion());
    unit = "Zell./µl";
    result = UnitConverter.getUcum(unit);
    assertTrue(result.isPresent());
    resultMapping = result.get();
    assertEquals("/uL", resultMapping.getUcumCode());
    assertNotNull(resultMapping.getUcumUnit());
    assertEquals(new BigDecimal("1.0"), resultMapping.getConversion());
  }

  @Test
  void testNumbers() {
    String input = "12345";
    Optional<UnitMapping> result = UnitConverter.getUcum(input);
    assertTrue(result.isPresent());
    UnitMapping m = result.get();
    assertEquals(input, m.getUcumCode());
    assertEquals(BigDecimal.ONE, m.getConversion());
    assertEquals(input, m.getUcumUnit());
  }
}
