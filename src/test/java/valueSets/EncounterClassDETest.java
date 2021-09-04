package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class EncounterClassDETest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/v3-ActCode";
    assertSimpleSystem(expectedSystem, EncounterClassDE.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("EncounterClassDECodes.json");
    assertValidCodes(EncounterClassDE.values(), EncounterClassDE::fromCode, actualCodes);
  }
}
