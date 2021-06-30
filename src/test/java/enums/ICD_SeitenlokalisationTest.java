package enums;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class ICD_SeitenlokalisationTest {
  @Test
  void testSystem() {
    String expectedSystem = "https://fhir.kbv.de/CodeSystem/KBV_CS_SFHIR_ICD_SEITENLOKALISATION";
    assertSimpleSystem(expectedSystem, ICD_Seitenlokalisation.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("ICD_SeitenlokalisationCodes.json");
    assertValidCodes(codes, ICD_Seitenlokalisation::fromCode);
    assertEquals(codes.size(), ICD_Seitenlokalisation.values().length);
  }
}
