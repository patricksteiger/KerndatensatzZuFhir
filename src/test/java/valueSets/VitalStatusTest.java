package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class VitalStatusTest {
  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/modul-person/CodeSystem/Vitalstatus";
    assertSimpleSystem(expectedSystem, VitalStatus.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(VitalStatus.class);
  }
}
