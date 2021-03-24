package model.person;

import constants.CodingCode;
import constants.CodingSystem;
import enums.VitalStatus;
import helper.Logger;
import model.Person;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Asserter.*;
import static util.Util.*;

class ObservationTest {
  private Person person;

  @BeforeEach
  public void setUp() throws Exception {
    Logger LOGGER = Mockito.mock(Logger.class);
    setUpLoggerMock(LOGGER);
    person = new Person();
    setMockLoggerField(person, LOGGER);
  }

  @Test
  void testValue() {
    // empty verstorben [NO LOGGING]
    person.setPatient_verstorben("");
    Coding result = person.getObservationValue();
    assertCoding(VitalStatus.UNBEKANNT, result);
    // valid verstorben
    person.setPatient_verstorben("1");
    result = person.getObservationValue();
    assertCoding(VitalStatus.VERSTORBEN, result);
  }

  @Test
  void testEffective() {
    // invalid zeitpunkt
    person.setLetzter_lebendzeitpunkt("2014-20-31");
    assertEmptyValue(person.getObservationEffective());
    // valid zeitpunkt
    String zeitpunkt = "2013-09-12";
    person.setLetzter_lebendzeitpunkt(zeitpunkt);
    DateTimeType result = person.getObservationEffective();
    assertDateTimeType(expectedDateString(zeitpunkt), result);
  }

  @Test
  void testCode() {
    // fixed values
    CodeableConcept result = person.getObservationCode();
    assertCodeableConcept(CodingCode.LOINC_OBSERVATION, CodingSystem.LOINC, null, result);
  }

  @Test
  void testCategory() {
    // fixed values
    CodeableConcept result = person.getObservationCategory();
    assertCodeableConcept(CodingCode.SURVEY, CodingSystem.OBSERVATION_CATEGORY, null, result);
  }

  @Test
  void testStatus() {
    // fixed value
    assertEquals(Observation.ObservationStatus.FINAL, person.getObservationStatus());
  }
}
