package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class AufnahmegrundTest {
  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/modul-fall/CodeSystem/Aufnahmegrund";
    assertSimpleSystem(expectedSystem, Aufnahmegrund.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("AufnahmegrundCodes.json");
    assertValidCodes(Aufnahmegrund.values(), Aufnahmegrund::fromCode, actualCodes);
  }
}
