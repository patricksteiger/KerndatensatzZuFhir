package enums;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class ReferenceRangeMeaningTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/referencerange-meaning";
    assertSimpleSystem(expectedSystem, ReferenceRangeMeaning.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("ReferenceRangeMeaningCodes.json");
    assertValidCodes(codes, ReferenceRangeMeaning::fromCode);
    assertEquals(codes.size(), ReferenceRangeMeaning.values().length);
  }
}