package helper;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.CodeUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static helper.DoseFormUvIps.validate;
import static org.junit.jupiter.api.Assertions.*;

class DoseFormUvIpsTest {
  @Test
  void testInvalidCodes() {
    assertFalse(validate(null));
    assertFalse(validate(""));
    assertFalse(validate("123"));
    assertFalse(validate("1234567890"));
    assertFalse(validate("40050"));
    assertFalse(validate("39000"));
    assertFalse(validate("50049999"));
    assertFalse(validate("50077300"));
    assertFalse(validate("50075000"));
  }

  @Test
  void testAllValidCodes() throws FileNotFoundException {
    File actualCodes = CodeUtil.getResourcePrefixFile("DoseFormUvIpsCodes.json");
    List<CodeDto> codes = CodeUtil.get(actualCodes);
    assertEquals(461, codes.size());
    codes.forEach(code -> assertTrue(validate(code.getCode())));
  }
}
