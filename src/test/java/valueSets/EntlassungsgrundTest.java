package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class EntlassungsgrundTest {
  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/modul-fall/CodeSystem/Entlassungsgrund";
    assertSimpleSystem(expectedSystem, Entlassungsgrund.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(Entlassungsgrund.class);
  }
}
