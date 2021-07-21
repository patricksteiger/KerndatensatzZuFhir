package endToEnd;

import basismodule.Diagnose;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import parser.CsvParser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.Asserter.*;
import static util.Util.*;

public class DiagnoseTest {
  private static final String FILE_PATH = "src/test/resources/endToEnd/Diagnose.csv";
  private static Condition condition;

  @BeforeAll
  static void setUpFile() throws IOException {
    FileWriter fileWriter = new FileWriter(FILE_PATH);
    // Column headers
    writeColumnValueWithSeparator(fileWriter, "icd_diagnosecode");
    writeColumnValueWithSeparator(fileWriter, "snomed_diagnosecode");
    writeColumnValueWithSeparator(fileWriter, "freitextbeschreibung");
    writeColumnValueWithSeparator(fileWriter, "zeitraum_von");
    writeColumnValueWithSeparator(fileWriter, "zeitraum_bis");
    writeColumnValueWithSeparator(fileWriter, "dokumentationsdatum");
    fileWriter.write("klinischer_status");
    fileWriter.write('\n');
    // Column values
    String icd =
        getCodeDisplaySystemStr(
            "550.0", "Prellung des Ellenbogens", "http://fhir.de/CodeSystem/dimdi/icd-10-gm");
    writeColumnValueWithSeparator(fileWriter, icd);
    String snomed =
        getCodeDisplaySystemStr(
            "91613004", "Contusion of elbow (disorder)", "http://snomed.info/sct");
    writeColumnValueWithSeparator(fileWriter, snomed);
    String freitext = "Prellung des linken Ellenbogens";
    writeColumnValueWithSeparator(fileWriter, freitext);
    String von = "2020-02-26T12:00:00+01:00";
    writeColumnValueWithSeparator(fileWriter, von);
    String bis = "2020-03-05T13:00:00+01:00";
    writeColumnValueWithSeparator(fileWriter, bis);
    String dokuDatum = "2020-02-26T12:00:00+01:00";
    writeColumnValueWithSeparator(fileWriter, dokuDatum);
    String status =
        getCodeSystemStr("active", "http://terminology.hl7.org/CodeSystem/condition-clinical");
    fileWriter.write(status);
    fileWriter.write('\n');
    fileWriter.close();
    List<Diagnose> parseResult = CsvParser.parseDatablocks(FILE_PATH, Diagnose.class);
    assertNonEmptyValue(parseResult);
    assertEquals(1, parseResult.size());
    Diagnose diagnose = parseResult.get(0);
    List<Resource> resources = diagnose.toFhirResources();
    assertNonEmptyValue(resources);
    assertEquals(1, resources.size());
    assertTrue(resources.get(0) instanceof Condition);
    condition = (Condition) resources.get(0);
  }

  public static void writeColumnValueWithSeparator(FileWriter fileWriter, String value)
      throws IOException {
    fileWriter.write(value);
    fileWriter.write(CsvParser.COLUMN_SEPARATOR);
  }

  @Test
  void testRecordedDate() {
    String date = "2020-02-26T12:00:00";
    assertEquals(expectedDateString(date), condition.getRecordedDate());
  }

  @Test
  void testOnset() {
    Type onset = condition.getOnset();
    assertNonEmptyValue(onset);
    assertTrue(onset instanceof Period);
    Period result = (Period) onset;
    Date start = expectedDateString("2020-02-26T12:00:00");
    Date end = expectedDateString("2020-03-05T13:00:00");
    assertPeriod(start, end, result);
  }

  @Test
  void testClinicalStatus() {
    CodeableConcept result = condition.getClinicalStatus();
    String code = "active";
    String system = "http://terminology.hl7.org/CodeSystem/condition-clinical";
    String display = "Active";
    assertCodeableConcept(code, system, display, result);
  }

  @Test
  void testCode() {
    CodeableConcept result = condition.getCode();
    assertNonEmptyValue(result);
    assertEquals("Prellung des linken Ellenbogens", result.getText());
    assertEquals(2, result.getCoding().size());
    String icdCode = "550.0",
        icdSystem = "http://fhir.de/CodeSystem/bfarm/icd-10-gm",
        icdDisplay = "Prellung des Ellenbogens";
    assertCoding(icdCode, icdSystem, icdDisplay, result.getCoding());
    String snomedCode = "91613004",
        snomedSystem = "http://snomed.info/sct",
        snomedDisplay = "Contusion of elbow (disorder)";
    assertCoding(snomedCode, snomedSystem, snomedDisplay, result.getCoding());
  }
}
