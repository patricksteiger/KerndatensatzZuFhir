package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class KontaktebeneTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://fhir.de/CodeSystem/Kontaktebene";
    assertSimpleSystem(expectedSystem, Kontaktebene.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("KontaktebeneCodes.json");
    assertValidCodes(Kontaktebene.values(), Kontaktebene::fromCode, actualCodes);
  }
}
