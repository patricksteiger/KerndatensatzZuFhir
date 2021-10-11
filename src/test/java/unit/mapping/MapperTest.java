package unit.mapping;

import org.junit.jupiter.api.Test;
import unit.ucum.Ucum;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {

  private static final Path LOINC_MAPPING_PATH =
      Paths.get("src/test/resources/unit/ULM-LOINC_Mapping_Checklist.csv");

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

  private static List<String[]> getAllLocalCodeLines() throws IOException {
    return Files.newBufferedReader(LOINC_MAPPING_PATH, StandardCharsets.ISO_8859_1)
        .lines()
        .skip(1)
        .map(s -> s.split(";"))
        .collect(Collectors.toUnmodifiableList());
  }

  private static Set<LocalCodeWithUnit> getAllLocalCodes() throws IOException {
    return getAllLocalCodeLines().stream()
        .map(line -> new LocalCodeWithUnit(line[3].trim(), line[7].trim()))
        .collect(Collectors.toSet());
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

  @Test
  void allUnitMappingsHaveUcumUnits() {
    Map<String, UnitMapping> mappings = Mapper.generateMappings();
    assertFalse(mappings.isEmpty());
    mappings.forEach(
        (local, mapping) -> {
          String unit = mapping.getUcumCode();
          String message = "Local: " + local + ", Unit: " + unit;
          assertTrue(Ucum.validate(unit), message);
        });
  }

  @Test
  void allLocalCodesAreRepresented() throws IOException {
    var unknownConversion = Mapper.getLocalCodesWithUnknownConversion();
    var localCodesWithUnitAndConversion =
        getAllLocalCodes().stream()
            .filter(c -> !unknownConversion.contains(c.getCode()))
            .collect(Collectors.toUnmodifiableSet());
    var loincMappings = Mapper.generateLoincMappings();
    for (var localCode : localCodesWithUnitAndConversion) {
      String message = "Code: " + localCode.getCode() + ", unit: " + localCode.getUnit();
      assertNotNull(loincMappings.get(localCode), message);
    }
    assertEquals(localCodesWithUnitAndConversion.size(), loincMappings.size());
  }

  @Test
  void allLocalCodesHaveUcumUnits() {
    var loincMappings = Mapper.generateLoincMappings();
    for (var loincMapping : loincMappings.values()) {
      String message = "Unit: " + loincMapping.getUcumCode();
      assertTrue(Ucum.validate(loincMapping.getUcumCode()), message);
    }
  }
}
