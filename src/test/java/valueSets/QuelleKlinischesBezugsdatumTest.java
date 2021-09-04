package valueSets;

import org.junit.jupiter.api.Test;

import java.io.File;

import static valueSet.CodeUtil.*;

class QuelleKlinischesBezugsdatumTest {
  @Test
  void testSystem() {
    String expectedSystem = "http://snomed.info/sct";
    assertSimpleSystem(expectedSystem, QuelleKlinischesBezugsdatum.values());
  }

  @Test
  void testAllValidCodes() {
    File actualCodes = getResourcePrefixFile("QuelleKlinischesBezugsdatumCodes.json");
    assertValidCodes(
        QuelleKlinischesBezugsdatum.values(), QuelleKlinischesBezugsdatum::fromCode, actualCodes);
  }
}
