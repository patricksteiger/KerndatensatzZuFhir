package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class ObservationInterpretationTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation";
    assertSimpleSystem(expectedSystem, ObservationInterpretation.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(ObservationInterpretation.class);
  }
}
