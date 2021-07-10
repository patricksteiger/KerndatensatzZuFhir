package valueSets;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static valueSets.ProcedureCategorySnomedMapping.*;

class ProcedureCategorySnomedMappingTest {
  @Test
  void testInvalidCodes() {
    assertFalse(fromOpsCode(null).isPresent());
    assertFalse(fromOpsCode("").isPresent());
    assertFalse(fromOpsCode("test").isPresent());
    assertFalse(fromOpsCode("0").isPresent());
  }

  @Test
  void testAllValidResults() {
    assertMapping(DIAGNOSTIC, "1");
    assertMapping(IMAGING, "3");
    assertMapping(SURGICAL, "5");
    assertMapping(ADMINISTRATION_OF_MEDICINE, "6");
    assertMapping(THERAPEUTIC, "8");
    assertMapping(OTHER, "9");
  }

  private void assertMapping(ProcedureCategorySnomedMapping expectedMapping, String input) {
    Optional<ProcedureCategorySnomedMapping> actual = fromOpsCode(input);
    assertTrue(actual.isPresent(), "Code: " + input);
    assertEquals(expectedMapping, actual.get());
  }
}
