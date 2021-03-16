package model.fall;

import constants.CodingSystem;
import constants.IdentifierSystem;
import enums.*;
import model.Fall;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static util.Asserter.*;
import static util.Util.*;

class EinrichtungsEncounterTest {
  private Fall fall;

  @BeforeEach
  void setUp() {
    fall = new Fall();
  }

  @Test
  void testIdentifier() {
    // empty aufnahmenummer [NO LOGGING]
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
    // non-empty klasse
    String code = "12345";
    String display = "very good klasse";
    klasse = getCodeDisplayStr(code, display);
    fall.setEinrichtungskontakt_klasse(klasse);
    assertCoding(
        code, CodingSystem.ENCOUNTER_CLASS_DE, display, fall.getEinrichtungsEncounterClass());
  }

  @Test
  void testType() {
    // empty ebene [NO LOGGING]
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
    // empty aufnahmegrund [NO LOGGING]
    fall.setEinrichtungskontakt_aufnahmegrund("");
    assertEmptyValue(fall.getEinrichtungsEncounterReasonCode());
    String emptyCode = getCodeStr("");
    fall.setEinrichtungskontakt_aufnahmegrund(emptyCode);
    assertEmptyValue(fall.getEinrichtungsEncounterReasonCode());
    // invalid aufnahmegrund
    String code = getCodeStr("invalid");
    fall.setEinrichtungskontakt_aufnahmegrund(code);
    assertEmptyValue(fall.getEinrichtungsEncounterReasonCode());
    // valid aufnahmegrund
    Aufnahmegrund aufnahmegrund = Aufnahmegrund.G01;
    code = getCodeStr(aufnahmegrund.getCode());
    fall.setEinrichtungskontakt_aufnahmegrund(code);
    CodeableConcept result = fall.getEinrichtungsEncounterReasonCode();
    assertCodeableConcept(aufnahmegrund, result);
  }

  @Test
  void testPeriod() {
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

  @Test
  void testDischargeDisposition() {
    // empty entlassungsgrund [NO LOGGING]
    fall.setEinrichtungskontakt_entlassungsgrund("");
    assertEmptyValue(fall.getEinrichtungsEncounterDischargeDisposition());
    fall.setEinrichtungskontakt_entlassungsgrund(null);
    assertEmptyValue(fall.getEinrichtungsEncounterDischargeDisposition());
    // invalid entlassungsgrund
    String entlassungsgrund = getCodeDisplayStr("invalid", "failed display");
    fall.setEinrichtungskontakt_entlassungsgrund(entlassungsgrund);
    assertEmptyValue(fall.getEinrichtungsEncounterDischargeDisposition());
    // valid entlassungsgrund
    Entlassungsgrund grund = Entlassungsgrund.G15;
    entlassungsgrund = getCodeDisplayStr(grund.getCode(), grund.getDisplay());
    fall.setEinrichtungskontakt_entlassungsgrund(entlassungsgrund);
    CodeableConcept result = fall.getEinrichtungsEncounterDischargeDisposition();
    assertCodeableConcept(grund, result);
  }

  @Test
  void testAdmitSource() {
    // empty aufnahmeanlass [NO LOGGING]
    fall.setEinrichtungskontakt_aufnahmeanlass("");
    assertEmptyValue(fall.getEinrichtungsEncounterAdmitSource());
    fall.setEinrichtungskontakt_aufnahmeanlass(null);
    assertEmptyValue(fall.getEinrichtungsEncounterAdmitSource());
    fall.setEinrichtungskontakt_aufnahmeanlass(getCodeStr(""));
    assertEmptyValue(fall.getEinrichtungsEncounterAdmitSource());
    // invalid aufnahmeanlass
    String anlass = getCodeDisplayStr("invalid", "invalid display");
    fall.setEinrichtungskontakt_aufnahmeanlass(anlass);
    assertEmptyValue(fall.getEinrichtungsEncounterAdmitSource());
    // valid aufnahmeanlass
    Aufnahmeanlass aufnahmeanlass = Aufnahmeanlass.NOTFALL;
    anlass = getCodeDisplayStr(aufnahmeanlass.getCode(), aufnahmeanlass.getDisplay());
    fall.setEinrichtungskontakt_aufnahmeanlass(anlass);
    CodeableConcept result = fall.getEinrichtungsEncounterAdmitSource();
    assertCodeableConcept(aufnahmeanlass, result);
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
    fall.setEinrichtungskontakt_aufnahmeanlass(
        getCodeDisplayStr(aufnahmeanlass.getCode(), aufnahmeanlass.getDisplay()));
    fall.setEinrichtungskontakt_entlassungsgrund(
        getCodeDisplayStr(entlassungsgrund.getCode(), entlassungsgrund.getDisplay()));
    result = fall.getEinrichtungsEncounterHospitalization();
    assertTrue(result.hasAdmitSource());
    assertTrue(result.hasDischargeDisposition());
  }
}
