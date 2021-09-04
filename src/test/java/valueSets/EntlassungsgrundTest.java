package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class EntlassungsgrundTest {
  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/modul-fall/CodeSystem/Entlassungsgrund";
    assertSimpleSystem(expectedSystem, Entlassungsgrund.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("EntlassungsgrundCodes.json");
    assertValidCodes(Entlassungsgrund.values(), Entlassungsgrund::fromCode, actualCodes);
  }
}
