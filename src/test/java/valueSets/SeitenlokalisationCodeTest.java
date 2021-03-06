package valueSets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertValidCodes;

class SeitenlokalisationCodeTest {
  @Test
  void testSystem() {
    String system = "urn:oid:2.16.840.1.113883.3.7.1.7",
        errorSystem = "http://terminology.hl7.org/CodeSystem/v3-NullFlavor";
    for (SeitenlokalisationCode code : SeitenlokalisationCode.values()) {
      if ("NA".equals(code.getCode()) || "UNK".equals(code.getCode())) {
        assertEquals(errorSystem, code.getSystem(), "Code: " + code.getCode());
      } else {
        assertEquals(system, code.getSystem(), "Code: " + code.getCode());
      }
    }
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(SeitenlokalisationCode.class);
  }
}
