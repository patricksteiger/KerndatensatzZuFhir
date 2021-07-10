package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class KBVBaseStageLifeTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, KBVBaseStageLife.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(KBVBaseStageLife.class);
  }
}
