package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class MIICoreLocationsTest {
  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/CodeSystem/core-location-identifier";
    assertSimpleSystem(expectedSystem, MIICoreLocations.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(MIICoreLocations.class);
  }
}
