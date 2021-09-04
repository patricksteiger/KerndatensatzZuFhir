package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class FachabteilungTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://fhir.de/CodeSystem/dkgev/Fachabteilungsschluessel";
    assertSimpleSystem(expectedSystem, Fachabteilung.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("FachabteilungCodes.json");
    assertValidCodes(Fachabteilung.values(), Fachabteilung::fromCode, actualCodes);
  }
}
