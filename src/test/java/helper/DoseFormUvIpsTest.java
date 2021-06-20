package helper;

import org.junit.jupiter.api.Test;
import valueSet.CodeDto;
import valueSet.DoseFormUvIpsCodes;

import java.io.FileNotFoundException;
import java.util.List;

import static helper.DoseFormUvIps.validate;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoseFormUvIpsTest {
  @Test
  void testValidCodes() {
    assertTrue(validate("22100"));
    assertTrue(validate("50049300"));
    assertTrue(validate("50082000"));
    assertTrue(validate("16040"));
    assertTrue(validate("31030"));
    assertTrue(validate("10402000"));
    assertTrue(validate("10101000"));
    assertTrue(validate("11010"));
    assertTrue(validate("12150"));
    assertTrue(validate("11602000"));
    assertTrue(validate("30047500"));
    assertTrue(validate("29020"));
    assertTrue(validate("50037750"));
  }

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
    List<CodeDto> codes = DoseFormUvIpsCodes.get();
    assertTrue(codes.size() > 400);
    for (CodeDto code : codes) assertTrue(validate(code.getCode()));
  }
}
