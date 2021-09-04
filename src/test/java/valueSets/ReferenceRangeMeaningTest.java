package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class ReferenceRangeMeaningTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/referencerange-meaning";
    assertSimpleSystem(expectedSystem, ReferenceRangeMeaning.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("ReferenceRangeMeaningCodes.json");
    assertValidCodes(ReferenceRangeMeaning.values(), ReferenceRangeMeaning::fromCode, actualCodes);
  }
}
