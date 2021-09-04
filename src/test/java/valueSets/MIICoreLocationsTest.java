package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class MIICoreLocationsTest {
  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/CodeSystem/core-location-identifier";
    assertSimpleSystem(expectedSystem, MIICoreLocations.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("MIICoreLocationsCodes.json");
    assertValidCodes(MIICoreLocations.values(), MIICoreLocations::fromCode, actualCodes);
  }
}
