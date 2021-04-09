package model.medikation;

import enums.Behandlungsgrund;
import helper.Logger;
import model.Medikation;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Asserter.*;
import static util.Util.setMockLoggerField;
import static util.Util.setUpLoggerMock;

class MedicationAdministrationTest {
  private Medikation medikation;

  @BeforeEach
  public void setUp() throws Exception {
    Logger LOGGER = Mockito.mock(Logger.class);
    setUpLoggerMock(LOGGER);
    medikation = new Medikation();
    setMockLoggerField(medikation, LOGGER);
  }

  @Test
  void testRequest() {
    // empty verordnung
    medikation.setBezug_verordnung("");
    assertEmptyValue(medikation.getMedicationAdministrationRequest());
    // non-empty verordnung
    String verordnung = "some text";
    medikation.setBezug_verordnung(verordnung);
    Reference result = medikation.getMedicationAdministrationRequest();
    assertReference(verordnung, result);
  }

  @Test
  void testDosage() {
    // empty text
    medikation.setDosierung_freitext("");
    assertEmptyValue(medikation.getMedicationAdministrationDosage());
    // non-empty text
    String text = "some dosage text";
    medikation.setDosierung_freitext(text);
    MedicationAdministration.MedicationAdministrationDosageComponent result =
        medikation.getMedicationAdministrationDosage();
    assertNonEmptyValue(result);
    assertEquals(text, result.getText());
  }

  @Test
  void testIdentifier() {
    // empty id
    medikation.setIdentifikation("");
    assertEmptyValue(medikation.getMedicationAdministrationIdentifier());
    // non-empty id
    String id = "ExampleMedicationAdministration";
    medikation.setIdentifikation(id);
    Identifier result = medikation.getMedicationAdministrationIdentifier();
    assertIdentifier(id, null, result);
  }

  @Test
  void testStatus() {
    // invalid status [LOGGING]
    medikation.setStatus("");
    assertEmptyValue(medikation.getMedicationAdministrationStatus());
    medikation.setStatus("incorrect status");
    assertEmptyValue(medikation.getMedicationAdministrationStatus());
    // valid status
    String status = "abgeschlossen";
    medikation.setStatus(status);
    MedicationAdministration.MedicationAdministrationStatus result =
        medikation.getMedicationAdministrationStatus();
    assertEquals(MedicationAdministration.MedicationAdministrationStatus.COMPLETED, result);
  }

  @Test
  void testReasonCode() {
    // empty grund
    assertEmptyCodeValue(
        medikation::setBehandlungsgrund, medikation::getMedicationAdministrationReasonCode);
    // invalid grund [LOGGING]
    medikation.setBehandlungsgrund("invalid reason");
    assertEmptyValue(medikation.getMedicationAdministrationReasonCode());
    // valid grund
    medikation.setBehandlungsgrund("b");
    CodeableConcept result = medikation.getMedicationAdministrationReasonCode();
    assertCodeableConcept(Behandlungsgrund.GIVEN_AS_ORDERED, result);
  }
}
