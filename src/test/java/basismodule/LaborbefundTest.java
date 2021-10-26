package basismodule;

import constants.IdentifierSystem;
import constants.ReferenceType;
import helper.Logger;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import valueSets.IdentifierTypeCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static constants.Constants.BIG_DECIMAL_ROUNDING_MODE;
import static constants.Constants.BIG_DECIMAL_SCALE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

public class LaborbefundTest {
  private static final String LOINC_SYSTEM = "http://loinc.org";
  private static final String QUANTITY_SYSTEM = "http://unitsofmeasure.org";
  private static final String SNOMED_SYSTEM = "http://snomed.info/sct";
  private static final String REFERENCE_RANGE_MEANING_SYSTEM =
      "http://terminology.hl7.org/CodeSystem/referencerange-meaning";
  private static final String QUELLE_BEZUGSDATUM_URL =
      "https://www.medizininformatik-initiative.de/fhir/core/modul-labor/StructureDefinition/QuelleKlinischesBezugsdatum";
  private static Logger LOGGER;
  private Laborbefund laborbefund;

  @BeforeAll
  static void init() {
    LOGGER = Mockito.mock(Logger.class, Mockito.CALLS_REAL_METHODS);
    setUpLoggerMock(LOGGER);
  }

  @BeforeEach
  public void setUp() {
    clearInvocations(LOGGER);
    laborbefund = new Laborbefund(LOGGER);
  }

  @Nested
  class DiagnosticReportTest {

    @Nested
    class SpecimenTest {
      @Test
      @DisplayName("empty Probenmaterialidentifikation should result in empty value")
      void testEmptyId() {
        laborbefund.setProbenmaterial_identifikation("");
        assertEmptyValue(laborbefund.getDiagnosticReportSpecimen());
      }

      @Test
      @DisplayName("non-empty Probenmaterialidentifikation should be present in Reference")
      void testSpecimen() {
        String id = "1234567890";
        laborbefund.setProbenmaterial_identifikation(id);
        Reference result = laborbefund.getDiagnosticReportSpecimen();
        assertReference(ReferenceType.SPECIMEN, id, IdentifierSystem.EMPTY, result);
      }
    }

    @Nested
    class IssuedTest {
      @Test
      @DisplayName("invalid Dokumentationsdatum should result in empty value")
      void testInvalidD() {
        laborbefund.setDokumentationsdatum("");
        assertEmptyValue(laborbefund.getDiagnosticReportIssued());
      }

      @Test
      @DisplayName("valid Dokumentationsdatum should be present in Date")
      void testIssued() {
        String date = "2015-07-19";
        laborbefund.setDokumentationsdatum(date);
        Date result = laborbefund.getDiagnosticReportIssued();
        assertEquals(expectedDateString(date), result);
      }
    }

    @Nested
    class EffectiveTest {
      @Test
      @DisplayName("empty Abnahmezeitpunkt and Laboreingangszeitpunkt should result in empty value")
      void testEmptyAL() {
        laborbefund.setProbenmaterial_abnahmezeitpunkt("");
        laborbefund.setProbenmaterial_laboreingangszeitpunkt("");
        assertEmptyValue(laborbefund.getDiagnosticReportEffective());
      }

      @Test
      @DisplayName(
          "valid Laboreingangszeitpunkt should be present in DateTimeType, when Abnahmezeitpunkt is empty")
      void testNonEmptyAAndEmptyL() {
        laborbefund.setProbenmaterial_abnahmezeitpunkt("");
        String eingang = "2020-11-13";
        laborbefund.setProbenmaterial_laboreingangszeitpunkt(eingang);
        DateTimeType result = laborbefund.getDiagnosticReportEffective();
        assertDateTimeType(expectedDateString(eingang), result);
      }

      @Test
      @DisplayName(
          "valid Abnahmezeitpunkt should be present in DateTimeType, even when Laboreingangszeitpunkt is valid")
      void testValidA() {
        String eingang = "2020-11-13";
        laborbefund.setProbenmaterial_laboreingangszeitpunkt(eingang);
        String abnahme = "2021-02-26";
        laborbefund.setProbenmaterial_abnahmezeitpunkt(abnahme);
        DateTimeType result = laborbefund.getDiagnosticReportEffective();
        assertDateTimeType(expectedDateString(abnahme), result);
      }

      @Test
      @DisplayName(
          "invalid Abnahmezeitpunkt should result in empty value, even when Laboreingangszeitpunkt is valid")
      void testEffective() {
        String eingang = "2020-11-13";
        laborbefund.setProbenmaterial_laboreingangszeitpunkt(eingang);
        laborbefund.setProbenmaterial_abnahmezeitpunkt("invalid date");
        assertEmptyValue(laborbefund.getDiagnosticReportEffective());
      }
    }

    @Nested
    class CodeTest {

      private final String LAB_REPORT = "11502-2";

      @Test
      @DisplayName("code is fixed value: labReport")
      void testCode() {
        CodeableConcept result = laborbefund.getDiagnosticReportCode();
        assertCodeableConcept(LAB_REPORT, LOINC_SYSTEM, result);
      }

      @Nested
      class LabReportTest {
        @Test
        @DisplayName("lapReport is fixed value: 11502-2")
        void testLabReport() {
          Coding result = laborbefund.getDiagnosticReportLabReport();
          assertCoding(LAB_REPORT, LOINC_SYSTEM, result);
        }
      }
    }

    @Nested
    class StatusTest {
      @Test
      @DisplayName("invalid Status should result in empty value")
      void testInvalidStatus() {
        laborbefund.setStatus(null);
        assertEmptyValue(laborbefund.getDiagnosticReportStatus());
      }

      @Test
      @DisplayName("valid Status should be present in DiagnosticReportStatus")
      void testStatus() {
        DiagnosticReport.DiagnosticReportStatus status =
            DiagnosticReport.DiagnosticReportStatus.FINAL;
        laborbefund.setStatus(getCodeStr("FINAL"));
        Assertions.assertEquals(status, laborbefund.getDiagnosticReportStatus());
      }
    }

    @Nested
    class CategoryTest {

      private final String SERVICE_SECTION_SYSTEM = "http://terminology.hl7.org/CodeSystem/v2-0074";
      private final String SERVICE_SECTION_LAB = "LAB";
      private final String LOINC_LAB = "26436-6";

      @Test
      @DisplayName("category is fixed values: serivceSection and loincLab")
      void testC() {
        CodeableConcept result = laborbefund.getDiagnosticReportCategory();
        assertNonEmptyValue(result);
        List<Coding> codings = result.getCoding();
        assertEquals(2, codings.size());
        assertCoding(LOINC_LAB, LOINC_SYSTEM, codings.get(0));
        assertCoding(SERVICE_SECTION_LAB, SERVICE_SECTION_SYSTEM, codings.get(1));
      }

      @Nested
      class ServiceSectionTest {
        @Test
        @DisplayName("serviceSection is fixed value: LAB")
        void testService() {
          Coding result = laborbefund.getDiagnosticReportServiceSection();
          assertCoding(SERVICE_SECTION_LAB, SERVICE_SECTION_SYSTEM, result);
        }
      }

      @Nested
      class LoincLabTest {
        @Test
        @DisplayName("loincLab is fixed value: 26436-6")
        void testLoincLab() {
          Coding result = laborbefund.getDiagnosticReportLoincLab();
          assertCoding(LOINC_LAB, LOINC_SYSTEM, result);
        }
      }
    }

    @Nested
    class IdentifierTest {
      @Nested
      class BefundTest {
        @Test
        @DisplayName("empty Identifikation should result in empty value")
        void testEmptyB() {
          laborbefund.setIdentifikation("");
          assertEmptyValue(laborbefund.getDiagnosticReportBefund());
        }

        @Test
        @DisplayName("non-empty Identifikation should be present in Identifier")
        void testBefund() {
          String id = "0987654321";
          laborbefund.setIdentifikation(id);
          Identifier result = laborbefund.getDiagnosticReportBefund();
          assertIdentifier(id, IdentifierSystem.EMPTY, result);
          assertTrue(result.hasType());
          CodeableConcept type = result.getType();
          String expectedCode = "FILL",
              IDENTIFIER_TYPE_SYSTEM = "http://terminology.hl7.org/CodeSystem/v2-0203",
              expectedDisplay = "Filler Identifier";
          assertCodeableConcept(expectedCode, IDENTIFIER_TYPE_SYSTEM, expectedDisplay, type);
          assertTrue(result.hasAssigner());
          String organization = "Organization/UKU";
          assertEquals(organization, result.getAssigner().getReference());
        }
      }
    }
  }

  @Nested
  class ObservationTest {

    @Nested
    class ReferenceRangeTest {
      @Test
      @DisplayName(
          "empty Referenzbereichuntergrenze, -obergrenze and -typ should result in empty value")
      void testEmptyR() {
        laborbefund.setLaboruntersuchung_referenzbereich_untergrenze("");
        laborbefund.setLaboruntersuchung_referenzbereich_obergrenze("");
        laborbefund.setLaboruntersuchung_referenzbereich_typ(null);
        assertEmptyValue(laborbefund.getObservationReferenceRange());
      }

      @Test
      @DisplayName(
          "non-empty Referenzbereichuntergrenze, -obergrenze and -typ should be present in ObservationReferenceRangeComponent")
      void testNonEmptyR() {
        String untergrenze = "71";
        laborbefund.setLaboruntersuchung_referenzbereich_untergrenze(getValueStr(untergrenze));
        String obergrenze = "132";
        laborbefund.setLaboruntersuchung_referenzbereich_obergrenze(getValueStr(obergrenze));
        String typeCode = "luteal", typeDisplay = "Luteal";
        laborbefund.setLaboruntersuchung_referenzbereich_typ(
            getCodeDisplayStr(typeCode, typeDisplay));
        Observation.ObservationReferenceRangeComponent result =
            laborbefund.getObservationReferenceRange();
        assertNonEmptyValue(result);
        assertQuantity(
            new BigDecimal(untergrenze), null, QUANTITY_SYSTEM, "(unity)", result.getLow());
        assertQuantity(
            new BigDecimal(obergrenze), null, QUANTITY_SYSTEM, "(unity)", result.getHigh());
        assertCodeableConcept(
            typeCode, REFERENCE_RANGE_MEANING_SYSTEM, typeDisplay, result.getType());
      }

      @Nested
      class LowTest {
        @Test
        @DisplayName("empty Referenzbereichuntergrenze should result in empty value")
        void testEmptyR() {
          laborbefund.setLaboruntersuchung_referenzbereich_untergrenze("");
          assertEmptyValue(laborbefund.getObservationReferenceRangeLow());
        }

        @Test
        @DisplayName("invalid Referenzbereichuntergrenze should result in empty value")
        void testInvalidR() {
          String grenze = getValueUnitStr("abc", "invalid unit");
          laborbefund.setLaboruntersuchung_referenzbereich_untergrenze(grenze);
          assertEmptyValue(laborbefund.getObservationReferenceRangeLow());
        }

        @Test
        @DisplayName("valid Referenzbereichuntergrenze should be present in Quantity")
        void testReferenceRangeLow() {
          String grenze = "72";
          String input = getValueStr(grenze);
          laborbefund.setLaboruntersuchung_referenzbereich_obergrenze(input);
          Quantity result = laborbefund.getObservationReferenceRangeHigh();
          assertQuantity(new BigDecimal(grenze), null, QUANTITY_SYSTEM, "(unity)", result);
        }
      }

      @Nested
      class HighTest {
        @Test
        @DisplayName("empty Referenzbereichobergrenze should result in empty value")
        void testEmptyR() {
          laborbefund.setLaboruntersuchung_referenzbereich_obergrenze("");
          assertEmptyValue(laborbefund.getObservationReferenceRangeHigh());
        }

        @Test
        @DisplayName("invalid Referenzbereichobergrenze should result in empty value")
        void testInvalidR() {
          String grenze = getValueUnitStr("abc", "invalid unit");
          laborbefund.setLaboruntersuchung_referenzbereich_obergrenze(grenze);
          assertEmptyValue(laborbefund.getObservationReferenceRangeHigh());
        }

        @Test
        @DisplayName("valid Referenzbereichobergrenze should be present in Quantity")
        void testReferenceRangeHigh() {
          String grenze = "127";
          String input = getValueStr(grenze);
          laborbefund.setLaboruntersuchung_referenzbereich_obergrenze(input);
          Quantity result = laborbefund.getObservationReferenceRangeHigh();
          assertQuantity(new BigDecimal(grenze), null, QUANTITY_SYSTEM, "(unity)", result);
        }
      }

      @Nested
      class TypeTest {
        @Test
        @DisplayName("empty Referenzbereichtyp should result in empty value")
        void testEmptyR() {
          assertEmptyCodeValue(
              laborbefund::setLaboruntersuchung_referenzbereich_typ,
              laborbefund::getObservationReferenceRangeType);
        }

        @Test
        @DisplayName("empty Referenzbereichtyp should result in empty value")
        void testInvalidR() {
          laborbefund.setLaboruntersuchung_referenzbereich_typ("invalid typ");
          assertEmptyValue(laborbefund.getObservationReferenceRangeType());
        }

        @Test
        @DisplayName("valid Referenzbereichtyp should be present in CodeableConcept")
        void testReferenceRangeType() {
          String typeCode = "normal", typeDisplay = "Normal Range";
          laborbefund.setLaboruntersuchung_referenzbereich_typ(
              getCodeDisplayStr(typeCode, typeDisplay));
          CodeableConcept result = laborbefund.getObservationReferenceRangeType();
          assertCodeableConcept(typeCode, REFERENCE_RANGE_MEANING_SYSTEM, typeDisplay, result);
        }
      }
    }

    @Nested
    class MethodTest {
      @Test
      @DisplayName("empty Untersuchungsmethode should result in empty value")
      void testEmptyM() {
        assertEmptyCodeValue(
            laborbefund::setLaboruntersuchung_untersuchungsmethode,
            laborbefund::getObservationMethod);
      }

      @Test
      @DisplayName("non-empty Untersuchungsmethode should be present in CodeableConcept")
      void testMethod() {
        String code = "method", system = "method-system", display = "Untersuchungsmethode";
        laborbefund.setLaboruntersuchung_untersuchungsmethode(
            getCodeDisplaySystemStr(code, display, system));
        CodeableConcept result = laborbefund.getObservationMethod();
        assertCodeableConcept(code, system, display, result);
      }
    }

    @Nested
    class NoteTest {
      @Test
      @DisplayName("empty Kommentar should result in empty value")
      void testEmptyN() {
        laborbefund.setLaboruntersuchung_kommentar("");
        assertEmptyValue(laborbefund.getObservationNote());
      }

      @Test
      @DisplayName("non-empty Kommentar should be present in Annotation")
      void testNote() {
        String kommentar = "some comment";
        laborbefund.setLaboruntersuchung_kommentar(kommentar);
        Annotation result = laborbefund.getObservationNote();
        assertAnnotation(kommentar, result);
      }
    }

    @Nested
    class InterpretationTest {
      @Test
      @DisplayName("empty Bewertung should result in empty value")
      void testEmptyI() {
        assertEmptyCodeValue(
            laborbefund::setLaboruntersuchung_bewertung, laborbefund::getObservationInterpretation);
      }

      @Test
      @DisplayName("invalid Bewertung should result in empty value")
      void testInvalidT() {
        laborbefund.setLaboruntersuchung_bewertung(getCodeStr("invalid interpretation"));
        assertEmptyValue(laborbefund.getObservationInterpretation());
      }

      @Test
      @DisplayName("valid Bewertung should be present in CodeableConcept")
      void testInterpretation() {
        String interpretation = "N";
        laborbefund.setLaboruntersuchung_bewertung(getCodeStr(interpretation));
        CodeableConcept result = laborbefund.getObservationInterpretation();
        assertCodeableConcept(
            interpretation,
            "http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation",
            "Normal",
            result);
      }
    }

    @Nested
    class ValueTest {

      @Test
      @DisplayName("valid, semi-quantitative Ergebnis should be present in CodeableConcept")
      void testSemiV() {
        String fourPlusCode = "260350009",
            fourPlusDisplay = "Present ++++ out of ++++ (qualifier value)";
        laborbefund.setLaboruntersuchung_ergebnis(getCodeDisplayStr(fourPlusCode, fourPlusDisplay));
        Type result = laborbefund.getObservationValue();
        assertNonEmptyValue(result);
        assertTrue(result instanceof CodeableConcept);
        assertCodeableConcept(
            fourPlusCode, SNOMED_SYSTEM, fourPlusDisplay, (CodeableConcept) result);
      }

      @Test
      @DisplayName("valid, non-semi-quantitative Ergebnis should be present in Quantity")
      void testValue() {
        String value = "10e3";
        laborbefund.setLaboruntersuchung_ergebnis(getValueStr(value));
        Type result = laborbefund.getObservationValue();
        assertNonEmptyValue(result);
        assertTrue(result instanceof Quantity);
        assertQuantity(new BigDecimal(value), null, QUANTITY_SYSTEM, "(unity)", (Quantity) result);
      }

      @Nested
      class ValueCodeableConceptTest {
        @Test
        @DisplayName("invalid Ergebnis should result in empty value")
        void testInvalidC() {
          laborbefund.setLaboruntersuchung_ergebnis("invalid result");
          assertEmptyValue(laborbefund.getObservationValueCodeableConcept());
        }

        @Test
        @DisplayName("valid, semi-quantitative Ergebnis should be present in CodeableConcept")
        void testValueCodeableConcept() {
          String onePlusOfThreeCode = "441614007",
              onePlusOfThreeDisplay = "Present one plus out of three plus";
          laborbefund.setLaboruntersuchung_ergebnis(
              getCodeDisplayStr(onePlusOfThreeCode, onePlusOfThreeDisplay));
          CodeableConcept result = laborbefund.getObservationValueCodeableConcept();
          assertCodeableConcept(onePlusOfThreeCode, SNOMED_SYSTEM, onePlusOfThreeDisplay, result);
        }
      }

      @Nested
      class ValueQuantityTest {
        @Test
        @DisplayName("invalid Ergebnis should result in empty value")
        void testInvalidQ() {
          laborbefund.setLaboruntersuchung_ergebnis("invalid result");
          assertEmptyValue(laborbefund.getObservationValueCodeableConcept());
        }

        @Test
        @DisplayName("invalid Ergebnis dividing by 0 should result in empty value")
        void testInvalidFraction() {
          String value = "0/0", unit = "mg";
          laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
          assertEmptyValue(laborbefund.getObservationValueCodeableConcept());
        }

        @Test
        @DisplayName("valid, non-semi-quantitative Ergebnis should be present in Quantity")
        void testValueQuantity() {
          String value = "7/4", unit = "mg";
          laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
          Quantity result = laborbefund.getObservationValueQuantity();
          BigDecimal expectedValue =
              new BigDecimal("1.75").setScale(BIG_DECIMAL_SCALE, BIG_DECIMAL_ROUNDING_MODE);
          String expectedUnit = "mg";
          String expectedDisplay = "(milligram)";
          assertQuantity(expectedValue, expectedUnit, QUANTITY_SYSTEM, expectedDisplay, result);
        }

        @Test
        @DisplayName(
            "valid Ergebnis with Local Code and LOINC-Mapping should be mapped and present in Quantity")
        void testMapping() {
          String code = "10002", value = "17.34", unit = "mmol/l";
          laborbefund.setLaboruntersuchung_code(code);
          laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
          Quantity result = laborbefund.getObservationValueQuantity();
          BigDecimal expectedValue = new BigDecimal(value);
          String expectedUnit = "mmol/L";
          String expectedDisplay = "(millimole) / (liter)";
          assertQuantity(expectedValue, expectedUnit, QUANTITY_SYSTEM, expectedDisplay, result);
        }

        @Test
        @DisplayName(
            "valid Ergebnis with Local Code and LOINC-Mapping should be mapped and present in Quantity with conversion")
        void testComplexMapping() {
          String code = "10058", value = "5.879", unit = "mg/dl";
          laborbefund.setLaboruntersuchung_code(code);
          laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
          Quantity result = laborbefund.getObservationValueQuantity();
          BigDecimal expectedValue = new BigDecimal("0.05879");
          String expectedUnit = "g/L";
          String expectedDisplay = "(gram) / (liter)";
          assertQuantity(expectedValue, expectedUnit, QUANTITY_SYSTEM, expectedDisplay, result);
        }

        @Test
        @DisplayName(
            "valid Ergebnis with Local Code and incorrect Local Unit-Mapping should be present in Quantity without conversion")
        void testComplexWrongUnitMapping() {
          String code = "10058", value = "5.879", unit = "kg/l";
          laborbefund.setLaboruntersuchung_code(code);
          laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
          Quantity result = laborbefund.getObservationValueQuantity();
          BigDecimal expectedValue = new BigDecimal("5.879");
          String expectedUnit = "kg/l";
          String expectedDisplay = "(kilogram) / (liter)";
          assertQuantity(expectedValue, expectedUnit, QUANTITY_SYSTEM, expectedDisplay, result);
        }

        @Test
        @DisplayName(
            "valid Ergebnis with Local Code and Local Unit-Mapping empty should be present in Quantity")
        void testEmptyMapping() {
          String code = "10052", value = "-7", unit = "";
          laborbefund.setLaboruntersuchung_code(code);
          laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
          Quantity result = laborbefund.getObservationValueQuantity();
          BigDecimal expectedValue = new BigDecimal(value);
          String expectedUnit = null;
          String expectedDisplay = "(unity)";
          assertQuantity(expectedValue, expectedUnit, QUANTITY_SYSTEM, expectedDisplay, result);
        }

        @Test
        @DisplayName(
            "valid Ergebnis as fraction with Local Code should be mapped and present in Quantity with conversion")
        void testUnit() {
          String code = "5307", value = "-16/8", unit = "µg/l";
          laborbefund.setLaboruntersuchung_code(code);
          laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
          Quantity result = laborbefund.getObservationValueQuantity();
          BigDecimal expectedValue =
              new BigDecimal("-2").setScale(BIG_DECIMAL_SCALE, BIG_DECIMAL_ROUNDING_MODE);
          String expectedUnit = "ng/mL";
          String expectedDisplay = "(nanogram) / (milliliter)";
          assertQuantity(expectedValue, expectedUnit, QUANTITY_SYSTEM, expectedDisplay, result);
        }

        @Test
        @DisplayName(
            "valid Ergebnis as fraction with Local Code with unknown conversion should be present in Quantity without conversion")
        void testLocalUnknown() {
          String code = "952", value = "17/8", unit = "µg/l";
          laborbefund.setLaboruntersuchung_code(code);
          laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
          Quantity result = laborbefund.getObservationValueQuantity();
          BigDecimal expectedValue =
              new BigDecimal("2.125").setScale(BIG_DECIMAL_SCALE, BIG_DECIMAL_ROUNDING_MODE);
          String expectedUnit = "µg/l";
          String expectedSystem = null;
          String expectedDisplay = "µg/l";
          assertQuantity(expectedValue, expectedUnit, expectedSystem, expectedDisplay, result);
        }
      }
    }

    @Nested
    class IssuedTest {
      @Test
      @DisplayName("empty Dokumentationsdatum should result in empty value")
      void testEmptyI() {
        laborbefund.setLaboruntersuchung_dokumentationsdatum("");
        assertEmptyValue(laborbefund.getObservationIssued());
      }

      @Test
      @DisplayName("invalid Dokumentationsdatum should result in empty value")
      void testInvalidI() {
        laborbefund.setLaboruntersuchung_dokumentationsdatum("invalid date");
        assertEmptyValue(laborbefund.getObservationIssued());
      }

      @Test
      @DisplayName("valid Dokumentationsdatum should be present in Date")
      void testIssued() {
        // valid datum
        String datum = "2013-08-24";
        laborbefund.setLaboruntersuchung_dokumentationsdatum(datum);
        Date result = laborbefund.getObservationIssued();
        Assertions.assertEquals(expectedDateString(datum), result);
      }
    }

    @Nested
    class EffectiveTest {
      @Test
      @DisplayName(
          "empty Abnahmezeitpunkt and invalid Laboreingangszeitpunkt should result in empty value")
      void testInvalidAEmptyL() {
        laborbefund.setProbenmaterial_abnahmezeitpunkt("");
        laborbefund.setProbenmaterial_laboreingangszeitpunkt("invalid date");
        assertEmptyValue(laborbefund.getObservationEffective());
      }

      @Test
      @DisplayName(
          "invalid Abnahmezeitpunkt should result in empty value, even when Laboreingangszeitpunkt is valid")
      void testInvalidA() {
        laborbefund.setProbenmaterial_abnahmezeitpunkt("invalid date");
        laborbefund.setProbenmaterial_laboreingangszeitpunkt("2013-08-24");
        assertEmptyValue(laborbefund.getObservationEffective());
      }

      @Test
      @DisplayName(
          "valid Abnahmezeitpunkt should be present in DateTimeType, when Laboreingangszeitpunkt is empty")
      void testValidA() {
        String abnahmezeitpunkt = "2013-08-24T10:13:27";
        laborbefund.setProbenmaterial_abnahmezeitpunkt(abnahmezeitpunkt);
        laborbefund.setProbenmaterial_laboreingangszeitpunkt("");
        DateTimeType result = laborbefund.getObservationEffective();
        assertDateTimeType(expectedDateString(abnahmezeitpunkt), result);
        Extension extension = result.getExtensionByUrl(QUELLE_BEZUGSDATUM_URL);
        assertNonEmptyValue(extension);
        String expectedCode = "399445004",
            expectedDisplay = "Specimen collection date (observable entity)";
        assertExtensionWithCoding(
            expectedCode, SNOMED_SYSTEM, expectedDisplay, QUELLE_BEZUGSDATUM_URL, extension);
      }

      @Test
      @DisplayName(
          "valid Laboreingangszeitpunkt should be present in DateTimeType, when Abnahmezeitpunkt is empty")
      void testValidL() {
        String laboreingangszeitpunkt = "2014-10-15";
        laborbefund.setProbenmaterial_abnahmezeitpunkt("");
        laborbefund.setProbenmaterial_laboreingangszeitpunkt(laboreingangszeitpunkt);
        DateTimeType result = laborbefund.getObservationEffective();
        assertDateTimeType(expectedDateString(laboreingangszeitpunkt), result);
        Extension extension = result.getExtensionByUrl(QUELLE_BEZUGSDATUM_URL);
        assertNonEmptyValue(extension);
        String expectedCode = "281271004",
            expectedDisplay = "Date sample received in laboratory (observable entity)";
        assertExtensionWithCoding(
            expectedCode, SNOMED_SYSTEM, expectedDisplay, QUELLE_BEZUGSDATUM_URL, extension);
      }
    }

    @Nested
    class CodeTest {
      @Test
      @DisplayName("invalid Code should result in empty value")
      void testInvalidC() {
        assertEmptyCodeValue(
            laborbefund::setLaboruntersuchung_code, laborbefund::getObservationCode);
      }

      @Test
      @DisplayName("valid Code should be present in CodeableConcept")
      void testValidV() {
        String code = "20570-8", display = "Hematocrit [Volume Fraction] of Blood";
        laborbefund.setLaboruntersuchung_code(getCodeDisplayStr(code, display));
        CodeableConcept result = laborbefund.getObservationCode();
        assertCodeableConcept(code, LOINC_SYSTEM, display, result);
        assertFalse(result.hasText());
      }

      @Test
      @DisplayName("valid Local Code with Loinc-Mapping should be present in CodeableConcept")
      void testValidVWithUnit() {
        String code = "5322", value = "1", unit = "IU/l";
        laborbefund.setLaboruntersuchung_code(getCodeStr(code));
        laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
        CodeableConcept result = laborbefund.getObservationCode();
        String expectedCode = "83098-4";
        String expectedDisplay = "Follitropin [Units/volume] in Serum or Plasma by Immunoassay";
        assertCodeableConcept(expectedCode, LOINC_SYSTEM, expectedDisplay, result);
        assertFalse(result.hasText());
      }

      @Test
      @DisplayName(
          "valid Local Code with Loinc-Mapping but incorrect unit should be present in CodeableConcept")
      void testValidLocalWithInvalidUnit() {
        String code = "5322", value = "1", unit = "g";
        laborbefund.setLaboruntersuchung_code(getCodeStr(code));
        laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
        CodeableConcept result = laborbefund.getObservationCode();
        String expectedCode = "5322";
        String expectedSystem = null;
        String expectedDisplay = "Follikel stim. Hormon (FSH).Serum .ECLIA";
        assertCodeableConcept(expectedCode, expectedSystem, expectedDisplay, result);
        assertFalse(result.hasText());
      }

      @Test
      @DisplayName("valid Local Code without Loinc-Mapping should be present in CodeableConcept")
      void testValidLocalWithoutMWithUnit() {
        String code = "988", value = "1", unit = "kg";
        laborbefund.setLaboruntersuchung_code(getCodeStr(code));
        laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
        CodeableConcept result = laborbefund.getObservationCode();
        String expectedCode = "988";
        String expectedSystem = null;
        String expectedDisplay = "Thyreoglobulin-Antikörper, Roche.Serum .ECLIA";
        assertCodeableConcept(expectedCode, expectedSystem, expectedDisplay, result);
        assertFalse(result.hasText());
      }

      @Test
      @DisplayName("non-Local-Code is seen as LOINC and should be present in CodeableConcept")
      void testInvalidCode() {
        String code = "1234567890", display = "test display", value = "1", unit = "kg";
        laborbefund.setLaboruntersuchung_code(getCodeDisplayStr(code, display));
        laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
        CodeableConcept result = laborbefund.getObservationCode();
        String expectedCode = code;
        String expectedSystem = LOINC_SYSTEM;
        String expectedDisplay = display;
        assertCodeableConcept(expectedCode, expectedSystem, expectedDisplay, result);
        assertFalse(result.hasText());
      }

      @Test
      @DisplayName("valid Code and non-empty Bezeichnung should be present in CodeableConcept")
      void testCode() {
        String code = "20570-8", display = "Hematocrit [Volume Fraction] of Blood";
        laborbefund.setLaboruntersuchung_code(getCodeDisplayStr(code, display));
        String bezeichnung = "some text";
        laborbefund.setLaborparameter_bezeichnung(bezeichnung);
        CodeableConcept result = laborbefund.getObservationCode();
        assertCodeableConcept(code, LOINC_SYSTEM, display, result);
        assertEquals(bezeichnung, result.getText());
      }
    }

    @Nested
    class CategoryTest {

      @Nested
      class LoincObservationTest {
        @Test
        @DisplayName("loinc has fixed value: 26436-6")
        void testCategoryLoinc() {
          Coding result = laborbefund.getObservationCategoryLoinc();
          assertCoding("26436-6", LOINC_SYSTEM, result);
        }
      }

      @Nested
      class ObservationCategoryTest {
        @Test
        @DisplayName("category has fixed value: laboratory")
        void testCategoryCategory() {
          Coding result = laborbefund.getObservationCategoryCategory();
          String OBSERVATION_CATEGORY_SYSTEM =
              "http://terminology.hl7.org/CodeSystem/observation-category";
          assertCoding("laboratory", OBSERVATION_CATEGORY_SYSTEM, result);
        }
      }

      @Nested
      class BereichTest {
        @Test
        @DisplayName("empty Code should result in empty value")
        void testEmptyB() {
          assertEmptyCodeValue(
              laborbefund::setLaborbereich_code, laborbefund::getObservationCategoryBereich);
        }

        @Test
        @DisplayName("invalid Code should result in empty value")
        void testInvalidB() {
          laborbefund.setLaborbereich_code("invalid code");
          assertEmptyValue(laborbefund.getObservationCategoryBereich());
        }

        @Test
        @DisplayName("valid Code should be present in Coding")
        void testCategoryBereich() {
          String expectedCode = "18718-7", expectedDisplay = "CELL MARKER STUDIES";
          laborbefund.setLaborbereich_code(getCodeDisplayStr(expectedCode, expectedDisplay));
          Coding result = laborbefund.getObservationCategoryBereich();
          assertCoding(expectedCode, LOINC_SYSTEM, expectedDisplay, result);
        }
      }

      @Nested
      class GruppenTest {
        @Test
        @DisplayName("empty Code should result in empty value")
        void testEmptyC() {
          assertEmptyCodeValue(
              laborbefund::setLaborgruppe_code, laborbefund::getObservationCategoryGruppen);
        }

        @Test
        @DisplayName("invalid Code should result in empty value")
        void testInvalidC() {
          laborbefund.setLaborgruppe_code("invalid code");
          assertEmptyValue(laborbefund.getObservationCategoryGruppen());
        }

        @Test
        @DisplayName("valid Code should be present in Coding")
        void testCategoryGruppen() {
          String expectedCode = "17880", expectedDisplay = "Zytologie";
          laborbefund.setLaborgruppe_code(getCodeDisplayStr(expectedCode, expectedDisplay));
          Coding result = laborbefund.getObservationCategoryGruppen();
          String LABORSTRUKTUR_SYSTEM = "urn:oid:1.2.40.0.34.5.11";
          assertCoding(expectedCode, LABORSTRUKTUR_SYSTEM, expectedDisplay, result);
        }
      }
    }

    @Nested
    class StatusTest {
      @Test
      @DisplayName("invalid Status should result in empty value")
      void testInvalidS() {
        laborbefund.setLaboruntersuchung_status("invalid status");
        assertEmptyValue(laborbefund.getObservationStatus());
      }

      @Test
      @DisplayName("valid Status should be present in ObservationStatus")
      void testStatus() {
        String status = "final";
        laborbefund.setLaboruntersuchung_status(getCodeStr(status));
        Observation.ObservationStatus result = laborbefund.getObservationStatus();
        assertEquals(Observation.ObservationStatus.FINAL, result);
      }
    }

    @Nested
    class IdentifierTest {
      @Test
      @DisplayName("empty Identifikation should result in empty value")
      void testEmptyId() {
        laborbefund.setLaboruntersuchung_identifikation("");
        assertEmptyValue(laborbefund.getObservationIdentifier());
      }

      @Test
      @DisplayName("non-empty Identifikation should be present in Identifier")
      void testIdentifier() {
        String id = "59826-8_1234567890";
        laborbefund.setLaboruntersuchung_identifikation(id);
        Identifier result = laborbefund.getObservationIdentifier();
        assertIdentifier(id, IdentifierSystem.EMPTY, result);
        assertCodeableConcept(IdentifierTypeCode.OBI, result.getType());
      }
    }
  }

  @Nested
  class ServiceRequestTest {

    @Nested
    class IdentifierTest {
      @Test
      @DisplayName("empty Identifikation should result in empty value")
      void testEmptyId() {
        laborbefund.setIdentifikation("");
        assertEmptyValue(laborbefund.getServiceRequestIdentifier());
      }

      @Test
      @DisplayName("non-empty Identifikation should be present in Identifier")
      void testIdentifier() {
        String id = "1234567890";
        laborbefund.setIdentifikation(id);
        Identifier result = laborbefund.getServiceRequestIdentifier();
        assertIdentifier(id, IdentifierSystem.EMPTY, IdentifierTypeCode.PLAC, result);
      }
    }

    @Nested
    class SpecimenTest {
      @Test
      @DisplayName("specimen is empty value")
      void testSpecimen() {
        assertEmptyValue(laborbefund.getServiceRequestSpecimen());
      }
    }

    @Nested
    class AuthoredOnTest {
      @Test
      @DisplayName("invalid Anforderungsdatum should result in empty value")
      void testInvalidD() {
        laborbefund.setLaboranforderung_anforderungsdatum("1997-12-34");
        assertEmptyValue(laborbefund.getServiceRequestAuthoredOn());
      }

      @Test
      @DisplayName("valid Anforderungsdatum should be present in Date")
      void testAuthoredOn() {
        String date = "2007-12-01";
        laborbefund.setLaboranforderung_anforderungsdatum(date);
        Date result = laborbefund.getServiceRequestAuthoredOn();
        assertEquals(expectedDateString(date), result);
      }
    }

    @Nested
    class CodeTest {
      @Test
      @DisplayName("empty Code should result in empty value")
      void testEmptyCode() {
        assertEmptyCodeValue(
            laborbefund::setLaboranforderung_laborparameter_code,
            laborbefund::getServiceRequestCode);
      }

      @Test
      @DisplayName("non-empty Code and Bezeichnung should be present in CodeableConcept")
      void testCode() {
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

      @Nested
      class TextTest {
        @Test
        @DisplayName("empty Bezeichnung should result in empty value")
        void testEmptyT() {
          laborbefund.setLaboranforderung_laborparameter_bezeichnung("");
          assertEmptyValue(laborbefund.getServiceRequestCodeText());
        }

        @Test
        @DisplayName("non-empty Bezeichnung should be present in String")
        void testNonEmptyText() {
          String text = "code text";
          laborbefund.setLaboranforderung_laborparameter_bezeichnung(text);
          Assertions.assertEquals(text, laborbefund.getServiceRequestCodeText());
        }
      }
    }

    @Nested
    class CategoryTest {
      @Test
      @DisplayName("category is fixed value: laboratory")
      void testCategory() {
        CodeableConcept result = laborbefund.getServiceRequestCategory();
        assertCodeableConcept(
            "laboratory", "http://terminology.hl7.org/CodeSystem/observation-category", result);
      }
    }

    @Nested
    class IntentTest {
      @Test
      @DisplayName("intent is fixed value: order")
      void testIntent() {
        Assertions.assertEquals(
            ServiceRequest.ServiceRequestIntent.ORDER, laborbefund.getServiceRequestIntent());
      }
    }

    @Nested
    class StatusTest {
      @Test
      @DisplayName("status is fixed value: completed")
      void testStatus() {
        Assertions.assertEquals(
            ServiceRequest.ServiceRequestStatus.COMPLETED, laborbefund.getServiceRequestStatus());
      }
    }
  }
}
