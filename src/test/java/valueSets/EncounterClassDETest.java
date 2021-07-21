package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class EncounterClassDETest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/v3-ActCode";
    assertSimpleSystem(expectedSystem, EncounterClassDE.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(EncounterClassDE.class);
  }
}
