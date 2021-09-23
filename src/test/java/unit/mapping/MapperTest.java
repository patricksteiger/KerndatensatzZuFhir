package unit.mapping;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {

  public static void assertUnitMappingFromUnit(
      String unit, String expectedUcumUnit, BigDecimal expectedConversion) {
    Optional<UnitMapping> result = Mapper.getUcumMappingFromLocalUnit(unit);
    assertTrue(result.isPresent());
    UnitMapping expected = new UnitMapping(expectedUcumUnit, expectedConversion);
    assertEquals(expected, result.get());
  }

  public static void assertOneToOneUnitMappingFromUnit(String unit, String expectedUcumUnit) {
    assertUnitMappingFromUnit(unit, expectedUcumUnit, BigDecimal.ONE);
  }

  public static void assertUnitCouldNotBeMapped(String unit) {
    assertFalse(Mapper.getUcumMappingFromLocalUnit(unit).isPresent());
  }

  @Test
  void nullReturnsEmpty() {
    assertUnitCouldNotBeMapped(null);
  }

  @Test
  void emptyInputReturnsUnity() {
    String unit = "", expectedUcumUnit = "";
    assertOneToOneUnitMappingFromUnit(unit, expectedUcumUnit);
  }

  @Test
  void invalidUnitReturnsEmpty() {
    assertUnitCouldNotBeMapped("xyz");
  }

  @Test
  void nonUcumButLocalUnitReturnsMapping() {
    String unit = "µmol/l", expectedUcumUnit = "umol/L";
    assertOneToOneUnitMappingFromUnit(unit, expectedUcumUnit);
  }

  @Test
  void nonLocalButUcumUnitReturnsMapping() {
    String unit = "10.uN.s/(cm5.m2)", expectedUcumUnit = "10.uN.s/(cm5.m2)";
    assertOneToOneUnitMappingFromUnit(unit, expectedUcumUnit);
  }

  @Test
  void localUnitReturnsNotOneToOneMapping() {
    String unit = "pg/ml", expectedUcumUnit = "ug/L";
    BigDecimal conversion = new BigDecimal("0.001");
    assertUnitMappingFromUnit(unit, expectedUcumUnit, conversion);
  }
}
