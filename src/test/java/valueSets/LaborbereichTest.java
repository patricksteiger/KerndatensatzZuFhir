package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertValidCodes;
import static valueSet.CodeUtil.getResourcePrefixFile;

class LaborbereichTest {
  @Test
  void testSystem() {
    String loincSystem = "http://loinc.org",
        geneticsSystem = "http://terminology.hl7.org/CodeSystem/v2-0074";
    for (Laborbereich laborbereich : Laborbereich.values()) {
      if ("GE".equals(laborbereich.getCode())) {
        assertEquals(geneticsSystem, laborbereich.getSystem(), "Code: GE");
      } else {
        assertEquals(loincSystem, laborbereich.getSystem(), "Code: " + laborbereich.getCode());
      }
    }
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("LaborbereichCodes.json");
    assertValidCodes(Laborbereich.values(), Laborbereich::fromCode, actualCodes);
  }
}
