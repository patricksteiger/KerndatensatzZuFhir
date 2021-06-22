package enums;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static valueSet.CodeUtil.assertValidCodes;

class EntlassungsgrundTest {
  @Test
  void testAllValidCodes() throws FileNotFoundException {
    List<CodeDto> codes = CodeUtil.get("EntlassungsgrundCodes.json");
    assertEquals(codes.size(), Entlassungsgrund.values().length);
    assertValidCodes(codes, Entlassungsgrund::fromCode);
  }
}