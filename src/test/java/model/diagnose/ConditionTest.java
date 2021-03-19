package model.diagnose;

import constants.CodingSystem;
import constants.ExtensionUrl;
import enums.Diagnosesicherheit;
import enums.ICD_Diagnosesicherheit;
import enums.ICD_Seitenlokalisation;
import helper.Logger;
import model.Diagnose;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static util.Asserter.*;
import static util.Util.*;

class ConditionTest {
  private Diagnose diagnose;
  private Logger LOGGER;

  @BeforeEach
  public void setUp() throws Exception {
    LOGGER = Mockito.mock(Logger.class);
    setUpLoggerMock(LOGGER);
    diagnose = new Diagnose();
    setMockLoggerField(diagnose, LOGGER);
  }

  @Test
  void testCodeIcdDiagnosesicherheit() {
    // empty diagnosesicherheit [NO LOGGING]
    assertEmptyCodeValue(
        diagnose::setIcd_diagnosesicherheit, diagnose::getCodeIcdDiagnosesicherheit);
    // invalid diagnosesicherheit
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
    // empty seitenlokalisation [NO LOGGING]
    assertEmptyCodeValue(
        diagnose::setIcd_seitenlokalisation, diagnose::getCodeIcdSeitenlokalisation);
    // invalid seitenlokalisation
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
    // empty ausrufezeichen [NO LOGGING]
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
    // empty manifestation [NO LOGGING]
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
    // empty primaercode [NO LOGGING]
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
    // empty diagnosecode [NO LOGGING]
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
    // empty diagnosecode [NO LOGGING]
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
    // empty snomed [NO LOGGING]
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
    // empty orphanet [NO LOGGING]
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
    // empty diagnosecode [No LOGGING]
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
}
