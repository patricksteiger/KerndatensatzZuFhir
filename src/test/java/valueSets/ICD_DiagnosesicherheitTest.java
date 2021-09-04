package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class ICD_DiagnosesicherheitTest {
  @Test
  void testSystem() {
    String expectedSystem = "https://fhir.kbv.de/CodeSystem/KBV_CS_SFHIR_ICD_DIAGNOSESICHERHEIT";
    assertSimpleSystem(expectedSystem, ICD_Diagnosesicherheit.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("ICD_DiagnosesicherheitCodes.json");
    assertValidCodes(
        ICD_Diagnosesicherheit.values(), ICD_Diagnosesicherheit::fromCode, actualCodes);
  }
}
