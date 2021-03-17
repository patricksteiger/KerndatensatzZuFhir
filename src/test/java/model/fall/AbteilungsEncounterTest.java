package model.fall;

import constants.CodingSystem;
import constants.Constants;
import constants.IdentifierSystem;
import enums.Fachabteilung;
import enums.IdentifierTypeCode;
import helper.Logger;
import model.Fall;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static util.Asserter.*;
import static util.Util.expectedDateString;
import static util.Util.getCodeDisplayStr;

class AbteilungsEncounterTest {

  private Fall fall;
  private Logger LOGGER;

  static void setFinalStatic(Fall fall, Field field, Object newValue) throws Exception {
    field.setAccessible(true);
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(fall, newValue);
  }

  @BeforeEach
  void setUp() throws Exception {
    LOGGER = Mockito.mock(Logger.class);
    Mockito.when(LOGGER.emptyValue(any(), any())).thenReturn(Constants::getEmptyValue);
    Mockito.when(LOGGER.error(any(), any())).thenReturn(Constants::getEmptyValue);
    Mockito.when(LOGGER.error(any(), any(), any())).thenReturn(Constants::getEmptyValue);
    Mockito.when(LOGGER.warn(any(), any(), any()))
        .thenAnswer(i -> (Supplier) () -> i.getArgument(0));
    fall = new Fall();
    setFinalStatic(fall, Fall.class.getDeclaredField("LOGGER"), LOGGER);
  }

  @Test
  void testIdentifier() {
    // empty aufnahmenummer [NO LOGGING]
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
    assertEmptyCodeValue(
        LOGGER, fall::setAbteilungskontakt_klasse, fall::getAbteilungsEncounterClass);
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
    // empty fachabteilungsschluessel [NO LOGGING]
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
    Mockito.verify(LOGGER, Mockito.times(1)).warn(any(), any(), any());
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
    // empty ebene [NO LOGGING]
    assertEmptyCodeValue(fall::setAbteilungskontakt_ebene, fall::getAbteilungsEncounterType);
    // non-empty ebene
    String code = "1a2c";
    String display = "display";
    String ebene = getCodeDisplayStr(code, display);
    fall.setAbteilungskontakt_ebene(ebene);
    CodeableConcept result = fall.getAbteilungsEncounterType();
    assertCodeableConcept(code, CodingSystem.FALL_KONTAKTEBENE, display, result);
  }

  @Test
  void testPeriod() {
    // empty beginndatum
    fall.setAbteilungskontakt_beginndatum("");
    assertEmptyValue(fall.getAbteilungsEncounterPeriod());
    Mockito.verify(LOGGER, Mockito.times(1)).emptyValue(any(), any());
    // invalid beginndatum
    fall.setAbteilungskontakt_beginndatum("invalid");
    Period result = fall.getAbteilungsEncounterPeriod();
    assertPeriod(null, null, result);
    Mockito.verify(LOGGER, Mockito.times(1)).error(any(), any(), any());
    // valid beginndatum
    String startDate = "2021-02-20";
    fall.setAbteilungskontakt_beginndatum(startDate);
    result = fall.getAbteilungsEncounterPeriod();
    assertPeriod(expectedDateString(startDate), null, result);
    // valid enddatum
    String endDate = "2021-02-23";
    fall.setAbteilungskontakt_enddatum(endDate);
    result = fall.getAbteilungsEncounterPeriod();
    assertPeriod(expectedDateString(startDate), expectedDateString(endDate), result);
  }
}
