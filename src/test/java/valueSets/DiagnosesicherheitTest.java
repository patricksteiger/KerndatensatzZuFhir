package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class DiagnosesicherheitTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, Diagnosesicherheit.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(Diagnosesicherheit.class);
  }
}
