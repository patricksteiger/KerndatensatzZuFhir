package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class SemiQuantitativesLaborergebnisTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, SemiQuantitativesLaborergebnis.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("SemiQuantitativesLaborergebnisCodes.json");
    assertValidCodes(
        SemiQuantitativesLaborergebnis.values(),
        SemiQuantitativesLaborergebnis::fromCode,
        actualCodes);
  }
}
