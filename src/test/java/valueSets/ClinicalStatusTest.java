package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class ClinicalStatusTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/condition-clinical";
    assertSimpleSystem(expectedSystem, ClinicalStatus.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(ClinicalStatus.class);
  }
}
