package model.person;

import constants.IdentifierSystem;
import enums.IdentifierTypeCode;
import helper.Logger;
import model.Person;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.ResearchSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Asserter.*;
import static util.Util.*;

class ResearchSubjectTest {
  private Person person;

  @BeforeEach
  public void setUp() throws Exception {
    Logger LOGGER = Mockito.mock(Logger.class, Mockito.CALLS_REAL_METHODS);
    setUpLoggerMock(LOGGER);
    person = new Person();
    setMockLoggerField(person, LOGGER);
  }

  @Test
  void testPeriod() {
    // invalid beginn
    person.setTeilnahme_beginn("");
    assertEmptyValue(person.getResearchSubjectPeriod());
    // valid beginn and empty ende
    String beginn = "2020-11-13";
    person.setTeilnahme_beginn(beginn);
    person.setTeilnahme_ende("");
    Period result = person.getResearchSubjectPeriod();
    assertPeriod(expectedDateString(beginn), null, result);
    // valid beinn and invalid ende
    person.setTeilnahme_beginn(beginn);
    person.setTeilnahme_ende("invalid date");
    assertEmptyValue(person.getResearchSubjectPeriod());
    // valid beginn and valid ende
    String ende = "2020-11-15";
    person.setTeilnahme_beginn(beginn);
    person.setTeilnahme_ende(ende);
    result = person.getResearchSubjectPeriod();
    assertPeriod(expectedDateString(beginn), expectedDateString(ende), result);
  }

  @Test
  void testStatus() {
    // invalid status
    assertEmptyCodeValue(person::setTeilnahme_status, person::getResearchSubjectStatus);
    // valid status
    String status = "candidate";
    person.setTeilnahme_status(status);
    ResearchSubject.ResearchSubjectStatus result = person.getResearchSubjectStatus();
    assertEquals(ResearchSubject.ResearchSubjectStatus.CANDIDATE, result);
  }

  @Test
  void testSubjectIdentificationCode() {
    // empty code
    assertEmptyCodeValue(
        person::setSubjekt_identifizierungscode,
        person::getResearchSubjectSubjectIdentificationCode);
    // non-empty code
    String subjektIdentifikationsCode = "12345";
    String code = getCodeStr(subjektIdentifikationsCode);
    person.setSubjekt_identifizierungscode(code);
    Identifier result = person.getResearchSubjectSubjectIdentificationCode();
    assertIdentifier(
        subjektIdentifikationsCode,
        IdentifierSystem.SUBJECT_IDENTIFICATION_CODE,
        IdentifierTypeCode.ANON,
        result);
  }
}
