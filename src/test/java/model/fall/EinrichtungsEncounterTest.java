package model.fall;

import constants.CodingSystem;
import constants.IdentifierSystem;
import enums.*;
import helper.Logger;
import model.Fall;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Period;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

class EinrichtungsEncounterTest {
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
    fall.setEinrichtungskontakt_aufnahmenummer("");
    assertEmptyValue(fall.getEinrichtungsEncounterIdentifier());
    fall.setEinrichtungskontakt_aufnahmenummer(null);
    assertEmptyValue(fall.getEinrichtungsEncounterIdentifier());
    // non-empty
    String aufnahmenummer = "123";
    fall.setEinrichtungskontakt_aufnahmenummer(aufnahmenummer);
    Identifier result = fall.getEinrichtungsEncounterIdentifier();
    assertIdentifier(aufnahmenummer, IdentifierSystem.ACME_PATIENT, IdentifierTypeCode.VN, result);
  }

  @Test
  void testStatus() {
    assertEquals(Encounter.EncounterStatus.FINISHED, fall.getEinrichtungsEncounterStatus());
  }

  @Test
  void testClass() {
    // empty klasse
    String klasse = "";
    fall.setEinrichtungskontakt_klasse(klasse);
    assertEmptyValue(fall.getEinrichtungsEncounterClass());
    assertLoggerHasCalledEmptyValue(LOGGER, 1);
    // non-empty klasse
    String code = "12345";
    String display = "very good klasse";
    klasse = getCodeDisplayStr(code, display);
    fall.setEinrichtungskontakt_klasse(klasse);
    assertCoding(
        code, CodingSystem.ENCOUNTER_CLASS_DE, display, fall.getEinrichtungsEncounterClass());
    assertLoggerHasCalledEmptyValue(LOGGER, 1);
  }

  @Test
  void testType() {
    // empty ebene
    String ebene = "";
    fall.setEinrichtungskontakt_ebene(ebene);
    assertEmptyValue(fall.getEinrichtungsEncounterType());
    // non-empty ebene
    String code = "1a2b";
    String display = "test display";
    ebene = getCodeDisplayStr(code, display);
    fall.setEinrichtungskontakt_ebene(ebene);
    assertCodeableConcept(
        code, CodingSystem.FALL_KONTAKTEBENE, display, fall.getEinrichtungsEncounterType());
  }

  @Test
  void testServiceType() {
    assertCodeableConcept(Fachabteilung.INNERE_MEDIZIN, fall.getEinrichtungsEncounterServiceType());
  }

  @Test
  void testReasonCode() {
    // empty aufnahmegrund
    assertEmptyCodeValue(
        fall::setEinrichtungskontakt_aufnahmegrund, fall::getEinrichtungsEncounterReasonCode);
    assertLoggerHasCalledError3(LOGGER, 0);
    // invalid aufnahmegrund
    String code = getCodeStr("invalid");
    fall.setEinrichtungskontakt_aufnahmegrund(code);
    assertEmptyValue(fall.getEinrichtungsEncounterReasonCode());
    assertLoggerHasCalledError3(LOGGER, 1);
    // valid aufnahmegrund
    Aufnahmegrund aufnahmegrund = Aufnahmegrund.G01;
    code = getCodeStr(aufnahmegrund.getCode());
    fall.setEinrichtungskontakt_aufnahmegrund(code);
    CodeableConcept result = fall.getEinrichtungsEncounterReasonCode();
    assertCodeableConcept(aufnahmegrund, result);
    assertLoggerHasCalledError3(LOGGER, 1);
  }

  @Test
  void testPeriod() {
    // beginndatum is required
    fall.setEinrichtungskontakt_beginndatum("");
    assertEmptyValue(fall.getEinrichtungsEncounterPeriod());
    assertLoggerHasCalledError3(LOGGER, 1);
    // enddatum is optional
    String startDate = "2021-03-15";
    fall.setEinrichtungskontakt_beginndatum(startDate);
    Period result = fall.getEinrichtungsEncounterPeriod();
    assertPeriod(expectedDateString(startDate), null, result);
    assertLoggerHasCalledError3(LOGGER, 1);
    // both are set
    fall.setEinrichtungskontakt_beginndatum(startDate);
    String endDate = "2021-04-17";
    fall.setEinrichtungskontakt_enddatum(endDate);
    result = fall.getEinrichtungsEncounterPeriod();
    assertPeriod(expectedDateString(startDate), expectedDateString(endDate), result);
    assertLoggerHasCalledError3(LOGGER, 1);
    // beginndatum is required, even if enddatum is set
    fall.setEinrichtungskontakt_beginndatum("");
    fall.setEinrichtungskontakt_enddatum(endDate);
    result = fall.getEinrichtungsEncounterPeriod();
    assertEmptyValue(result);
    assertLoggerHasCalledError3(LOGGER, 2);
  }

  @Test
  void testDischargeDisposition() {
    // empty entlassungsgrund [NO LOGGING]
    assertEmptyCodeValue(
        fall::setEinrichtungskontakt_entlassungsgrund,
        fall::getEinrichtungsEncounterDischargeDisposition);
    assertLoggerHasCalledError3(LOGGER, 0);
    // invalid entlassungsgrund
    String entlassungsgrund = getCodeDisplayStr("invalid", "failed display");
    fall.setEinrichtungskontakt_entlassungsgrund(entlassungsgrund);
    assertEmptyValue(fall.getEinrichtungsEncounterDischargeDisposition());
    assertLoggerHasCalledError3(LOGGER, 1);
    // valid entlassungsgrund
    Entlassungsgrund grund = Entlassungsgrund.G15;
    entlassungsgrund = getCodeDisplayStr(grund);
    fall.setEinrichtungskontakt_entlassungsgrund(entlassungsgrund);
    CodeableConcept result = fall.getEinrichtungsEncounterDischargeDisposition();
    assertCodeableConcept(grund, result);
    assertLoggerHasCalledError3(LOGGER, 1);
  }

  @Test
  void testAdmitSource() {
    // empty aufnahmeanlass
    assertEmptyCodeValue(
        fall::setEinrichtungskontakt_aufnahmeanlass, fall::getEinrichtungsEncounterAdmitSource);
    assertLoggerHasCalledError3(LOGGER, 0);
    // invalid aufnahmeanlass
    String anlass = getCodeDisplayStr("invalid", "invalid display");
    fall.setEinrichtungskontakt_aufnahmeanlass(anlass);
    assertEmptyValue(fall.getEinrichtungsEncounterAdmitSource());
    assertLoggerHasCalledError3(LOGGER, 1);
    // valid aufnahmeanlass
    Aufnahmeanlass aufnahmeanlass = Aufnahmeanlass.NOTFALL;
    anlass = getCodeDisplayStr(aufnahmeanlass);
    fall.setEinrichtungskontakt_aufnahmeanlass(anlass);
    CodeableConcept result = fall.getEinrichtungsEncounterAdmitSource();
    assertCodeableConcept(aufnahmeanlass, result);
    assertLoggerHasCalledError3(LOGGER, 1);
  }

  @Test
  void testHospitalization() {
    // empty aufnahmeanlass/entlassungsgrund
    fall.setEinrichtungskontakt_aufnahmeanlass("");
    fall.setEinrichtungskontakt_entlassungsgrund("");
    Encounter.EncounterHospitalizationComponent result =
        fall.getEinrichtungsEncounterHospitalization();
    assertFalse(result.hasAdmitSource());
    assertFalse(result.hasDischargeDisposition());
    // valid aufnahmeanlass/entlassungsgrund
    Aufnahmeanlass aufnahmeanlass = Aufnahmeanlass.GEBURT;
    Entlassungsgrund entlassungsgrund = Entlassungsgrund.G011;
    fall.setEinrichtungskontakt_aufnahmeanlass(getCodeDisplayStr(aufnahmeanlass));
    fall.setEinrichtungskontakt_entlassungsgrund(getCodeDisplayStr(entlassungsgrund));
    result = fall.getEinrichtungsEncounterHospitalization();
    assertTrue(result.hasAdmitSource());
    assertTrue(result.hasDischargeDisposition());
  }
}
