package model.fall;

import constants.CodingSystem;
import constants.IdentifierSystem;
import enums.IdentifierTypeCode;
import helper.Logger;
import model.Fall;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Asserter.*;
import static util.Util.*;

class VersorgungsstellenEncounterTest {
  private Fall fall;

  @BeforeEach
  void setUp() throws Exception {
    Logger LOGGER = Mockito.mock(Logger.class, Mockito.CALLS_REAL_METHODS);
    setUpLoggerMock(LOGGER);
    fall = new Fall();
    setMockLoggerField(fall, LOGGER);
  }

  @Test
  void testIdentifier() {
    // empty aufnahmenummer [NO LOGGING]
    fall.setVersorgungsstellenkontakt_aufnahmenummer("");
    assertEmptyValue(fall.getVersorgungsstellenEncounterIdentifier());
    fall.setVersorgungsstellenkontakt_aufnahmenummer(null);
    assertEmptyValue(fall.getVersorgungsstellenEncounterIdentifier());
    // non-empty aufnahmenummer
    String aufnahmenummer = "1a2b";
    fall.setVersorgungsstellenkontakt_aufnahmenummer(aufnahmenummer);
    Identifier result = fall.getVersorgungsstellenEncounterIdentifier();
    assertIdentifier(aufnahmenummer, IdentifierSystem.ACME_PATIENT, IdentifierTypeCode.VN, result);
  }

  @Test
  void testStatus() {
    assertEquals(Encounter.EncounterStatus.FINISHED, fall.getVersorgungsstellenEncounterStatus());
  }

  @Test
  void testClass() {
    // empty klasse
    assertEmptyCodeValue(
        fall::setVersorgungsstellenkontakt_klasse, fall::getVersorgungsstellenEncounterClass);
    // non-empty klasse
    String code = "1a2c";
    String display = "display";
    String klasse = getCodeDisplayStr(code, display);
    fall.setVersorgungsstellenkontakt_klasse(klasse);
    Coding result = fall.getVersorgungsstellenEncounterClass();
    assertCoding(code, CodingSystem.ENCOUNTER_CLASS_DE, display, result);
  }

  @Test
  void testType() {
    // empty ebene [NO LOGGING]
    assertEmptyCodeValue(
        fall::setVersorgungsstellenkontakt_ebene, fall::getVersorgungsstellenEncounterType);
    // non-empty ebene
    String code = "423gjh";
    String display = "correct display";
    String ebene = getCodeDisplayStr(code, display);
    fall.setVersorgungsstellenkontakt_ebene(ebene);
    CodeableConcept result = fall.getVersorgungsstellenEncounterType();
    assertCodeableConcept(code, CodingSystem.FALL_KONTAKTEBENE, display, result);
  }

  @Test
  void testPeriod() {
    // empty beginndatum
    fall.setVersorgungsstellenkontakt_beginndatum("");
    assertEmptyValue(fall.getVersorgungsstellenEncounterPeriod());
    // invalid beginndatum
    fall.setVersorgungsstellenkontakt_beginndatum("invalid");
    assertEmptyValue(fall.getVersorgungsstellenEncounterPeriod());
    // valid beginndatum
    String startDate = "2021-02-04";
    fall.setVersorgungsstellenkontakt_beginndatum(startDate);
    Period result = fall.getVersorgungsstellenEncounterPeriod();
    assertPeriod(expectedDateString(startDate), null, result);
    // valid enddatum
    String endDate = "2021-03-04";
    fall.setVersorgungsstellenkontakt_enddatum(endDate);
    result = fall.getVersorgungsstellenEncounterPeriod();
    assertPeriod(expectedDateString(startDate), expectedDateString(endDate), result);
  }
}
