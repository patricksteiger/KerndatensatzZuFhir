package model.laborbefund;

import constants.IdentifierSystem;
import enums.IdentifierTypeCode;
import helper.Logger;
import model.Laborbefund;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

class ServiceRequestTest {
  private static Logger LOGGER;
  private Laborbefund laborbefund;

  @BeforeAll
  static void init() {
    LOGGER = Mockito.mock(Logger.class, Mockito.CALLS_REAL_METHODS);
    setUpLoggerMock(LOGGER);
  }

  @BeforeEach
  public void setUp() throws Exception {
    clearInvocations(LOGGER);
    laborbefund = new Laborbefund();
    setMockLoggerField(laborbefund, LOGGER);
  }

  @Test
  void testIdentifier() {
    // empty id
    laborbefund.setIdentifikation("");
    assertEmptyValue(laborbefund.getServiceRequestIdentifier());
    // non-empty id
    String id = "1234567890";
    laborbefund.setIdentifikation(id);
    Identifier result = laborbefund.getServiceRequestIdentifier();
    assertIdentifier(id, IdentifierSystem.EMPTY, IdentifierTypeCode.PLAC, result);
  }

  @Test
  void testSpecimen() {
    // fixed value
    assertEmptyValue(laborbefund.getServiceRequestSpecimen());
  }

  @Test
  void testAuthoredOn() {
    // invalid date
    laborbefund.setLaboranforderung_anforderungsdatum("1997-12-34");
    assertEmptyValue(laborbefund.getServiceRequestAuthoredOn());
    // valid date
    String date = "2007-12-01";
    laborbefund.setLaboranforderung_anforderungsdatum(date);
    Date result = laborbefund.getServiceRequestAuthoredOn();
    assertEquals(expectedDateString(date), result);
  }

  @Test
  void testCodeText() {
    // empty text
    laborbefund.setLaboranforderung_laborparameter_bezeichnung("");
    assertEmptyValue(laborbefund.getServiceRequestCodeText());
    // non-empty text
    String text = "code text";
    laborbefund.setLaboranforderung_laborparameter_bezeichnung(text);
    assertEquals(text, laborbefund.getServiceRequestCodeText());
  }

  @Test
  void testCode() {
    // empty code [LOGGING]
    assertEmptyCodeValue(
        laborbefund::setLaboranforderung_laborparameter_code, laborbefund::getServiceRequestCode);
    // non-empty, with text
    String code = "GroßesBlutbild",
        system = "http://diz.mii.de/fhir/CodeSystem/LabTests",
        display = "großes Blutbild";
    String labor = getCodeDisplaySystemStr(code, display, system);
    laborbefund.setLaboranforderung_laborparameter_code(labor);
    String text = "some text";
    laborbefund.setLaboranforderung_laborparameter_bezeichnung(text);
    CodeableConcept result = laborbefund.getServiceRequestCode();
    assertCodeableConcept(code, system, display, result);
    assertEquals(text, result.getText());
  }

  @Test
  void testCategory() {
    // fixed value
    CodeableConcept result = laborbefund.getServiceRequestCategory();
    assertCodeableConcept(
        "laboratory", "http://terminology.hl7.org/CodeSystem/observation-category", null, result);
  }

  @Test
  void testIntent() {
    // fixed value
    assertEquals(ServiceRequest.ServiceRequestIntent.ORDER, laborbefund.getServiceRequestIntent());
  }

  @Test
  void testStatus() {
    // fixed value
    assertEquals(
        ServiceRequest.ServiceRequestStatus.COMPLETED, laborbefund.getServiceRequestStatus());
  }
}
