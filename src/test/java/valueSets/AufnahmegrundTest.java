package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class AufnahmegrundTest {
  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/modul-fall/CodeSystem/Aufnahmegrund";
    assertSimpleSystem(expectedSystem, Aufnahmegrund.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(Aufnahmegrund.class);
  }
}
