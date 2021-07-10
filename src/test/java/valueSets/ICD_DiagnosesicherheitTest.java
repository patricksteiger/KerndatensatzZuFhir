package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class ICD_DiagnosesicherheitTest {
  @Test
  void testSystem() {
    String expectedSystem = "https://fhir.kbv.de/CodeSystem/KBV_CS_SFHIR_ICD_DIAGNOSESICHERHEIT";
    assertSimpleSystem(expectedSystem, ICD_Diagnosesicherheit.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(ICD_Diagnosesicherheit.class);
  }
}
