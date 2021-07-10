package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class AufnahmeanlassTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://fhir.de/CodeSystem/dgkev/Aufnahmeanlass";
    assertSimpleSystem(expectedSystem, Aufnahmeanlass.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(Aufnahmeanlass.class);
  }
}
