package enums;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class SemiQuantitativesLaborergebnisTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, SemiQuantitativesLaborergebnis.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("SemiQuantitativesLaborergebnisCodes.json");
    assertValidCodes(codes, SemiQuantitativesLaborergebnis::fromCode);
    assertEquals(codes.size(), SemiQuantitativesLaborergebnis.values().length);
  }
}
