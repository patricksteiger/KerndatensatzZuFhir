package model;

import constants.Constants;
import constants.*;
import enums.*;
import helper.Logger;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

public class LaborbefundTest {
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
      @Test
      @DisplayName("code is fixed value: labReport")
      void testCode() {
        CodeableConcept result = laborbefund.getDiagnosticReportCode();
        assertCodeableConcept(CodingCode.LOINC_LAB_REPORT, CodingSystem.LOINC, result);
      }

      @Nested
      class LabReportTest {
        @Test
        @DisplayName("lapReport is fixed value: 11502-2")
        void testLabReport() {
          Coding result = laborbefund.getDiagnosticReportLabReport();
          assertCoding(CodingCode.LOINC_LAB_REPORT, CodingSystem.LOINC, result);
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
      @Test
      @DisplayName("category is fixed values: serivceSection and loincLab")
      void testC() {
        CodeableConcept result = laborbefund.getDiagnosticReportCategory();
        assertNonEmptyValue(result);
        List<Coding> codings = result.getCoding();
        assertEquals(2, codings.size());
        assertCoding(CodingCode.LOINC_LAB, CodingSystem.LOINC, codings.get(0));
        assertCoding(
            CodingCode.LAB_DIAGNOSTIC_REPORT,
            CodingSystem.DIAGNOSTIC_SERVICE_SECTION,
            codings.get(1));
      }

      @Nested
      class ServiceSectionTest {
        @Test
        @DisplayName("serviceSection is fixed value: LAB")
        void testService() {
          Coding result = laborbefund.getDiagnosticReportServiceSection();
          assertCoding(
              CodingCode.LAB_DIAGNOSTIC_REPORT, CodingSystem.DIAGNOSTIC_SERVICE_SECTION, result);
        }
      }

      @Nested
      class LoincLabTest {
        @Test
        @DisplayName("loincLab is fixed value: 26436-6")
        void testLoincLab() {
          Coding result = laborbefund.getDiagnosticReportLoincLab();
          assertCoding(CodingCode.LOINC_LAB, CodingSystem.LOINC, result);
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
          assertIdentifier(id, IdentifierSystem.EMPTY, IdentifierTypeCode.FILL, result);
          assertTrue(result.hasAssigner());
          assertEquals(MIIReference.MII_ORGANIZATION, result.getAssigner().getReference());
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
          assertQuantity(new BigDecimal(grenze), "1", Constants.QUANTITY_SYSTEM, "1", result);
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
          assertQuantity(new BigDecimal(grenze), "1", Constants.QUANTITY_SYSTEM, "1", result);
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
          // valid typ
          ReferenceRangeMeaning typ = ReferenceRangeMeaning.NORMAL;
          laborbefund.setLaboruntersuchung_referenzbereich_typ(getCodeStr(typ.getCode()));
          CodeableConcept result = laborbefund.getObservationReferenceRangeType();
          assertCodeableConcept(typ, result);
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
            "http://hl7.org/fhir/ValueSet/observation-interpretation",
            "Normal",
            result);
      }
    }

    @Nested
    class ValueTest {
      @Test
      @DisplayName("invalid Ergebnis should result in empty value")
      void testInvalidV() {
        laborbefund.setLaboruntersuchung_ergebnis("invalid result");
        assertEmptyValue(laborbefund.getObservationValue());
      }

      @Test
      @DisplayName("valid, semi-quantitative Ergebnis should be present in CodeableConcept")
      void testSemiV() {
        SemiQuantitativesLaborergebnis semiErgebnis = SemiQuantitativesLaborergebnis.FOUR_OF_FOUR;
        laborbefund.setLaboruntersuchung_ergebnis(getCodeStr(semiErgebnis.getCode()));
        Type result = laborbefund.getObservationValue();
        assertNonEmptyValue(result);
        assertTrue(result instanceof CodeableConcept);
        assertCodeableConcept(semiErgebnis, (CodeableConcept) result);
      }

      @Test
      @DisplayName("valid, non-semi-quantitative Ergebnis should be present in Quantity")
      void testValue() {
        String value = "10e3";
        laborbefund.setLaboruntersuchung_ergebnis(getValueStr(value));
        Type result = laborbefund.getObservationValue();
        assertNonEmptyValue(result);
        assertTrue(result instanceof Quantity);
        assertQuantity(
            new BigDecimal(value), "1", Constants.QUANTITY_SYSTEM, "1", (Quantity) result);
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
          SemiQuantitativesLaborergebnis ergebnis = SemiQuantitativesLaborergebnis.ONE_OF_THREE;
          laborbefund.setLaboruntersuchung_ergebnis(getCodeStr(ergebnis.getCode()));
          CodeableConcept result = laborbefund.getObservationValueCodeableConcept();
          assertCodeableConcept(ergebnis, result);
        }
      }

      @Nested
      class ValueQuantity {
        @Test
        @DisplayName("invalid Ergebnis should result in empty value")
        void testInvalidQ() {
          laborbefund.setLaboruntersuchung_ergebnis("invalid result");
          assertEmptyValue(laborbefund.getObservationValueCodeableConcept());
        }

        @Test
        @DisplayName("valid, non-semi-quantitative Ergebnis should be present in Quantity")
        void testValueQuantity() {
          String value = "13.5", unit = "mg";
          laborbefund.setLaboruntersuchung_ergebnis(getValueUnitStr(value, unit));
          Quantity result = laborbefund.getObservationValueQuantity();
          assertQuantity(
              new BigDecimal(value), unit, Constants.QUANTITY_SYSTEM, "(milligram)", result);
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
        Extension extension = result.getExtensionByUrl(ExtensionUrl.QUELLE_KLINISCHES_BEZUGSDATUM);
        assertNonEmptyValue(extension);
        assertExtensionWithCoding(
            QuelleKlinischesBezugsdatum.SPECIMEN,
            ExtensionUrl.QUELLE_KLINISCHES_BEZUGSDATUM,
            extension);
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
        Extension extension = result.getExtensionByUrl(ExtensionUrl.QUELLE_KLINISCHES_BEZUGSDATUM);
        assertNonEmptyValue(extension);
        assertExtensionWithCoding(
            QuelleKlinischesBezugsdatum.LABORATORY,
            ExtensionUrl.QUELLE_KLINISCHES_BEZUGSDATUM,
            extension);
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
        assertCodeableConcept(code, CodingSystem.LOINC, display, result);
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
        assertCodeableConcept(code, CodingSystem.LOINC, display, result);
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
          assertCoding("26436-6", CodingSystem.LOINC, result);
        }
      }

      @Nested
      class ObservationCategoryTest {
        @Test
        @DisplayName("category has fixed value: laboratory")
        void testCategoryCategory() {
          Coding result = laborbefund.getObservationCategoryCategory();
          assertCoding("laboratory", CodingSystem.OBSERVATION_CATEGORY_TERMINOLOGY, result);
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
          Laborbereich code = Laborbereich.CELL_MARKER;
          laborbefund.setLaborbereich_code(getCodeStr(code.getCode()));
          Coding result = laborbefund.getObservationCategoryBereich();
          assertCoding(code, result);
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
          Laborstruktur code = Laborstruktur.ZYTOLOGIE;
          laborbefund.setLaborgruppe_code(getCodeStr(code.getCode()));
          Coding result = laborbefund.getObservationCategoryGruppen();
          assertCoding(code, result);
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
        Observation.ObservationStatus status = Observation.ObservationStatus.FINAL;
        laborbefund.setLaboruntersuchung_status(getCodeStr(status.toCode()));
        Observation.ObservationStatus result = laborbefund.getObservationStatus();
        assertEquals(status, result);
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
            "laboratory",
            "http://terminology.hl7.org/CodeSystem/observation-category",
            null,
            result);
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
