package valueSets;

import org.junit.jupiter.api.Test;

import static valueSet.CodeUtil.assertSimpleSystem;
import static valueSet.CodeUtil.assertValidCodes;

class QuelleKlinischesBezugsdatumTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, QuelleKlinischesBezugsdatum.values());
  }

  @Test
  void testAllValidCodes() {
    assertValidCodes(QuelleKlinischesBezugsdatum.class);
  }
}
