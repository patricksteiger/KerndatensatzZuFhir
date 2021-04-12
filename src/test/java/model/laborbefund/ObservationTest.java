package model.laborbefund;

import constants.CodingCode;
import constants.CodingSystem;
import constants.Constants;
import constants.IdentifierSystem;
import enums.*;
import helper.Logger;
import model.Laborbefund;
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

class ObservationTest {
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
  void testReferenceRangeHigh() {
    // empty grenze [NO LOGGING]
    laborbefund.setLaboruntersuchung_referenzbereich_obergrenze("");
    assertEmptyValue(laborbefund.getObservationReferenceRangeHigh());
    // invalid grenze
    String grenze = getValueUnitStr("abc", "invalid unit");
    laborbefund.setLaboruntersuchung_referenzbereich_obergrenze(grenze);
    assertEmptyValue(laborbefund.getObservationReferenceRangeHigh());
    // valid grenze
    grenze = "127";
    String input = getValueStr(grenze);
    laborbefund.setLaboruntersuchung_referenzbereich_obergrenze(input);
    Quantity result = laborbefund.getObservationReferenceRangeHigh();
    assertQuantity(new BigDecimal(grenze), "1", Constants.QUANTITY_SYSTEM, "1", result);
  }

  @Test
  void testReferenceRangeLow() {
    // empty grenze [NO LOGGING]
    laborbefund.setLaboruntersuchung_referenzbereich_untergrenze("");
    assertEmptyValue(laborbefund.getObservationReferenceRangeLow());
    // invalid grenze
    String grenze = getValueUnitStr("abc", "invalid unit");
    laborbefund.setLaboruntersuchung_referenzbereich_untergrenze(grenze);
    assertEmptyValue(laborbefund.getObservationReferenceRangeLow());
    // valid grenze
    grenze = "72";
    String input = getValueStr(grenze);
    laborbefund.setLaboruntersuchung_referenzbereich_obergrenze(input);
    Quantity result = laborbefund.getObservationReferenceRangeHigh();
    assertQuantity(new BigDecimal(grenze), "1", Constants.QUANTITY_SYSTEM, "1", result);
  }

  @Test
  void testReferenceRangeType() {
    // empty typ [NO LOGGING]
    assertEmptyCodeValue(
        laborbefund::setLaboruntersuchung_referenzbereich_typ,
        laborbefund::getObservationReferenceRangeType);
    // invalid typ
    laborbefund.setLaboruntersuchung_referenzbereich_typ("invalid typ");
    assertEmptyValue(laborbefund.getObservationReferenceRangeType());
    // valid typ
    ReferenceRangeMeaning typ = ReferenceRangeMeaning.NORMAL;
    laborbefund.setLaboruntersuchung_referenzbereich_typ(getCodeStr(typ.getCode()));
    CodeableConcept result = laborbefund.getObservationReferenceRangeType();
    assertCodeableConcept(typ, result);
  }

  @Test
  void testReferenceRange() {
    // empty untergrenze, obergrenze, typ [NO LOGGING]
    laborbefund.setLaboruntersuchung_referenzbereich_untergrenze("");
    laborbefund.setLaboruntersuchung_referenzbereich_obergrenze("");
    laborbefund.setLaboruntersuchung_referenzbereich_typ("");
    assertEmptyValue(laborbefund.getObservationReferenceRange());
    // non-empty untergrenze, obergrenze, typ [NO LOGGING]
    String untergrenze = "71";
    laborbefund.setLaboruntersuchung_referenzbereich_untergrenze(getValueStr(untergrenze));
    String obergrenze = "132";
    laborbefund.setLaboruntersuchung_referenzbereich_obergrenze(getValueStr(obergrenze));
    ReferenceRangeMeaning typ = ReferenceRangeMeaning.LUTEAL;
    laborbefund.setLaboruntersuchung_referenzbereich_typ(getCodeStr(typ.getCode()));
    Observation.ObservationReferenceRangeComponent result =
        laborbefund.getObservationReferenceRange();
    assertNonEmptyValue(result);
    assertQuantity(
        new BigDecimal(untergrenze), "1", Constants.QUANTITY_SYSTEM, "1", result.getLow());
    assertQuantity(
        new BigDecimal(obergrenze), "1", Constants.QUANTITY_SYSTEM, "1", result.getHigh());
    assertCodeableConcept(typ, result.getType());
  }

  @Test
  void testMethod() {
    // empty methode [NO LOGGING]
    assertEmptyCodeValue(
        laborbefund::setLaboruntersuchung_untersuchungsmethode, laborbefund::getObservationMethod);
    // non-empty methode
    String code = "method", system = "method-system", display = "Untersuchungsmethode";
    laborbefund.setLaboruntersuchung_untersuchungsmethode(
        getCodeDisplaySystemStr(code, display, system));
    CodeableConcept result = laborbefund.getObservationMethod();
    assertCodeableConcept(code, system, display, result);
  }

  @Test
  void testNote() {
    // empty kommentar [NO LOGGING]
    laborbefund.setLaboruntersuchung_kommentar("");
    assertEmptyValue(laborbefund.getObservationNote());
    // non-empty kommentar
    String kommentar = "some comment";
    laborbefund.setLaboruntersuchung_kommentar(kommentar);
    Annotation result = laborbefund.getObservationNote();
    assertAnnotation(kommentar, result);
  }

  @Test
  void testValueCodeableConcept() {
    // invalid ergebnis [NO LOGGING]
    laborbefund.setLaboruntersuchung_ergebnis("invalid result");
    assertEmptyValue(laborbefund.getObservationValueCodeableConcept());
    // valid ergebnis
    SemiQuantitativesLaborergebnis ergebnis = SemiQuantitativesLaborergebnis.ONE_PLUS;
    laborbefund.setLaboruntersuchung_ergebnis(getCodeStr(ergebnis.getCode()));
    CodeableConcept result = laborbefund.getObservationValueCodeableConcept();
    assertCodeableConcept(ergebnis, result);
  }

  @Test
  void testValueQuantity() {
    // invalid quantity
    laborbefund.setLaboruntersuchung_ergebnis("invalid result");
    assertEmptyValue(laborbefund.getObservationValueCodeableConcept());
    // valid quantity
    String value = "13.5", unit = "mg";
    laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
    Quantity result = laborbefund.getObservationValueQuantity();
    assertQuantity(new BigDecimal(value), unit, Constants.QUANTITY_SYSTEM, "(milligram)", result);
  }

  @Test
  void testValue() {
    // invalid ergebnis
    laborbefund.setLaboruntersuchung_ergebnis("invalid result");
    assertEmptyValue(laborbefund.getObservationValue());
    // semi-quantitatives ergebnis
    SemiQuantitativesLaborergebnis semiErgebnis = SemiQuantitativesLaborergebnis.FOUR_PLUS;
    laborbefund.setLaboruntersuchung_ergebnis(getCodeStr(semiErgebnis.getCode()));
    Type result = laborbefund.getObservationValue();
    assertNonEmptyValue(result);
    assertTrue(result instanceof CodeableConcept);
    assertCodeableConcept(semiErgebnis, (CodeableConcept) result);
    // ergebnis
    String value = "10e3";
    laborbefund.setLaboruntersuchung_ergebnis(getValueStr(value));
    result = laborbefund.getObservationValue();
    assertNonEmptyValue(result);
    assertTrue(result instanceof Quantity);
    assertQuantity(new BigDecimal(value), "1", Constants.QUANTITY_SYSTEM, "1", (Quantity) result);
  }

  @Test
  void testIssued() {
    // empty datum [NO LOGGING]
    laborbefund.setLaboruntersuchung_dokumentationsdatum("");
    assertEmptyValue(laborbefund.getObservationIssued());
    // invalid datum
    laborbefund.setLaboruntersuchung_dokumentationsdatum("invalid date");
    assertEmptyValue(laborbefund.getObservationIssued());
    // valid datum
    String datum = "2013-08-24";
    laborbefund.setLaboruntersuchung_dokumentationsdatum(datum);
    assertEquals(expectedDateString(datum), laborbefund.getObservationIssued());
  }

  @Test
  void testEffective() {
    // invalid datum
    laborbefund.setLaboruntersuchung_untersuchungszeitpunkt("invalid date");
    assertEmptyValue(laborbefund.getObservationEffective());
    // valid datum
    String datum = "2013-08-24T10:13:27";
    laborbefund.setLaboruntersuchung_untersuchungszeitpunkt(datum);
    DateTimeType result = laborbefund.getObservationEffective();
    assertDateTimeType(expectedDateString(datum), result);
  }

  @Test
  void testCode() {
    // empty/invalid code
    assertEmptyCodeValue(laborbefund::setLaboruntersuchung_code, laborbefund::getObservationCode);
    // valid code, empty text
    String code = "20570-8", display = "Hematocrit [Volume Fraction] of Blood";
    laborbefund.setLaboruntersuchung_code(getCodeDisplayStr(code, display));
    CodeableConcept result = laborbefund.getObservationCode();
    assertCodeableConcept(code, CodingSystem.LOINC, display, result);
    assertFalse(result.hasText());
    // valid code and text
    laborbefund.setLaboruntersuchung_code(getCodeDisplayStr(code, display));
    String bezeichnung = "some text";
    laborbefund.setLaborparameter_bezeichnung(bezeichnung);
    result = laborbefund.getObservationCode();
    assertCodeableConcept(code, CodingSystem.LOINC, display, result);
    assertEquals(bezeichnung, result.getText());
  }

  @Test
  void testCategoryLoinc() {
    // fixed values
    Coding result = laborbefund.getObservationCategoryLoinc();
    assertCoding(CodingCode.LOINC_LAB, CodingSystem.LOINC, result);
  }

  @Test
  void testCategoryCategory() {
    // fixed values
    Coding result = laborbefund.getObservationCategoryCategory();
    assertCoding(CodingCode.LABORATORY, CodingSystem.OBSERVATION_CATEGORY_TERMINOLOGY, result);
  }

  @Test
  void testCategoryBereich() {
    // empty code [NO LOGGING]
    assertEmptyCodeValue(
        laborbefund::setLaborbereich_code, laborbefund::getObservationCategoryBereich);
    // invalid code
    laborbefund.setLaborbereich_code("invalid code");
    assertEmptyValue(laborbefund.getObservationCategoryBereich());
    // valid code
    Laborbereich code = Laborbereich.CELL_MARKER;
    laborbefund.setLaborbereich_code(getCodeStr(code.getCode()));
    Coding result = laborbefund.getObservationCategoryBereich();
    assertCoding(code, result);
  }

  @Test
  void testCategoryGruppen() {
    // empty code [NO LOGGING]
    assertEmptyCodeValue(
        laborbefund::setLaborgruppe_code, laborbefund::getObservationCategoryGruppen);
    // invalid code
    laborbefund.setLaborgruppe_code("invalid code");
    assertEmptyValue(laborbefund.getObservationCategoryGruppen());
    // valid code
    Laborstruktur code = Laborstruktur.ZYTOLOGIE;
    laborbefund.setLaborgruppe_code(getCodeStr(code.getCode()));
    Coding result = laborbefund.getObservationCategoryGruppen();
    assertCoding(code, result);
  }

  @Test
  void testStatus() {
    // invalid status
    laborbefund.setLaboruntersuchung_status("invalid status");
    assertEmptyValue(laborbefund.getObservationStatus());
    // valid status
    Observation.ObservationStatus status = Observation.ObservationStatus.FINAL;
    laborbefund.setLaboruntersuchung_status(getCodeStr(status.toCode()));
    Observation.ObservationStatus result = laborbefund.getObservationStatus();
    assertEquals(status, result);
  }

  @Test
  void testIdentifier() {
    // empty id
    laborbefund.setLaboruntersuchung_identifikation("");
    assertEmptyValue(laborbefund.getObservationIdentifier());
    // non-empty id
    String id = "59826-8_1234567890";
    laborbefund.setLaboruntersuchung_identifikation(id);
    Identifier result = laborbefund.getObservationIdentifier();
    assertIdentifier(id, IdentifierSystem.EMPTY, result);
    assertCodeableConcept(IdentifierTypeCode.OBI, result.getType());
  }
}
