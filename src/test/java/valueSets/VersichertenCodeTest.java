package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class VersichertenCodeTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://fhir.de/CodeSystem/identifier-type-de-basis";
    assertSimpleSystem(expectedSystem, VersichertenCode.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(VersichertenCode.class);
  }
}
