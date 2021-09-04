package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class VitalStatusTest {
  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/modul-person/CodeSystem/Vitalstatus";
    assertSimpleSystem(expectedSystem, VitalStatus.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("VitalStatusCodes.json");
    assertValidCodes(VitalStatus.values(), VitalStatus::fromCode, actualCodes);
  }
}
