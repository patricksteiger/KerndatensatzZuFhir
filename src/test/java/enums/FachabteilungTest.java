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
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("FachabteilungCodes.json");
    assertEquals(codes.size(), Fachabteilung.values().length);
    assertValidCodes(codes, Fachabteilung::fromCode);
  }
}
