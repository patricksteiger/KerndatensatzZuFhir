package enums;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

/**
 * @see
 *     "https://art-decor.org/decor/services/RetrieveValueSet?prefix=mide-&language=de-DE&version=&id=1.2.40.0.34.10.47&effectiveDate=2018-01-18T00%3A00%3A00&format=json"
 */
class LaborstrukturTest {
  @Test
  void testSystem() {
    String expectedSystem = "urn:oid:1.2.40.0.34.5.11";
    assertSimpleSystem(expectedSystem, Laborstruktur.values());
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("LaborstrukturCodes.json");
    assertValidCodes(codes, Laborstruktur::fromCode);
    assertEquals(codes.size(), Laborstruktur.values().length);
  }
}
