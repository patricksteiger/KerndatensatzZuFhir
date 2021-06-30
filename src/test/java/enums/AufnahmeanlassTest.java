package enums;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class AufnahmeanlassTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://fhir.de/CodeSystem/dgkev/Aufnahmeanlass";
    assertSimpleSystem(expectedSystem, Aufnahmeanlass.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("AufnahmeanlassCodes.json");
    assertValidCodes(codes, Aufnahmeanlass::fromCode);
    assertEquals(codes.size(), Aufnahmeanlass.values().length);
  }
}
