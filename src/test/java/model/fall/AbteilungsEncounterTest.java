package model.fall;

import constants.CodingSystem;
import constants.IdentifierSystem;
import enums.Fachabteilung;
import enums.IdentifierTypeCode;
import enums.Kontaktebene;
import helper.Logger;
import model.Fall;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

class AbteilungsEncounterTest {
  private static Logger LOGGER;
  private Fall fall;

  @BeforeAll
  static void init() {
    LOGGER = Mockito.mock(Logger.class, Mockito.CALLS_REAL_METHODS);
    setUpLoggerMock(LOGGER);
  }

  @BeforeEach
  public void setUp() throws Exception {
    clearInvocations(LOGGER);
    fall = new Fall();
    setMockLoggerField(fall, LOGGER);
  }

  @Test
  void testIdentifier() {
    // empty aufnahmenummer
    fall.setAbteilungskontakt_aufnahmenummer("");
    assertEmptyValue(fall.getAbteilungsEncounterIdentifier());
    fall.setAbteilungskontakt_aufnahmenummer(null);
    assertEmptyValue(fall.getAbteilungsEncounterIdentifier());
    // non-empty
    String aufnahmenummer = "1a2b";
    fall.setAbteilungskontakt_aufnahmenummer(aufnahmenummer);
    Identifier result = fall.getAbteilungsEncounterIdentifier();
    assertIdentifier(aufnahmenummer, IdentifierSystem.ACME_PATIENT, IdentifierTypeCode.VN, result);
  }

  @Test
  void testStatus() {
    assertEquals(Encounter.EncounterStatus.FINISHED, fall.getAbteilungsEncounterStatus());
  }

  @Test
  void testClass() {
    // empty klasse
    assertEmptyCodeValue(fall::setAbteilungskontakt_klasse, fall::getAbteilungsEncounterClass);
    // non-empty klasse
    String code = "1a2b";
    String display = "correct display";
    String klasse = getCodeDisplayStr(code, display);
    fall.setAbteilungskontakt_klasse(klasse);
    Coding result = fall.getAbteilungsEncounterClass();
    assertCoding(code, CodingSystem.ENCOUNTER_CLASS_DE, display, result);
  }

  @Test
  void testServiceType() {
    // empty fachabteilungsschluessel
    assertEmptyCodeValue(
        fall::setAbteilungskontakt_fachabteilungsschluessel,
        fall::getAbteilungsEncounterServiceType);
    // invalid fachabteilungsschluessel
    String code = "invalid";
    String display = "invalid display";
    String schluessel = getCodeDisplayStr(code, display);
    fall.setAbteilungskontakt_fachabteilungsschluessel(schluessel);
    CodeableConcept result = fall.getAbteilungsEncounterServiceType();
    assertCodeableConcept(code, CodingSystem.FALL_FACHABTEILUNGSSCHLUESSEL, display, result);
    // valid
    Fachabteilung fachabteilung = Fachabteilung.AUGENHEILKUNDE;
    code = fachabteilung.getCode();
    display = fachabteilung.getDisplay();
    schluessel = getCodeDisplayStr(code, display);
    fall.setAbteilungskontakt_fachabteilungsschluessel(schluessel);
    result = fall.getAbteilungsEncounterServiceType();
    assertCodeableConcept(fachabteilung, result);
  }

  @Test
  void testType() {
    // empty ebene
    assertEmptyCodeValue(fall::setAbteilungskontakt_ebene, fall::getAbteilungsEncounterType);
    // non-empty ebene
    String code = "abteilungskontakt";
    String ebene = getCodeStr(code);
    fall.setAbteilungskontakt_ebene(ebene);
    CodeableConcept result = fall.getAbteilungsEncounterType();
    assertCodeableConcept(Kontaktebene.ABTEILUNG, result);
  }

  @Test
  void testPeriod() {
    // empty beginndatum
    fall.setAbteilungskontakt_beginndatum("");
    assertEmptyValue(fall.getAbteilungsEncounterPeriod());
    // invalid beginndatum
    fall.setAbteilungskontakt_beginndatum("invalid");
    assertEmptyValue(fall.getAbteilungsEncounterPeriod());
    // valid beginndatum
    String startDate = "2021-02-20";
    fall.setAbteilungskontakt_beginndatum(startDate);
    Period result = fall.getAbteilungsEncounterPeriod();
    assertPeriod(expectedDateString(startDate), null, result);
    // valid enddatum
    String endDate = "2021-02-23";
    fall.setAbteilungskontakt_enddatum(endDate);
    result = fall.getAbteilungsEncounterPeriod();
    assertPeriod(expectedDateString(startDate), expectedDateString(endDate), result);
  }
}
