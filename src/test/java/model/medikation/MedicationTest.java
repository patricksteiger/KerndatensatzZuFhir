package model.medikation;

import constants.CodingSystem;
import constants.ExtensionUrl;
import enums.Wirkstofftyp;
import helper.Logger;
import model.Medikation;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

class MedicationTest {
  private static Logger LOGGER;
  private Medikation medikation;

  @BeforeAll
  static void init() {
    LOGGER = Mockito.mock(Logger.class, Mockito.CALLS_REAL_METHODS);
    setUpLoggerMock(LOGGER);
  }

  @BeforeEach
  public void setUp() throws Exception {
    clearInvocations(LOGGER);
    medikation = new Medikation();
    setMockLoggerField(medikation, LOGGER);
  }

  @Test
  void testIngredientExtension() {
    // empty code
    assertEmptyCodeValue(
        medikation::setWirkstoff_code_allgemein, medikation::getMedicationIngredientExtension);
    // invalid code [LOGGING]
    medikation.setWirkstoff_code_allgemein(getCodeStr("invalid wirkstoff code"));
    assertEmptyValue(medikation.getMedicationIngredientExtension());
    // valid code
    medikation.setWirkstoff_code_allgemein(getCodeStr("MIN"));
    Extension result = medikation.getMedicationIngredientExtension();
    assertExtensionWithCoding(
        Wirkstofftyp.KOMBINATION, ExtensionUrl.MEDIKATION_WIRKSTOFFTYP, result);
  }

  @Test
  void testIngredientItem() {
    // empty code [LOGGING]
    assertEmptyCodeValue(
        medikation::setWirkstoff_code_aktiv, medikation::getMedicationIngredientItem);
    // non-empty code and display taken from wirkstoff_name_aktiv
    String code = "00002", system = "http://fhir.de/CodeSystem/ask", display = "Acetylsalicylsäure";
    medikation.setWirkstoff_code_aktiv(getCodeSystemStr(code, system));
    medikation.setWirkstoff_name_aktiv(display);
    CodeableConcept result = medikation.getMedicationIngredientItem();
    assertCodeableConcept(code, system, display, result);
    // non-empty code and directly given display overwrites display from wirkstoff_name_aktiv
    medikation.setWirkstoff_code_aktiv(getCodeDisplaySystemStr(code, display, system));
    medikation.setWirkstoff_name_aktiv("ignored display");
    result = medikation.getMedicationIngredientItem();
    assertCodeableConcept(code, system, display, result);
  }

  @Test
  void testIngredientStrength() {
    // empty menge
    medikation.setWirkstoff_menge("");
    assertEmptyValue(medikation.getMedicationIngredientStrength());
    // invalid menge [LOGGING]
    medikation.setWirkstoff_menge(getValueUnitStr("12/0", "mmol/L"));
    assertEmptyValue(medikation.getMedicationIngredientStrength());
    // valid menge
    String value = "3/4", unit = "mmol/L";
    medikation.setWirkstoff_menge(getValueUnitStr(value, unit));
    Ratio result = medikation.getMedicationIngredientStrength();
    assertRatio(new BigDecimal("3"), "mmol", new BigDecimal("4"), "L", result);
  }

  @Test
  void testIngredient() {
    // invalid itemCode [LOGGING]
    medikation.setWirkstoff_code_aktiv("");
    assertEmptyValue(medikation.getMedicationIngredient());
    // valid itemCode
    medikation.setWirkstoff_code_aktiv("00002");
    Medication.MedicationIngredientComponent result = medikation.getMedicationIngredient();
    assertNonEmptyValue(result);
    assertTrue(result.hasItem());
  }

  @Test
  void testAmount() {
    // empty wirkstaerke
    medikation.setArzneimittel_wirkstaerke("");
    assertEmptyValue(medikation.getMedicationAmount());
    // invalid wirksaerke
    medikation.setArzneimittel_wirkstaerke(getValueUnitStr("", "mmol/L"));
    assertEmptyValue(medikation.getMedicationAmount());
    // valid wirkstaerke
    String value = "5/7", unit = "mmol/L";
    medikation.setArzneimittel_wirkstaerke(getValueUnitStr(value, unit));
    Ratio result = medikation.getMedicationAmount();
    assertRatio(new BigDecimal("5"), "mmol", new BigDecimal("7"), "L", result);
  }

  @Test
  void testForm() {
    // empty form
    assertEmptyCodeValue(medikation::setDarreichungsform, medikation::getMedicationForm);
    // invalid form
    medikation.setDarreichungsform("invalid code");
    assertEmptyValue(medikation.getMedicationForm());
    // non-empty form
    String code = "10219000", display = "Tablet";
    medikation.setDarreichungsform(getCodeDisplayStr(code, display));
    CodeableConcept result = medikation.getMedicationForm();
    assertCodeableConcept(code, CodingSystem.EDQM_STANDARD, display, result);
  }

  @Test
  void testCodePharma() {
    // empty code
    assertEmptyCodeValue(medikation::setArzneimittel_code, medikation::getMedicationCodePharma);
    // non-empty code, with display from arzneimittel_name
    String code = "06312077", display = "ASS 100 - 1a Pharma TAH Tabletten";
    medikation.setArzneimittel_code(getCodeStr(code));
    medikation.setArzneimittel_name(display);
    Coding result = medikation.getMedicationCodePharma();
    assertCoding(code, CodingSystem.PHARMA_ZENTRAL_NUMMER, display, result);
    // non-empty code, but directly given display overwrites display of arzneimittel_name
    medikation.setArzneimittel_code(getCodeDisplayStr(code, display));
    medikation.setArzneimittel_name("ignored display");
    result = medikation.getMedicationCodePharma();
    assertCoding(code, CodingSystem.PHARMA_ZENTRAL_NUMMER, display, result);
  }

  @Test
  void testCodeAtcDE() {
    // empty code
    assertEmptyCodeValue(medikation::setArzneimittel_code, medikation::getMedicationCodeAtcDE);
    // non-empty code, with display from arzneimittel_name
    String code = "B01AC06", display = "acetylsalicylic acid";
    medikation.setArzneimittel_code(getCodeStr(code));
    medikation.setArzneimittel_name(display);
    Coding result = medikation.getMedicationCodeAtcDE();
    assertCoding(code, CodingSystem.ATC_DIMDI, display, result);
    // non-empty code, but directly given display overwrites display of arzneimittel_name
    medikation.setArzneimittel_code(getCodeDisplayStr(code, display));
    medikation.setArzneimittel_name("ignored display");
    result = medikation.getMedicationCodeAtcDE();
    assertCoding(code, CodingSystem.ATC_DIMDI, display, result);
  }

  @Test
  void testCode() {
    // empty code and empty text
    medikation.setArzneimittel_name("");
    medikation.setRezeptur_freitextzeile("");
    assertEmptyCodeValue(medikation::setArzneimittel_code, medikation::getMedicationCode);
    // empty code and non-empty text
    medikation.setArzneimittel_name("");
    medikation.setArzneimittel_code("");
    String text = "some medication info";
    medikation.setRezeptur_freitextzeile(text);
    CodeableConcept result = medikation.getMedicationCode();
    assertNonEmptyValue(result);
    assertFalse(result.hasCoding());
    assertEquals(text, result.getText());
    // non-empty code, with pharma-system
    medikation.setRezeptur_freitextzeile("");
    String code = "06312077",
        system = CodingSystem.PHARMA_ZENTRAL_NUMMER,
        display = "ASS 100 - 1a Pharma TAH Tabletten";
    medikation.setArzneimittel_code(getCodeDisplaySystemStr(code, display, system));
    result = medikation.getMedicationCode();
    assertCodeableConcept(code, system, display, result);
    // non-empty code, with no system
    code = "B01AC06";
    display = "acetylsalicylic acid";
    medikation.setArzneimittel_code(getCodeDisplayStr(code, display));
    result = medikation.getMedicationCode();
    assertCodeableConcept(code, CodingSystem.ATC_DIMDI, display, result);
  }
}
