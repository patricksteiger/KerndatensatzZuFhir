package valueSets;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertValidCodes;

class LaborbereichTest {
  @Test
  void testSystem() {
    String loincSystem = "http://loinc.org",
        geneticsSystem = "http://terminology.hl7.org/CodeSystem/v2-0074";
    for (Laborbereich laborbereich : Laborbereich.values()) {
      if ("GE".equals(laborbereich.getCode()))
        assertEquals(geneticsSystem, laborbereich.getSystem(), "Code: GE");
      else assertEquals(loincSystem, laborbereich.getSystem(), "Code: " + laborbereich.getCode());
    }
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("LaborbereichCodes.json");
    assertValidCodes(codes, Laborbereich::fromCode);
    assertEquals(codes.size(), Laborbereich.values().length);
  }
}
