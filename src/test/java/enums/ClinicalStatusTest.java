package enums;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class ClinicalStatusTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/condition-clinical";
    assertSimpleSystem(expectedSystem, ClinicalStatus.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("ClinicalStatusCodes.json");
    assertEquals(codes.size(), ClinicalStatus.values().length);
    assertValidCodes(codes, ClinicalStatus::fromCode);
  }
}
