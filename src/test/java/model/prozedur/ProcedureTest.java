package model.prozedur;

import constants.CodingSystem;
import constants.ExtensionUrl;
import enums.DurchfuehrungsabsichtCode;
import enums.ProcedureCategorySnomedMapping;
import enums.SeitenlokalisationCode;
import helper.Logger;
import model.Prozedur;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

class ProcedureTest {
  private static Logger LOGGER;
  private Prozedur prozedur;

  @BeforeAll
  static void init() {
    LOGGER = Mockito.mock(Logger.class, Mockito.CALLS_REAL_METHODS);
    setUpLoggerMock(LOGGER);
  }

  @BeforeEach
  public void setUp() throws Exception {
    clearInvocations(LOGGER);
    prozedur = new Prozedur();
    setMockLoggerField(prozedur, LOGGER);
  }

  @Test
  void testStatus() {
    assertEquals(Procedure.ProcedureStatus.COMPLETED, prozedur.getStatus());
  }

  @Test
  void testCategory() {
    // empty ops [NO LOGGING]
    assertEmptyCodeValue(prozedur::setOPS_Vollst_Prozedurenkode, prozedur::getCategory);
    // invalid mapping
    String code = getCodeStr("invalid");
    prozedur.setOPS_Vollst_Prozedurenkode(code);
    assertEmptyValue(prozedur.getCategory());
    // valid mapping
    code = getCodeStr("5-526.21");
    prozedur.setOPS_Vollst_Prozedurenkode(code);
    ProcedureCategorySnomedMapping expectedMapping = ProcedureCategorySnomedMapping.SURGICAL;
    CodeableConcept result = prozedur.getCategory();
    assertCodeableConcept(expectedMapping, result);
  }

  @Test
  void testSeitenlokalisation() {
    // empty seitenlokalisation [NO LOGGING]
    assertEmptyCodeValue(prozedur::setOPS_Seitenlokalisation, prozedur::getSeitenlokalisation);
    // invalid Seitenlokalisation
    prozedur.setOPS_Seitenlokalisation("invalid");
    assertEmptyValue(prozedur.getSeitenlokalisation());
    // valid Seitenlokalisation
    SeitenlokalisationCode seitenlokalisationCode = SeitenlokalisationCode.LINKS;
    String seitenlokalisation = getCodeDisplayStr(seitenlokalisationCode);
    prozedur.setOPS_Seitenlokalisation(seitenlokalisation);
    Extension result = prozedur.getSeitenlokalisation();
    assertExtensionWithCoding(seitenlokalisationCode, ExtensionUrl.OPS_SEITENLOKALISATION, result);
  }

  @Test
  void testCodingOps() {
    // empty ops [NO LOGGING]
    assertEmptyCodeValue(prozedur::setOPS_Vollst_Prozedurenkode, prozedur::getCodingOps);
    // non-empty ops
    String code = "5-526.21";
    String display = "ops display";
    String opsCode = getCodeDisplayStr(code, display);
    prozedur.setOPS_Vollst_Prozedurenkode(opsCode);
    Coding result = prozedur.getCodingOps();
    assertCoding(code, CodingSystem.OPS_DIMDI, display, result);
    assertFalse(result.hasExtension());
    // add seitenlokalisation
    SeitenlokalisationCode seitenlokalisationCode = SeitenlokalisationCode.RECHTS;
    String seitenlokalisation = getCodeDisplayStr(seitenlokalisationCode);
    prozedur.setOPS_Seitenlokalisation(seitenlokalisation);
    result = prozedur.getCodingOps();
    assertEquals(1, result.getExtension().size());
  }

  @Test
  void testCodingSnomed() {
    // empty snomed [NO LOGGING]
    assertEmptyCodeValue(prozedur::setSNOMED_Vollst_Prozedurenkode, prozedur::getCodingSnomed);
    // non-empty snomed
    String code = "53124423";
    String display = "snomed display";
    String snomed = getCodeDisplayStr(code, display);
    prozedur.setSNOMED_Vollst_Prozedurenkode(snomed);
    Coding result = prozedur.getCodingSnomed();
    assertCoding(code, CodingSystem.SNOMED_CLINICAL_TERMS, display, result);
  }

  @Test
  void testCode() {
    // empty snomed and ops
    prozedur.setOPS_Vollst_Prozedurenkode("");
    prozedur.setSNOMED_Vollst_Prozedurenkode("");
    assertEmptyValue(prozedur.getCode());
    // non-empty snomed and ops
    String opsCode = "5-526.21";
    prozedur.setOPS_Vollst_Prozedurenkode(opsCode);
    String snomedCode = "467823";
    prozedur.setSNOMED_Vollst_Prozedurenkode(snomedCode);
    CodeableConcept result = prozedur.getCode();
    assertNonEmptyValue(result);
    List<Coding> codings = result.getCoding();
    assertEquals(2, codings.size());
    assertCoding(opsCode, CodingSystem.OPS_DIMDI, null, codings);
    assertCoding(snomedCode, CodingSystem.SNOMED_CLINICAL_TERMS, null, codings);
  }

  @Test
  void testPerformed() {
    // invalid durchfuehrungsdatum
    prozedur.setDurchfuehrungsdatum("invalid");
    assertEmptyValue(prozedur.getPerformed());
    // valid durchfuehrungsdatum
    String date = "2020-12-12";
    prozedur.setDurchfuehrungsdatum(date);
    DateTimeType result = prozedur.getPerformed();
    assertDateTimeType(expectedDateString(date), result);
  }

  @Test
  void testBodySite() {
    // empty koerperstelle [NO LOGGING]
    assertEmptyCodeValue(prozedur::setKoerperstelle, prozedur::getBodySite);
    // non-empty koerperstelle
    String code = "478923";
    String display = "koerper";
    String koerperstelle = getCodeDisplayStr(code, display);
    prozedur.setKoerperstelle(koerperstelle);
    CodeableConcept result = prozedur.getBodySite();
    assertCodeableConcept(code, CodingSystem.SNOMED_CLINICAL_TERMS, display, result);
  }

  @Test
  void testNote() {
    // empty freitext [NO LOGGING]
    prozedur.setFreitextbeschreibung("");
    assertEmptyValue(prozedur.getNote());
    // non-empty freitext
    String freitext = "text";
    prozedur.setFreitextbeschreibung(freitext);
    Annotation result = prozedur.getNote();
    assertNonEmptyValue(result);
    assertEquals(freitext, result.getText());
  }

  @Test
  void testRecordedDate() {
    // empty dokumentationsdatum [NO LOGGING]
    prozedur.setDokumentationsdatum("");
    assertEmptyValue(prozedur.getRecordedDate());
    // invalid dokumentationsdatum
    prozedur.setDokumentationsdatum("invalid");
    assertEmptyValue(prozedur.getRecordedDate());
    // valid dokumentationsdatum
    String date = "2021-01-14";
    prozedur.setDokumentationsdatum(date);
    prozedur.setDurchfuehrungsdatum("");
    Extension result = prozedur.getRecordedDate();
    assertNonEmptyValue(result);
    assertEquals(ExtensionUrl.RECORDED_DATE, result.getUrl());
    assertTrue(result.hasValue());
    assertTrue(result.getValue() instanceof DateTimeType);
    assertDateTimeType(expectedDateString(date), (DateTimeType) result.getValue());
    // dokumentationsdatum = durchfuehrungsdatum [NO LOGGING]
    prozedur.setDokumentationsdatum(date);
    prozedur.setDurchfuehrungsdatum(date);
    assertEmptyValue(prozedur.getRecordedDate());
  }

  @Test
  void testDurchfuehrungsabsicht() {
    // empty durchfuehrungsabsicht [NO LOGGING]
    assertEmptyCodeValue(prozedur::setDurchfuehrungsabsicht, prozedur::getDurchfuehrungsabsicht);
    // invalid durchfuehrungsabsicht
    prozedur.setDurchfuehrungsabsicht("invalid");
    assertEmptyValue(prozedur.getDurchfuehrungsabsicht());
    // valid durchfuehrungsabsicht
    DurchfuehrungsabsichtCode durchfuehrungsabsichtCode = DurchfuehrungsabsichtCode.OTHER;
    String durchfuehrungsabsicht = getCodeDisplayStr(durchfuehrungsabsichtCode);
    prozedur.setDurchfuehrungsabsicht(durchfuehrungsabsicht);
    Extension result = prozedur.getDurchfuehrungsabsicht();
    assertExtensionWithCoding(
        durchfuehrungsabsichtCode, ExtensionUrl.DURCHFUEHRUNGSABSICHT, result);
  }
}
