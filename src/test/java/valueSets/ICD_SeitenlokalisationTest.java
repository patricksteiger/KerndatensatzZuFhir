package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class ICD_SeitenlokalisationTest {
  @Test
  void testSystem() {
    String expectedSystem = "https://fhir.kbv.de/CodeSystem/KBV_CS_SFHIR_ICD_SEITENLOKALISATION";
    assertSimpleSystem(expectedSystem, ICD_Seitenlokalisation.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("ICD_SeitenlokalisationCodes.json");
    assertValidCodes(
        ICD_Seitenlokalisation.values(), ICD_Seitenlokalisation::fromCode, actualCodes);
  }
}
