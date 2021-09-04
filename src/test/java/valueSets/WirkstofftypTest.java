package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class WirkstofftypTest {
  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/modul-medikation/CodeSystem/wirkstofftyp";
    assertSimpleSystem(expectedSystem, Wirkstofftyp.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("WirkstofftypCodes.json");
    assertValidCodes(Wirkstofftyp.values(), Wirkstofftyp::fromCode, actualCodes);
  }
}
