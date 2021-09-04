package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class DiagnosesicherheitTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, Diagnosesicherheit.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("DiagnosesicherheitCodes.json");
    assertValidCodes(Diagnosesicherheit.values(), Diagnosesicherheit::fromCode, actualCodes);
  }
}
