package enums;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertValidCodes;

class FachabteilungTest {

  @Test
  void testSystem() {
    String expectedSystem =
        "https://www.medizininformatik-initiative.de/fhir/core/modul-fall/CodeSystem/Fachabteilungsschluessel";
    for (Fachabteilung fachabteilung : Fachabteilung.values()) {
      assertEquals(
          expectedSystem,
          fachabteilung.getSystem(),
          "Fachabteilungscode: " + fachabteilung.getCode());
    }
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("FachabteilungCodes.json");
    assertValidCodes(codes, Fachabteilung::fromCode);
    assertEquals(codes.size(), Fachabteilung.values().length);
  }
}
