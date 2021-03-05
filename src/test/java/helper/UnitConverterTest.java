package helper;

import org.fhir.ucum.UcumException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UnitConverterTest {
  @Test
  void testEmptyInput() throws UcumException, IOException {
    assertFalse(UnitConverter.getUcum(null).isPresent());
    assertFalse(UnitConverter.getUcum("").isPresent());
  }

  @Test
  void testUcumUnit() throws UcumException, IOException {
    String unit = "mg/l";
    Optional<UnitConverter.UnitMapping> result = UnitConverter.getUcum(unit);
    assertTrue(result.isPresent());
    UnitConverter.UnitMapping resultMapping = result.get();
    assertEquals(unit, resultMapping.getUcumUnit());
    assertEquals(BigDecimal.ONE, resultMapping.getConversion());
  }

  @Test
  void testLocalUnit() throws UcumException, IOException {
    String unit = "µg/l";
    Optional<UnitConverter.UnitMapping> result = UnitConverter.getUcum(unit);
    assertTrue(result.isPresent());
    UnitConverter.UnitMapping resultMapping = result.get();
    assertEquals("ng/mL", resultMapping.getUcumUnit());
    assertEquals(new BigDecimal("1.0"), resultMapping.getConversion());
  }
}
