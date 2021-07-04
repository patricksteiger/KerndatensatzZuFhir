package valueSets;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class VitalStatusTest {
  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/modul-person/CodeSystem/Vitalstatus";
    assertSimpleSystem(expectedSystem, VitalStatus.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("VitalStatusCodes.json");
    assertValidCodes(codes, VitalStatus::fromCode);
    assertEquals(codes.size(), VitalStatus.values().length);
  }
}
