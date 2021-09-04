package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class DurchfuehrungsabsichtCodeTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, DurchfuehrungsabsichtCode.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("DurchfuehrungsabsichtCodeCodes.json");
    assertValidCodes(
        DurchfuehrungsabsichtCode.values(), DurchfuehrungsabsichtCode::fromCode, actualCodes);
  }
}
