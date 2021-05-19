package model.diagnose;

import constants.CodingSystem;
import constants.ExtensionUrl;
import enums.*;
import helper.Logger;
import model.Diagnose;
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

class ConditionTest {
  private static Logger LOGGER;
  private Diagnose diagnose;

  @BeforeAll
  static void init() {
    LOGGER = Mockito.mock(Logger.class, Mockito.CALLS_REAL_METHODS);
    setUpLoggerMock(LOGGER);
  }

  @BeforeEach
  public void setUp() throws Exception {
    clearInvocations(LOGGER);
    diagnose = new Diagnose();
    setMockLoggerField(diagnose, LOGGER);
  }

  @Test
  void testCodeIcdDiagnosesicherheit() {
    // empty diagnosesicherheit
    assertEmptyCodeValue(
        diagnose::setIcd_diagnosesicherheit, diagnose::getCodeIcdDiagnosesicherheit);
    // invalid diagnosesicherheit [LOGGING]
    diagnose.setIcd_diagnosesicherheit("invalid");
    assertEmptyValue(diagnose.getCodeIcdDiagnosesicherheit());
    // valid diagnosesicherheit
    Diagnosesicherheit diagnosesicherheitCode = Diagnosesicherheit.SUSPECTED;
    String diagnosesicherheit = getCodeDisplayStr(diagnosesicherheitCode);
    diagnose.setIcd_diagnosesicherheit(diagnosesicherheit);
    Extension result = diagnose.getCodeIcdDiagnosesicherheit();
    ICD_Diagnosesicherheit icdDiagnosesicherheitCode = diagnosesicherheitCode.getIcdMapping();
    assertExtensionWithCoding(
        icdDiagnosesicherheitCode, ExtensionUrl.ICD_10_GM_DIAGNOSESEICHERHEIT, result);
  }

  @Test
  void testCodeIcdSeitenlokalisation() {
    // empty seitenlokalisation
    assertEmptyCodeValue(
        diagnose::setIcd_seitenlokalisation, diagnose::getCodeIcdSeitenlokalisation);
    // invalid seitenlokalisation [LOGGING]
    diagnose.setIcd_seitenlokalisation("invalid");
    assertEmptyValue(diagnose.getCodeIcdSeitenlokalisation());
    // valid seitenlokalisation
    ICD_Seitenlokalisation icdSeitenlokalisation = ICD_Seitenlokalisation.BEIDSEITIG;
    String seitenlokalisation = getCodeDisplayStr(icdSeitenlokalisation);
    diagnose.setIcd_seitenlokalisation(seitenlokalisation);
    Extension result = diagnose.getCodeIcdSeitenlokalisation();
    assertExtensionWithCoding(
        icdSeitenlokalisation, ExtensionUrl.ICD_10_GM_SEITENLOKALISATION, result);
  }

  @Test
  void testCodeIcdAusrufezeichen() {
    // empty ausrufezeichen
    assertEmptyCodeValue(diagnose::setIcd_ausrufezeichencode, diagnose::getCodeIcdAusrufezeichen);
    // non-empty ausrufezeichen
    String code = "5h52d7";
    String display = "display";
    String ausrufezeichen = getCodeDisplayStr(code, display);
    diagnose.setIcd_ausrufezeichencode(ausrufezeichen);
    Extension result = diagnose.getCodeIcdAusrufezeichen();
    assertExtensionWithCoding(
        code, CodingSystem.ICD_10_GM_DIMDI, display, ExtensionUrl.ICD_10_GM_AUSRUFEZEICHEN, result);
  }

  @Test
  void testCodeIcdManifestationscode() {
    // empty manifestation
    assertEmptyCodeValue(
        diagnose::setIcd_manifestationscode, diagnose::getCodeIcdManifestationscode);
    // non-empty manifestation
    String code = "98052";
    String display = "mdisplay";
    String manifestation = getCodeDisplayStr(code, display);
    diagnose.setIcd_manifestationscode(manifestation);
    Extension result = diagnose.getCodeIcdManifestationscode();
    assertExtensionWithCoding(
        code,
        CodingSystem.ICD_10_GM_DIMDI,
        display,
        ExtensionUrl.ICD_10_GM_MANIFESTATIONSCODE,
        result);
  }

  @Test
  void testCodeIcdPrimaerCode() {
    // empty primaercode
    assertEmptyCodeValue(diagnose::setIcd_primaercode, diagnose::getCodeIcdPrimaercode);
    // non-empty primaercode
    String code = "3542";
    String display = "primaer display";
    String primaercode = getCodeDisplayStr(code, display);
    diagnose.setIcd_primaercode(primaercode);
    Extension result = diagnose.getCodeIcdPrimaercode();
    assertExtensionWithCoding(
        code, CodingSystem.ICD_10_GM_DIMDI, display, ExtensionUrl.ICD_10_GM_PRIMAERCODE, result);
  }

  @Test
  void testCodeIcd() {
    // empty diagnosecode
    assertEmptyCodeValue(diagnose::setIcd_diagnosecode, diagnose::getCodeIcd);
    // non-empty diagnosecode
    String code = "4325234";
    String display = "icd code";
    String diagnosecode = getCodeDisplayStr(code, display);
    diagnose.setIcd_diagnosecode(diagnosecode);
    Coding result = diagnose.getCodeIcd();
    assertCoding(code, CodingSystem.ICD_10_GM_DIMDI, display, result);
    assertFalse(result.hasExtension());
    // non-empty diagnosecode with extension
    diagnose.setIcd_diagnosecode(diagnosecode);
    String seitenlokalisation = getCodeDisplayStr(ICD_Seitenlokalisation.LINKS);
    diagnose.setIcd_seitenlokalisation(seitenlokalisation);
    result = diagnose.getCodeIcd();
    assertCoding(code, CodingSystem.ICD_10_GM_DIMDI, display, result);
    assertEquals(1, result.getExtension().size());
    assertTrue(result.hasExtension(ExtensionUrl.ICD_10_GM_SEITENLOKALISATION));
  }

  @Test
  void testCodeAlpha() {
    // empty diagnosecode
    assertEmptyCodeValue(diagnose::setAlpha_diagnosecode, diagnose::getCodeAlpha);
    // empty diagnosecode
    String code = "h34jk2";
    String display = "alpha display";
    String diagnosecode = getCodeDisplayStr(code, display);
    diagnose.setAlpha_diagnosecode(diagnosecode);
    Coding result = diagnose.getCodeAlpha();
    assertCoding(code, CodingSystem.ALPHA_ID_DIMDI, display, result);
  }

  @Test
  void testCodeSct() {
    // empty snomed
    assertEmptyCodeValue(diagnose::setSnomed_diagnosecode, diagnose::getCodeSct);
    // non-empty snomed
    String code = "5478923";
    String display = "snomed display";
    String snomed = getCodeDisplayStr(code, display);
    diagnose.setSnomed_diagnosecode(snomed);
    Coding result = diagnose.getCodeSct();
    assertCoding(code, CodingSystem.SNOMED_CLINICAL_TERMS, display, result);
  }

  @Test
  void testCodeOrphanet() {
    // empty orphanet
    assertEmptyCodeValue(diagnose::setOrphanet_diagnosecode, diagnose::getCodeOrphanet);
    // non-empty orphanet
    String code = "532k-24";
    String display = "orphanet display";
    String orphanet = getCodeDisplayStr(code, display);
    diagnose.setOrphanet_diagnosecode(orphanet);
    Coding result = diagnose.getCodeOrphanet();
    assertCoding(code, CodingSystem.ORPHANET, display, result);
  }

  @Test
  void testCodeWeitere() {
    // empty diagnosecode
    assertEmptyCodeValue(diagnose::setWeitere_diagnosecode, diagnose::getCodeWeitere);
    // non-empty diagnosecode
    String code = "43252";
    String display = "display";
    String system = "custom system";
    String diagnosecode = getCodeDisplayStr(code, display) + " system=\"" + system + "\"";
    diagnose.setWeitere_diagnosecode(diagnosecode);
    Coding result = diagnose.getCodeWeitere();
    assertCoding(code, system, display, result);
  }

  @Test
  void testCode() {
    // no codes set
    diagnose.setIcd_diagnosecode("");
    diagnose.setAlpha_diagnosecode("");
    diagnose.setSnomed_diagnosecode("");
    diagnose.setOrphanet_diagnosecode("");
    diagnose.setWeitere_diagnosecode("");
    assertEmptyValue(diagnose.getCode());
    // alpha and snomed set
    String alphaCode = "432421";
    String alphaDisplay = "alpha display";
    String alphaDiagnosecode = getCodeDisplayStr(alphaCode, alphaDisplay);
    diagnose.setAlpha_diagnosecode(alphaDiagnosecode);
    String snomedCode = "6904783";
    String snomedDisplay = "snomed display";
    String snomedDiagnosecode = getCodeDisplayStr(snomedCode, snomedDisplay);
    diagnose.setSnomed_diagnosecode(snomedDiagnosecode);
    String freitext = "some text here!";
    diagnose.setFreitextbeschreibung(freitext);
    CodeableConcept result = diagnose.getCode();
    assertNonEmptyValue(result);
    assertEquals(freitext, result.getText());
    List<Coding> codings = result.getCoding();
    assertEquals(2, codings.size());
    assertCoding(alphaCode, CodingSystem.ALPHA_ID_DIMDI, alphaDisplay, codings);
    assertCoding(snomedCode, CodingSystem.SNOMED_CLINICAL_TERMS, snomedDisplay, codings);
  }

  @Test
  void testNote() {
    // empty erlaeuterung
    diagnose.setDiagnoseerlaeuterung("");
    assertEmptyValue(diagnose.getNote());
    // non-empty erlaeuterung
    String erlaeuterung = "some text";
    diagnose.setDiagnoseerlaeuterung(erlaeuterung);
    Annotation result = diagnose.getNote();
    assertEquals(erlaeuterung, result.getText());
  }

  @Test
  void testRecordedDate() {
    // invalid datum
    diagnose.setDokumentationsdatum("");
    assertEmptyValue(diagnose.getRecordedDate());
    diagnose.setDokumentationsdatum("invalid");
    assertEmptyValue(diagnose.getRecordedDate());
    // valid datum
    String date = "2020-11-24";
    diagnose.setDokumentationsdatum(date);
    assertEquals(expectedDateString(date), diagnose.getRecordedDate());
  }

  @Test
  void testLebensphaseVon() {
    // empty von
    assertEmptyCodeValue(diagnose::setLebensphase_von, diagnose::getLebensphaseVon);
    // invalid von
    diagnose.setLebensphase_von("invalid");
    assertEmptyValue(diagnose.getLebensphaseVon());
    // valid von
    KBVBaseStageLife vonStageLife = KBVBaseStageLife.INFANCY;
    String vonCode = getCodeDisplayStr(vonStageLife);
    diagnose.setLebensphase_von(vonCode);
    Extension result = diagnose.getLebensphaseVon();
    assertExtensionWithCodeableConcept(vonStageLife, ExtensionUrl.STAGE_LIFE, result);
  }

  @Test
  void testLebensphaseBis() {
    // empty bis
    assertEmptyCodeValue(diagnose::setLebensphase_bis, diagnose::getLebensphaseBis);
    // invalid bis
    diagnose.setLebensphase_bis("invalid");
    assertEmptyValue(diagnose.getLebensphaseBis());
    // valid bis
    KBVBaseStageLife bisStageLife = KBVBaseStageLife.INFANCY;
    String bisCode = getCodeDisplayStr(bisStageLife);
    diagnose.setLebensphase_von(bisCode);
    Extension result = diagnose.getLebensphaseVon();
    assertExtensionWithCodeableConcept(bisStageLife, ExtensionUrl.STAGE_LIFE, result);
  }

  @Test
  void testOnset() {
    // empty values
    diagnose.setZeitraum_bis("");
    diagnose.setZeitraum_von("");
    diagnose.setLebensphase_von("");
    diagnose.setLebensphase_bis("");
    assertEmptyValue(diagnose.getOnset());
    // only lebensphase_von
    KBVBaseStageLife vonStageLife = KBVBaseStageLife.NEONATAL;
    String lebensphaseVon = getCodeDisplayStr(vonStageLife);
    diagnose.setLebensphase_von(lebensphaseVon);
    Type result = diagnose.getOnset();
    assertNonEmptyValue(result);
    assertTrue(result instanceof DateTimeType);
    assertFalse(((DateTimeType) result).hasValue());
    assertTrue(result.hasExtension(ExtensionUrl.STAGE_LIFE));
    assertEquals(1, result.getExtension().size());
    // only zeitraum_von and lebensphase_von is set
    String startDate = "2021-01-16";
    diagnose.setZeitraum_von(startDate);
    result = diagnose.getOnset();
    assertNonEmptyValue(result);
    assertTrue(result instanceof DateTimeType);
    assertEquals(expectedDateString(startDate), ((DateTimeType) result).getValue());
    assertTrue(result.hasExtension(ExtensionUrl.STAGE_LIFE));
    assertEquals(1, result.getExtension().size());
    // also zeitraum_bis is set
    String endDate = "2021-02-07";
    diagnose.setZeitraum_bis(endDate);
    result = diagnose.getOnset();
    assertNonEmptyValue(result);
    assertTrue(result instanceof Period);
    Period period = (Period) result;
    assertTrue(period.hasStart());
    assertEquals(expectedDateString(startDate), period.getStart());
    assertEquals(1, period.getStartElement().getExtension().size());
    assertTrue(period.getStartElement().hasExtension(ExtensionUrl.STAGE_LIFE));
    assertTrue(period.hasEnd());
    assertEquals(expectedDateString(endDate), period.getEnd());
    assertFalse(period.getEndElement().hasExtension());
  }

  @Test
  void testBodySite() {
    // empty koerperstelle
    assertEmptyCodeValue(diagnose::setKoerperstelle, diagnose::getBodySite);
    // non-empty koerperstelle
    String code = "4789231";
    String display = "somewhere on the body";
    String koerperstelle = getCodeDisplayStr(code, display);
    diagnose.setKoerperstelle(koerperstelle);
    CodeableConcept result = diagnose.getBodySite();
    assertCodeableConcept(code, CodingSystem.SNOMED_CLINICAL_TERMS, display, result);
  }

  @Test
  void testClinicalStatus() {
    // empty status
    assertEmptyCodeValue(diagnose::setKlinischer_status, diagnose::getClinicalStatus);
    // invalid status
    diagnose.setKlinischer_status("invalid status");
    assertEmptyValue(diagnose.getClinicalStatus());
    // valid status
    ClinicalStatus clinicalStatus = ClinicalStatus.ACTIVE;
    String status = getCodeDisplayStr(clinicalStatus);
    diagnose.setKlinischer_status(status);
    CodeableConcept result = diagnose.getClinicalStatus();
    assertCodeableConcept(clinicalStatus, result);
  }
}
