package enums;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class KontaktebeneTest {
  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/modul-fall/CodeSystem/Kontaktebene";
    assertSimpleSystem(expectedSystem, Kontaktebene.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("KontaktebeneCodes.json");
    assertValidCodes(codes, Kontaktebene::fromCode);
    assertEquals(codes.size(), Kontaktebene.values().length);
  }
}
