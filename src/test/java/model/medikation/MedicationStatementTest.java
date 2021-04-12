package model.medikation;

import helper.Logger;
import model.Medikation;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

class MedicationStatementTest {
  private static Logger LOGGER;
  private Medikation medikation;

  @BeforeAll
  static void init() {
    LOGGER = Mockito.mock(Logger.class, Mockito.CALLS_REAL_METHODS);
    setUpLoggerMock(LOGGER);
  }

  @BeforeEach
  public void setUp() throws Exception {
    clearInvocations(LOGGER);
    medikation = new Medikation();
    setMockLoggerField(medikation, LOGGER);
  }

  @Test
  void testInformationSource() {
    // empty name
    medikation.setOrganisationsname("");
    assertEmptyValue(medikation.getMedicationStatementInformationSource());
    // non-empty name
    String name = "organisation";
    medikation.setOrganisationsname(name);
    Reference result = medikation.getMedicationStatementInformationSource();
    assertReference(name, result);
  }

  @Test
  void testDateAsserted() {
    // empty datum
    medikation.setDatum_eintrag("");
    assertEmptyValue(medikation.getMedicationStatementDateAsserted());
    // invalid datum [LOGGING]
    medikation.setDatum_eintrag("2018-13-13");
    assertEmptyValue(medikation.getMedicationStatementDateAsserted());
    // valid datum
    String date = "2020-04-18";
    medikation.setDatum_eintrag(date);
    Date result = medikation.getMedicationStatementDateAsserted();
    assertEquals(expectedDateString(date), result);
  }

  @Test
  void testNote() {
    // empty hinweis
    medikation.setHinweis("");
    assertEmptyValue(medikation.getMedicationStatementNote());
    // non-empty hinweis
    String hinweis = "some notification";
    medikation.setHinweis(hinweis);
    Annotation result = medikation.getMedicationStatementNote();
    assertAnnotation(hinweis, result);
  }

  @Test
  void testDosage() {
    // empty text
    medikation.setDosierung_freitext("");
    assertEmptyValue(medikation.getMedicationStatementDosage());
    // non-empty text
    String text = "dosage text";
    medikation.setDosierung_freitext(text);
    Dosage result = medikation.getMedicationStatementDosage();
    assertNonEmptyValue(result);
    assertEquals(text, result.getText());
  }

  @Test
  void testEffective() {
    // invalid start [LOGGING]
    medikation.setEinnahme_startzeitpunkt("2018-01-50");
    assertEmptyValue(medikation.getMedicationStatementEffective());
    // valid start, empty end
    String start = "2004-11-23";
    medikation.setEinnahme_startzeitpunkt(start);
    medikation.setEinnahme_endzeitpunkt("");
    Type result = medikation.getMedicationStatementEffective();
    assertNonEmptyValue(result);
    assertTrue(result instanceof DateTimeType);
    DateTimeType resultDate = (DateTimeType) result;
    assertDateTimeType(expectedDateString(start), resultDate);
    // valid start, but invalid end overwrites [LOGGING]
    medikation.setEinnahme_startzeitpunkt(start);
    medikation.setEinnahme_endzeitpunkt("2016-13-40");
    assertEmptyValue(medikation.getMedicationStatementEffective());
    // valid start and end
    medikation.setEinnahme_startzeitpunkt(start);
    String end = "2005-03-20";
    medikation.setEinnahme_endzeitpunkt(end);
    result = medikation.getMedicationStatementEffective();
    assertNonEmptyValue(result);
    assertTrue(result instanceof Period);
    Period resultPeriod = (Period) result;
    assertPeriod(expectedDateString(start), expectedDateString(end), resultPeriod);
  }

  @Test
  void testBasedOn() {
    // empty verordnung
    medikation.setBezug_verordnung("");
    assertEmptyValue(medikation.getMedicationStatementBasedOn());
    // non-empty verordnung
    String verordnung = "some rules";
    medikation.setBezug_verordnung(verordnung);
    Reference result = medikation.getMedicationStatementBasedOn();
    assertReference(verordnung, result);
  }

  @Test
  void testStatus() {
    // invalid status [LOGGING]
    medikation.setStatus("");
    assertEmptyValue(medikation.getMedicationStatementStatus());
    medikation.setStatus("invalid status");
    assertEmptyValue(medikation.getMedicationStatementStatus());
    // valid status
    medikation.setStatus("aktiv");
    MedicationStatement.MedicationStatementStatus result =
        medikation.getMedicationStatementStatus();
    assertEquals(MedicationStatement.MedicationStatementStatus.ACTIVE, result);
  }

  @Test
  void testIdentifier() {
    // empty id
    medikation.setIdentifikation("");
    assertEmptyValue(medikation.getMedicationStatementIdentifier());
    // non-empty id
    String id = "ExampleMedicationThiotepa";
    medikation.setIdentifikation(id);
    Identifier result = medikation.getMedicationStatementIdentifier();
    assertIdentifier(id, null, result);
  }

  @Test
  void testPartOf() {
    // empty abgabe
    medikation.setBezug_abgabe("");
    assertEmptyValue(medikation.getMedicationStatementPartOf());
    // non-empty abgabe
    String abgabe = "some text";
    medikation.setBezug_abgabe(abgabe);
    Reference result = medikation.getMedicationStatementPartOf();
    assertReference(abgabe, result);
  }
}
