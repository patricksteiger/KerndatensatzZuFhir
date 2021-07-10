package valueSets;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class VersichertenCodeTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://fhir.de/CodeSystem/identifier-type-de-basis";
    assertSimpleSystem(expectedSystem, VersichertenCode.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("VersichertenCodeCodes.json");
    assertValidCodes(codes, VersichertenCode::fromCode);
    assertEquals(codes.size(), VersichertenCode.values().length);
  }
}
