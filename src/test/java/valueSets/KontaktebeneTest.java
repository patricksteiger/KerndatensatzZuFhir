package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class KontaktebeneTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://fhir.de/CodeSystem/Kontaktebene";
    assertSimpleSystem(expectedSystem, Kontaktebene.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(Kontaktebene.class);
  }
}
