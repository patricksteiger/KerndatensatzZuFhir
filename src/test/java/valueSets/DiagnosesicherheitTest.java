package valueSets;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class DiagnosesicherheitTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, Diagnosesicherheit.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("DiagnosesicherheitCodes.json");
    assertValidCodes(codes, Diagnosesicherheit::fromCode);
    assertEquals(codes.size(), Diagnosesicherheit.values().length);
  }
}