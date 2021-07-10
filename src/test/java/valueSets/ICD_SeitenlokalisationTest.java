package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class ICD_SeitenlokalisationTest {
  @Test
  void testSystem() {
    String expectedSystem = "https://fhir.kbv.de/CodeSystem/KBV_CS_SFHIR_ICD_SEITENLOKALISATION";
    assertSimpleSystem(expectedSystem, ICD_Seitenlokalisation.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(ICD_Seitenlokalisation.class);
  }
}
