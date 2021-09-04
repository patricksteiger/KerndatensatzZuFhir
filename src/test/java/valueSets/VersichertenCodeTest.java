package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class VersichertenCodeTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://fhir.de/CodeSystem/identifier-type-de-basis";
    assertSimpleSystem(expectedSystem, VersichertenCode.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("VersichertenCodeCodes.json");
    assertValidCodes(VersichertenCode.values(), VersichertenCode::fromCode, actualCodes);
  }
}
