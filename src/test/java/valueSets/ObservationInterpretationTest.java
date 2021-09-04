package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class ObservationInterpretationTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation";
    assertSimpleSystem(expectedSystem, ObservationInterpretation.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("ObservationInterpretationCodes.json");
    assertValidCodes(
        ObservationInterpretation.values(), ObservationInterpretation::fromCode, actualCodes);
  }
}
