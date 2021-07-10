package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class FachabteilungTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://fhir.de/CodeSystem/dkgev/Fachabteilungsschluessel";
    assertSimpleSystem(expectedSystem, Fachabteilung.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(Fachabteilung.class);
  }
}
