package model;

import constants.Constants;
import enums.Aufnahmegrund;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FallTest {
  private Fall fall;

  @BeforeEach
  void setUp() {
    fall = new Fall();
  }

  @Test
  void testGetEinrichtungsEncounterReasonCode() {
    // empty aufnahmegrund [NO LOGGING]
    fall.setEinrichtungskontakt_aufnahmegrund("");
    assertEmptyValue(fall.getEinrichtungsEncounterReasonCode());
    String emptyCode = "code=\"\"";
    fall.setEinrichtungskontakt_aufnahmegrund(emptyCode);
    assertEmptyValue(fall.getEinrichtungsEncounterReasonCode());
    // invalid aufnahmegrund
    String code = "code=\"invalid\"";
    fall.setEinrichtungskontakt_aufnahmegrund(code);
    assertEmptyValue(fall.getEinrichtungsEncounterReasonCode());
    // valid aufnahmegrund
    Aufnahmegrund aufnahmegrund = Aufnahmegrund.G01;
    code = "code=\"" + aufnahmegrund.getCode() + "\"";
    fall.setEinrichtungskontakt_aufnahmegrund(code);
    CodeableConcept result = fall.getEinrichtungsEncounterReasonCode();
    assertNonEmptyValue(result);
    assertEquals(1, result.getCoding().size());
    Coding coding = result.getCoding().get(0);
    assertEquals(aufnahmegrund.getCode(), coding.getCode());
    assertEquals(aufnahmegrund.getSystem(), coding.getSystem());
    assertEquals(aufnahmegrund.getDisplay(), coding.getDisplay());
  }

  @Test
  void testGetEinrichtungsEncounterPeriod() {
    // beginndatum is required
    fall.setEinrichtungskontakt_beginndatum("");
    assertEmptyValue(fall.getEinrichtungsEncounterPeriod());
    // enddatum is optional
    String startDate = "2021-03-15";
    fall.setEinrichtungskontakt_beginndatum(startDate);
    Period result = fall.getEinrichtungsEncounterPeriod();
    assertPeriod(expectedDateString(startDate), null, result);
    // both are set
    fall.setEinrichtungskontakt_beginndatum(startDate);
    String endDate = "2021-04-17";
    fall.setEinrichtungskontakt_enddatum(endDate);
    result = fall.getEinrichtungsEncounterPeriod();
    assertPeriod(expectedDateString(startDate), expectedDateString(endDate), result);
    // beginndatum is required, even if enddatum is set
    fall.setEinrichtungskontakt_beginndatum("");
    fall.setEinrichtungskontakt_enddatum(endDate);
    result = fall.getEinrichtungsEncounterPeriod();
    assertEmptyValue(result);
  }

  private String expectedDateString(String date) {
    String[] numbers = date.split("-");
    return numbers[2] + "." + numbers[1] + "." + numbers[0] + " 00:00:00";
  }

  private void assertPeriod(String expectedStart, String expectedEnd, Period period) {
    if (expectedStart == Constants.getEmptyValue()) {
      assertEquals(Constants.getEmptyValue(), period.getStart());
    } else {
      assertEquals(expectedStart, period.getStart().toLocaleString());
    }
    if (expectedEnd == Constants.getEmptyValue()) {
      assertEquals(Constants.getEmptyValue(), period.getEnd());
    } else {
      assertEquals(expectedEnd, period.getEnd().toLocaleString());
    }
  }

  private <T> void assertNonEmptyValue(T value) {
    assertNotNull(value);
  }

  private <T> void assertEmptyValue(T value) {
    assertNull(value);
  }
}
