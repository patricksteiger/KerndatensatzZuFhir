package valueSets;

import org.junit.jupiter.api.Test;

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
  void testAllValidCodes() {
    assertValidCodes(Laborstruktur.class);
  }
}
