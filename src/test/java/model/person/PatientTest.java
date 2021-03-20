package model.person;

import constants.IdentifierSystem;
import enums.IdentifierTypeCode;
import enums.MIICoreLocations;
import helper.Logger;
import model.Person;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static util.Asserter.assertEmptyValue;
import static util.Asserter.assertIdentifier;
import static util.Util.setMockLoggerField;
import static util.Util.setUpLoggerMock;

class PatientTest {
  private Person person;
  private Logger LOGGER;

  @BeforeEach
  public void setUp() throws Exception {
    LOGGER = Mockito.mock(Logger.class);
    setUpLoggerMock(LOGGER);
    person = new Person();
    setMockLoggerField(person, LOGGER);
  }

  @Test
  void testPID() {
    // empty pid [NO LOGGING]
    person.setPatient_pid("");
    assertEmptyValue(person.getPatientPID());
    // non-empty pid
    String pid = "42285243";
    person.setPatient_pid(pid);
    Identifier pidIdentifier = person.getPatientPID();
    assertIdentifier(pid, IdentifierSystem.PID, IdentifierTypeCode.MR, pidIdentifier);
    assertTrue(pidIdentifier.hasAssigner());
    Reference assigner = pidIdentifier.getAssigner();
    assertEquals(MIICoreLocations.UKU.toString(), assigner.getDisplay());
    assertTrue(assigner.hasIdentifier());
    assertEquals(MIICoreLocations.UKU.name(), assigner.getIdentifier().getValue());
    assertEquals(IdentifierSystem.CORE_LOCATIONS, assigner.getIdentifier().getSystem());
  }

  @Test
  void testOrganizationReference() {
    fail("Not implemented!");
  }
}
