package enums;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class BehandlungsgrundTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://terminology.hl7.org/CodeSystem/reason-medication-given";
    assertSimpleSystem(expectedSystem, Behandlungsgrund.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("BehandlungsgrundCodes.json");
    assertValidCodes(codes, Behandlungsgrund::fromCode);
    assertEquals(codes.size(), Behandlungsgrund.values().length);
  }
}
