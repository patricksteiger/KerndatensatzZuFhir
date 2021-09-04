package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class ClinicalStatusTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/condition-clinical";
    assertSimpleSystem(expectedSystem, ClinicalStatus.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("ClinicalStatusCodes.json");
    assertValidCodes(ClinicalStatus.values(), ClinicalStatus::fromCode, actualCodes);
  }
}
