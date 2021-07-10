package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class SemiQuantitativesLaborergebnisTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, SemiQuantitativesLaborergebnis.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(SemiQuantitativesLaborergebnis.class);
  }
}
