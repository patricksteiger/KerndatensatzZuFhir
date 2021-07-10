package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class WirkstofftypTest {
  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/modul-medikation/CodeSystem/wirkstofftyp";
    assertSimpleSystem(expectedSystem, Wirkstofftyp.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(Wirkstofftyp.class);
  }
}
