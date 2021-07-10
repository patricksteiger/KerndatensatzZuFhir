package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class BehandlungsgrundTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/reason-medication-given";
    assertSimpleSystem(expectedSystem, Behandlungsgrund.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(Behandlungsgrund.class);
  }
}
