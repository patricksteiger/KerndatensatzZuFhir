package basismodule;

import constants.CodingSystem;
import constants.ExtensionUrl;
import helper.Logger;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import valueSets.DurchfuehrungsabsichtCode;
import valueSets.ProcedureCategorySnomedMapping;
import valueSets.SeitenlokalisationCode;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

class ProzedurTest {
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

  @Nested
  class ProcedureTest {

    @Nested
    class StatusTest {
      @Test
      @DisplayName("status is fixed value: COMPLETED")
      void testStatus() {
        assertEquals(Procedure.ProcedureStatus.COMPLETED, prozedur.getStatus());
      }
    }

    @Nested
    class CategoryTest {
      @Test
      @DisplayName("empty OPS-Code should result in empty value")
      void testEmptyCategory() {
        assertEmptyCodeValue(prozedur::setOPS_Vollst_Prozedurenkode, prozedur::getCategory);
      }

      @Test
      @DisplayName("invalid OPS-Code should result in empty value")
      void testInvalidCategory() {
        String code = getCodeStr("invalid");
        prozedur.setOPS_Vollst_Prozedurenkode(code);
        assertEmptyValue(prozedur.getCategory());
      }

      @Test
      @DisplayName("valid OPS-Code should be present in CodeableConcept")
      void testValidCategory() {
        String code = getCodeStr("5-526.21");
        prozedur.setOPS_Vollst_Prozedurenkode(code);
        ProcedureCategorySnomedMapping expectedMapping = ProcedureCategorySnomedMapping.SURGICAL;
        CodeableConcept result = prozedur.getCategory();
        assertCodeableConcept(expectedMapping, result);
      }
    }

    @Nested
    class CodeTest {
      @Test
      @DisplayName("empty OPS- and SNOMED-Code should result in empty value")
      void testEmptyCode() {
        prozedur.setOPS_Vollst_Prozedurenkode("");
        prozedur.setSNOMED_Vollst_Prozedurenkode("");
        assertEmptyValue(prozedur.getCode());
      }

      @Test
      @DisplayName("non-empty OPS- and SNOMED-Code should be present in CodeableConcept")
      void testNonEmptyCode() {
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

      @Nested
      class CodingOpsTest {
        @Test
        @DisplayName("empty OPS-Code should result in empty value")
        void testEmptyOps() {
          assertEmptyCodeValue(prozedur::setOPS_Vollst_Prozedurenkode, prozedur::getCodingOps);
        }

        @Test
        @DisplayName("non-empty OPS-Code should be present in Coding")
        void testNonEmptyOps() {
          String code = "5-526.21";
          String display = "ops display";
          String opsCode = getCodeDisplayStr(code, display);
          prozedur.setOPS_Vollst_Prozedurenkode(opsCode);
          Coding result = prozedur.getCodingOps();
          assertCoding(code, CodingSystem.OPS_DIMDI, display, result);
          assertFalse(result.hasExtension());
        }

        @Test
        @DisplayName(
            "non-empty OPS-Seitenlokalisation-Code should be present in Extension of Coding")
        void testCodingOps() {
          String code = "5-526.21";
          String display = "ops display";
          String opsCode = getCodeDisplayStr(code, display);
          prozedur.setOPS_Vollst_Prozedurenkode(opsCode);
          SeitenlokalisationCode seitenlokalisationCode = SeitenlokalisationCode.RECHTS;
          String seitenlokalisation = getCodeDisplayStr(seitenlokalisationCode);
          prozedur.setOPS_Seitenlokalisation(seitenlokalisation);
          Coding result = prozedur.getCodingOps();
          assertEquals(1, result.getExtension().size());
        }

        @Nested
        class SeitenlokalisationTest {
          @Test
          @DisplayName("empty OPS-Seitenlokalisation-Code should result in empty value")
          void testEmptySeitenlokalisation() {
            assertEmptyCodeValue(
                prozedur::setOPS_Seitenlokalisation, prozedur::getSeitenlokalisation);
          }

          @Test
          @DisplayName("invalid OPS-Seitenlokalisation-Code should result in empty value")
          void testInvalidSeitenlokalisation() {
            prozedur.setOPS_Seitenlokalisation("invalid");
            assertEmptyValue(prozedur.getSeitenlokalisation());
          }

          @Test
          void testSeitenlokalisation() {
            SeitenlokalisationCode seitenlokalisationCode = SeitenlokalisationCode.LINKS;
            String seitenlokalisation = getCodeDisplayStr(seitenlokalisationCode);
            prozedur.setOPS_Seitenlokalisation(seitenlokalisation);
            Extension result = prozedur.getSeitenlokalisation();
            assertExtensionWithCoding(
                seitenlokalisationCode, ExtensionUrl.OPS_SEITENLOKALISATION, result);
          }
        }
      }

      @Nested
      class CodingSnomedTest {
        @Test
        @DisplayName("empty SNOMED-Code should result in empty value")
        void testEmptySnomed() {
          assertEmptyCodeValue(
              prozedur::setSNOMED_Vollst_Prozedurenkode, prozedur::getCodingSnomed);
        }

        @Test
        @DisplayName("non-empty SNOMED-Code should be present in Coding")
        void testNonEmptySnomed() {
          String code = "53124423";
          String display = "snomed display";
          String snomed = getCodeDisplayStr(code, display);
          prozedur.setSNOMED_Vollst_Prozedurenkode(snomed);
          Coding result = prozedur.getCodingSnomed();
          assertCoding(code, CodingSystem.SNOMED_CLINICAL_TERMS, display, result);
        }
      }
    }

    @Nested
    class PerformedTest {
      @Test
      @DisplayName("invalid Durchfuehrungsdatum should result in empty value")
      void testInvalidPerformed() {
        prozedur.setDurchfuehrungsdatum("invalid");
        assertEmptyValue(prozedur.getPerformed());
      }

      @Test
      @DisplayName("valid Durchfuehrungsdatum should be present in DateTimeType")
      void testValidPerformed() {
        String date = "2020-12-12";
        prozedur.setDurchfuehrungsdatum(date);
        DateTimeType result = prozedur.getPerformed();
        assertDateTimeType(expectedDateString(date), result);
      }
    }

    @Nested
    class BodySiteTest {
      @Test
      @DisplayName("empty Koerperstelle should result in empty value")
      void testEmptyBodySite() {
        assertEmptyCodeValue(prozedur::setKoerperstelle, prozedur::getBodySite);
      }

      @Test
      @DisplayName("non-empty Koerperstelle should be present in CodeableConcept")
      void testNonEmptyBodySite() {
        String code = "478923";
        String display = "koerper";
        String koerperstelle = getCodeDisplayStr(code, display);
        prozedur.setKoerperstelle(koerperstelle);
        CodeableConcept result = prozedur.getBodySite();
        assertCodeableConcept(code, CodingSystem.SNOMED_CLINICAL_TERMS, display, result);
      }
    }

    @Nested
    class NoteTest {
      @Test
      @DisplayName("empty Freitext should result in empty value")
      void testEmptyNote() {
        prozedur.setFreitextbeschreibung("");
        assertEmptyValue(prozedur.getNote());
      }

      @Test
      @DisplayName("non-empty Freitext should be present in Annotation")
      void testNonEmptyNote() {
        String freitext = "text";
        prozedur.setFreitextbeschreibung(freitext);
        Annotation result = prozedur.getNote();
        assertNonEmptyValue(result);
        assertEquals(freitext, result.getText());
      }
    }

    @Nested
    class RecordedDateTest {
      @Test
      @DisplayName("empty Dokumentationsdatum should result in empty value")
      void testEmptyRecordedDate() {
        prozedur.setDokumentationsdatum("");
        assertEmptyValue(prozedur.getRecordedDate());
      }

      @Test
      @DisplayName("invalid Dokumentationsdatum should result in empty value")
      void testInvalidRecordedDate() {
        prozedur.setDokumentationsdatum("invalid");
        assertEmptyValue(prozedur.getRecordedDate());
      }

      @Test
      @DisplayName("valid Dokumentationsdatum should be present in Extension")
      void testValidRecordedDate() {
        String date = "2021-01-14";
        prozedur.setDokumentationsdatum(date);
        prozedur.setDurchfuehrungsdatum("");
        Extension result = prozedur.getRecordedDate();
        assertNonEmptyValue(result);
        assertEquals(ExtensionUrl.RECORDED_DATE, result.getUrl());
        assertTrue(result.hasValue());
        assertTrue(result.getValue() instanceof DateTimeType);
        assertDateTimeType(expectedDateString(date), (DateTimeType) result.getValue());
      }

      @Test
      @DisplayName(
          "when Dokumentationsdatum and Durchfuehrungsdatum are equal, the Extension should be empty value")
      void testEqualRecordedDate() {
        String dokuDate = "2017-05-26";
        prozedur.setDokumentationsdatum(dokuDate);
        String durchDate = new String("2017-05-26");
        prozedur.setDurchfuehrungsdatum(durchDate);
        assertEmptyValue(prozedur.getRecordedDate());
      }
    }

    @Nested
    class DurchfuehrungsabsichtTest {
      @Test
      @DisplayName("empty Durchfuehrungsabsicht should result in empty value")
      void testEmptyDurchfuehrungsabsicht() {
        assertEmptyCodeValue(
            prozedur::setDurchfuehrungsabsicht, prozedur::getDurchfuehrungsabsicht);
      }

      @Test
      @DisplayName("invalid Durchfuehrungsabsicht should result in empty value")
      void testInvalidDurchfuehrungsabsicht() {
        prozedur.setDurchfuehrungsabsicht("invalid");
        assertEmptyValue(prozedur.getDurchfuehrungsabsicht());
      }

      @Test
      @DisplayName("valid Durchfuehrungsabsicht should be present in extension")
      void testValidDurchfuehrungsabsicht() {
        DurchfuehrungsabsichtCode durchfuehrungsabsichtCode = DurchfuehrungsabsichtCode.OTHER;
        String durchfuehrungsabsicht = getCodeDisplayStr(durchfuehrungsabsichtCode);
        prozedur.setDurchfuehrungsabsicht(durchfuehrungsabsicht);
        Extension result = prozedur.getDurchfuehrungsabsicht();
        assertExtensionWithCoding(
            durchfuehrungsabsichtCode, ExtensionUrl.DURCHFUEHRUNGSABSICHT, result);
      }
    }
  }
}
