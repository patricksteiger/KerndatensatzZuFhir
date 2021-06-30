package enums;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class ICD_DiagnosesicherheitTest {
  @Test
  void testSystem() {
    String expectedSystem = "https://fhir.kbv.de/CodeSystem/KBV_CS_SFHIR_ICD_DIAGNOSESICHERHEIT";
    assertSimpleSystem(expectedSystem, ICD_Diagnosesicherheit.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("ICD_DiagnosesicherheitCodes.json");
    assertValidCodes(codes, ICD_Diagnosesicherheit::fromCode);
    assertEquals(codes.size(), ICD_Diagnosesicherheit.values().length);
  }
}
