package helper;

import org.hl7.fhir.r4.model.Quantity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ParsedRatioTest {
  @Test
  void testEmpty() {
    String code = null;
    ParsedRatio result = ParsedRatio.fromString(code);
    assertFalse(result.toRatio().isPresent());
    code = "";
    result = ParsedRatio.fromString(code);
    assertFalse(result.toRatio().isPresent());
  }

  @Test
  void testInvalidUnit() {
    String code = "value=\"1/2\"";
    ParsedRatio result = ParsedRatio.fromString(code);
    assertFalse(result.toRatio().isPresent());
    code = "value=\"1/2\" unit=\"g\"";
    result = ParsedRatio.fromString(code);
    assertFalse(result.toRatio().isPresent());
  }

  @Test
  void testBothNumAndDenom() {
    String code = "value=\"1/2\" unit=\"g/l\"";
    ParsedRatio result = ParsedRatio.fromString(code);
    assertTrue(result.toRatio().isPresent());
    Quantity numerator = result.toRatio().get().getNumerator();
    assertEquals(BigDecimal.ONE, numerator.getValue());
    assertEquals("g", numerator.getUnit());
    Quantity denominator = result.toRatio().get().getDenominator();
    assertEquals(new BigDecimal("2"), denominator.getValue());
    assertEquals("l", denominator.getUnit());
  }
  @Test
  void testOnlyNum() {
    String code = "value=\"2\" unit=\"g/l\"";
    ParsedRatio result = ParsedRatio.fromString(code);
    assertTrue(result.toRatio().isPresent());
    Quantity numerator = result.toRatio().get().getNumerator();
    assertEquals(new BigDecimal("2"), numerator.getValue());
    assertEquals("g", numerator.getUnit());
    Quantity denominator = result.toRatio().get().getDenominator();
    assertEquals(BigDecimal.ONE, denominator.getValue());
    assertEquals("l", denominator.getUnit());
  }
}
