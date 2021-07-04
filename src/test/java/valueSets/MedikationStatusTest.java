package valueSets;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MedikationStatusTest {
  private static void assertMedikationStatus(String code) {
    Optional<MedikationStatus> status = MedikationStatus.fromCode(code);
    assertTrue(status.isPresent(), "Expected Code: " + code);
    assertEquals(code, status.get().getCode());
  }

  @Test
  void testAllValidCodes() {
    String[] validCodes = {
      "aktiv", "abgeschlossen", "geplant", "unterbrochen", "abgebrochen", "unbekannt"
    };
    for (String code : validCodes) {
      assertMedikationStatus(code);
    }
    assertEquals(validCodes.length, MedikationStatus.values().length);
  }
}
