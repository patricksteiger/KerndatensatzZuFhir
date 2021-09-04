package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class BehandlungsgrundTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/reason-medication-given";
    assertSimpleSystem(expectedSystem, Behandlungsgrund.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("BehandlungsgrundCodes.json");
    assertValidCodes(Behandlungsgrund.values(), Behandlungsgrund::fromCode, actualCodes);
  }
}
