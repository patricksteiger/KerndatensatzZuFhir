package valueSets;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class QuelleKlinischesBezugsdatumTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, QuelleKlinischesBezugsdatum.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("QuelleKlinischesBezugsdatumCodes.json");
    assertValidCodes(codes, QuelleKlinischesBezugsdatum::fromCode);
    assertEquals(codes.size(), QuelleKlinischesBezugsdatum.values().length);
  }
}
