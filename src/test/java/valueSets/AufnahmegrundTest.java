package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertValidCodes;

class AufnahmegrundTest {
  @Test
  void testAllValidCodes() {
    assertValidCodes(Aufnahmegrund.class);
  }
}
