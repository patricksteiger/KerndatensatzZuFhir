package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class AufnahmeanlassTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://fhir.de/CodeSystem/dgkev/Aufnahmeanlass";
    assertSimpleSystem(expectedSystem, Aufnahmeanlass.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("AufnahmeanlassCodes.json");
    assertValidCodes(Aufnahmeanlass.values(), Aufnahmeanlass::fromCode, actualCodes);
  }
}
