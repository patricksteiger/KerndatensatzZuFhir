package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class ReferenceRangeMeaningTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/referencerange-meaning";
    assertSimpleSystem(expectedSystem, ReferenceRangeMeaning.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(ReferenceRangeMeaning.class);
  }
}
