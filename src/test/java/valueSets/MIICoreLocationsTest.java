package valueSets;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("MIICoreLocationsCodes.json");
    assertValidCodes(codes, MIICoreLocations::fromCode);
    assertEquals(codes.size(), MIICoreLocations.values().length);
  }
}