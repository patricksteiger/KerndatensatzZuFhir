package model.laborbefund;

import constants.*;
import enums.IdentifierTypeCode;
import helper.Logger;
import model.Laborbefund;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.Asserter.*;
import static util.Util.*;

class DiagnosticReportTest {
  private Laborbefund laborbefund;

  @BeforeEach
  public void setUp() throws Exception {
    Logger LOGGER = Mockito.mock(Logger.class);
    setUpLoggerMock(LOGGER);
    laborbefund = new Laborbefund();
    setMockLoggerField(laborbefund, LOGGER);
  }

  @Test
  void testSpecimen() {
    // empty id [NO LOGGING]
    laborbefund.setProbenmaterial_identifikation("");
    assertEmptyValue(laborbefund.getDiagnosticReportSpecimen());
    // non-empty id
    String id = "1234567890";
    laborbefund.setProbenmaterial_identifikation(id);
    Reference result = laborbefund.getDiagnosticReportSpecimen();
    assertReference(ReferenceType.SPECIMEN, id, IdentifierSystem.EMPTY, result);
  }

  @Test
  void testIssued() {
    // invalid datum
    laborbefund.setDokumentationsdatum("");
    assertEmptyValue(laborbefund.getDiagnosticReportIssued());
    // valid datum
    String date = "2015-07-19";
    laborbefund.setDokumentationsdatum(date);
    Date result = laborbefund.getDiagnosticReportIssued();
    assertEquals(expectedDateString(date), result);
  }

  @Test
  void testEffective() {
    // empty abnahme and eingang
    laborbefund.setProbenmaterial_abnahmezeitpunkt("");
    laborbefund.setProbenmaterial_laboreingangszeitpunkt("");
    assertEmptyValue(laborbefund.getDiagnosticReportEffective());
    // empty abnahme and non-empty eingang
    laborbefund.setProbenmaterial_abnahmezeitpunkt("");
    String eingang = "2020-11-13";
    laborbefund.setProbenmaterial_laboreingangszeitpunkt(eingang);
    DateTimeType result = laborbefund.getDiagnosticReportEffective();
    assertDateTimeType(expectedDateString(eingang), result);
    // valid abnahme overrides non-empty eingang
    laborbefund.setProbenmaterial_laboreingangszeitpunkt(eingang);
    String abnahme = "2021-02-26";
    laborbefund.setProbenmaterial_abnahmezeitpunkt(abnahme);
    result = laborbefund.getDiagnosticReportEffective();
    assertDateTimeType(expectedDateString(abnahme), result);
    // invalid abnahme overrides non-empty eingang
    laborbefund.setProbenmaterial_laboreingangszeitpunkt(eingang);
    laborbefund.setProbenmaterial_abnahmezeitpunkt("invalid date");
    assertEmptyValue(laborbefund.getDiagnosticReportEffective());
  }

  @Test
  void testLabReport() {
    // fixed values
    Coding result = laborbefund.getDiagnosticReportLabReport();
    assertCoding(CodingCode.LOINC_LAB_REPORT, CodingSystem.LOINC, result);
  }

  @Test
  void testCode() {
    // fixed values
    CodeableConcept result = laborbefund.getDiagnosticReportCode();
    assertCodeableConcept(CodingCode.LOINC_LAB_REPORT, CodingSystem.LOINC, result);
  }

  @Test
  void testStatus() {
    // invalid status
    laborbefund.setStatus(null);
    assertEmptyValue(laborbefund.getDiagnosticReportStatus());
    // valid status
    DiagnosticReport.DiagnosticReportStatus status = DiagnosticReport.DiagnosticReportStatus.FINAL;
    laborbefund.setStatus(getCodeStr(status.name()));
    assertEquals(status, laborbefund.getDiagnosticReportStatus());
  }

  @Test
  void testServiceSection() {
    // fixed values
    Coding result = laborbefund.getDiagnosticReportServiceSection();
    assertCoding(CodingCode.LAB_DIAGNOSTIC_REPORT, CodingSystem.DIAGNOSTIC_SERVICE_SECTION, result);
  }

  @Test
  void testLoincLab() {
    // fixed values
    Coding result = laborbefund.getDiagnosticReportLoincLab();
    assertCoding(CodingCode.LOINC_LAB, CodingSystem.LOINC, result);
  }

  @Test
  void testCategory() {
    // fixed values
    CodeableConcept result = laborbefund.getDiagnosticReportCategory();
    assertNonEmptyValue(result);
    List<Coding> codings = result.getCoding();
    assertEquals(2, codings.size());
    assertCoding(CodingCode.LOINC_LAB, CodingSystem.LOINC, codings.get(0));
    assertCoding(
        CodingCode.LAB_DIAGNOSTIC_REPORT, CodingSystem.DIAGNOSTIC_SERVICE_SECTION, codings.get(1));
  }

  @Test
  void testBefund() {
    // empty id
    laborbefund.setIdentifikation("");
    assertEmptyValue(laborbefund.getDiagnosticReportBefund());
    // non-empty id
    String id = "0987654321";
    laborbefund.setIdentifikation(id);
    Identifier result = laborbefund.getDiagnosticReportBefund();
    assertIdentifier(id, IdentifierSystem.EMPTY, IdentifierTypeCode.FILL, result);
    assertTrue(result.hasAssigner());
    assertEquals(MIIReference.MII_ORGANIZATION, result.getAssigner().getReference());
  }
}
