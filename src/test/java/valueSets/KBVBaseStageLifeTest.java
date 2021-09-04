package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class KBVBaseStageLifeTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, KBVBaseStageLife.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("KBVBaseStageLifeCodes.json");
    assertValidCodes(KBVBaseStageLife.values(), KBVBaseStageLife::fromCode, actualCodes);
  }
}
