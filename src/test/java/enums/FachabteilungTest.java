package enums;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class FachabteilungTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://fhir.de/CodeSystem/dkgev/Fachabteilungsschluessel";
    assertSimpleSystem(expectedSystem, Fachabteilung.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("FachabteilungCodes.json");
    assertValidCodes(codes, Fachabteilung::fromCode);
    assertEquals(codes.size(), Fachabteilung.values().length);
  }
}
