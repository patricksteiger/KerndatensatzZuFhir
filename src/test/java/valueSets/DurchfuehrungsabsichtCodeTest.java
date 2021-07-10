package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class DurchfuehrungsabsichtCodeTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, DurchfuehrungsabsichtCode.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(DurchfuehrungsabsichtCode.class);
  }
}
