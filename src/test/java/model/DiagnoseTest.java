package model;

import constants.CodingSystem;
import constants.ExtensionUrl;
import enums.*;
import helper.Logger;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

class DiagnoseTest {
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

  @Nested
  class ConditionTest {

    @Nested
    class CodeTest {
      @Test
      @DisplayName("no codes set should result in empty value")
      void testNoCodes() {
        diagnose.setIcd_diagnosecode("");
        diagnose.setAlpha_diagnosecode("");
        diagnose.setSnomed_diagnosecode("");
        diagnose.setOrphanet_diagnosecode("");
        diagnose.setWeitere_diagnosecode("");
        assertEmptyValue(diagnose.getCode());
      }

      @Test
      @DisplayName("non-empty Alpha- and SNOMED-Code should be present in CodeableConcept")
      void testAplhaAndSnomed() {
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

      @Nested
      class CodeIcdTest {
        @Test
        @DisplayName("empty Diagnosecode should result in empty value")
        void testEmptyDiagnosecode() {
          assertEmptyCodeValue(diagnose::setIcd_diagnosecode, diagnose::getCodeIcd);
        }

        @Test
        @DisplayName("non-empty Diagnosecode should be present in Coding")
        void testNonEmptyDiagnosecode() {
          String code = "4325234";
          String display = "icd code";
          String diagnosecode = getCodeDisplayStr(code, display);
          diagnose.setIcd_diagnosecode(diagnosecode);
          Coding result = diagnose.getCodeIcd();
          assertCoding(code, CodingSystem.ICD_10_GM_DIMDI, display, result);
          assertFalse(result.hasExtension());
        }

        @Test
        @DisplayName("non-empty Seitenlokalisation should be present in Extension of ICD-Coding")
        void testExtensionDiagnosecode() {
          String code = "4379834";
          String display = "icd code";
          String diagnosecode = getCodeDisplayStr(code, display);
          diagnose.setIcd_diagnosecode(diagnosecode);
          String seitenlokalisation = getCodeDisplayStr(ICD_Seitenlokalisation.LINKS);
          diagnose.setIcd_seitenlokalisation(seitenlokalisation);
          Coding result = diagnose.getCodeIcd();
          assertCoding(code, CodingSystem.ICD_10_GM_DIMDI, display, result);
          assertEquals(1, result.getExtension().size());
          assertTrue(result.hasExtension(ExtensionUrl.ICD_10_GM_SEITENLOKALISATION));
        }

        @Nested
        class DiagnosesicherheitTest {
          @Test
          @DisplayName("empty Diagnosesicherheit should result in empty value")
          void testEmptyCodeIcdSicherheit() {
            assertEmptyCodeValue(
                diagnose::setIcd_diagnosesicherheit, diagnose::getCodeIcdDiagnosesicherheit);
          }

          @Test
          @DisplayName("invalid Diagnosesicherheit should result in empty value")
          void testInvalidCodeIcdSicherheit() {
            diagnose.setIcd_diagnosesicherheit("invalid");
            assertEmptyValue(diagnose.getCodeIcdDiagnosesicherheit());
          }

          @Test
          @DisplayName("valid Diagnosesicherheit should be present in Extension")
          void testValidCodeIcdSicherheit() {
            Diagnosesicherheit diagnosesicherheitCode = Diagnosesicherheit.SUSPECTED;
            String diagnosesicherheit = getCodeDisplayStr(diagnosesicherheitCode);
            diagnose.setIcd_diagnosesicherheit(diagnosesicherheit);
            Extension result = diagnose.getCodeIcdDiagnosesicherheit();
            ICD_Diagnosesicherheit icdDiagnosesicherheitCode =
                diagnosesicherheitCode.getIcdMapping();
            assertExtensionWithCoding(
                icdDiagnosesicherheitCode, ExtensionUrl.ICD_10_GM_DIAGNOSESEICHERHEIT, result);
          }
        }

        @Nested
        class SeitenlokalisationTest {
          @Test
          @DisplayName("empty Seitenlokalisation should result in empty value")
          void testEmptySeitenlokalisation() {
            assertEmptyCodeValue(
                diagnose::setIcd_seitenlokalisation, diagnose::getCodeIcdSeitenlokalisation);
          }

          @Test
          @DisplayName("invalid Seitenlokalisation should result in empty value")
          void testInvalidSeitenlokalisation() {
            diagnose.setIcd_seitenlokalisation("invalid");
            assertEmptyValue(diagnose.getCodeIcdSeitenlokalisation());
          }

          @Test
          @DisplayName("valid Seitenlokalisation should be present in Extension")
          void testCodeIcdSeitenlokalisation() {
            ICD_Seitenlokalisation icdSeitenlokalisation = ICD_Seitenlokalisation.BEIDSEITIG;
            String seitenlokalisation = getCodeDisplayStr(icdSeitenlokalisation);
            diagnose.setIcd_seitenlokalisation(seitenlokalisation);
            Extension result = diagnose.getCodeIcdSeitenlokalisation();
            assertExtensionWithCoding(
                icdSeitenlokalisation, ExtensionUrl.ICD_10_GM_SEITENLOKALISATION, result);
          }
        }

        @Nested
        class AusrufezeichenTest {
          @Test
          @DisplayName("empty Ausrufezeichen should result in empty value")
          void testEmptyAusrufezeichen() {
            assertEmptyCodeValue(
                diagnose::setIcd_ausrufezeichencode, diagnose::getCodeIcdAusrufezeichen);
          }

          @Test
          @DisplayName("non-empty Ausrufezeichen should be present in Extension")
          void testNonEmptyAusrufezeichen() {
            String code = "5h52d7";
            String display = "display";
            String ausrufezeichen = getCodeDisplayStr(code, display);
            diagnose.setIcd_ausrufezeichencode(ausrufezeichen);
            Extension result = diagnose.getCodeIcdAusrufezeichen();
            assertExtensionWithCoding(
                code,
                CodingSystem.ICD_10_GM_DIMDI,
                display,
                ExtensionUrl.ICD_10_GM_AUSRUFEZEICHEN,
                result);
          }
        }

        @Nested
        class ManifestationscodeTest {
          @Test
          @DisplayName("empty Manifestationscode should result in empty value")
          void testEmptyManifestationscode() {
            assertEmptyCodeValue(
                diagnose::setIcd_manifestationscode, diagnose::getCodeIcdManifestationscode);
          }

          @Test
          @DisplayName("non-empty Manifestationscode should be present in Extension")
          void testNonEmptyManifestationscode() {
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
        }

        @Nested
        class Primaercode {
          @Test
          @DisplayName("empty Primaercode should result in empty value")
          void testEmptyPrimaercode() {
            assertEmptyCodeValue(diagnose::setIcd_primaercode, diagnose::getCodeIcdPrimaercode);
          }

          @Test
          @DisplayName("non-empty Primaercode should be present in Extension")
          void testNonEmptyPrimaercode() {
            String code = "3542";
            String display = "primaer display";
            String primaercode = getCodeDisplayStr(code, display);
            diagnose.setIcd_primaercode(primaercode);
            Extension result = diagnose.getCodeIcdPrimaercode();
            assertExtensionWithCoding(
                code,
                CodingSystem.ICD_10_GM_DIMDI,
                display,
                ExtensionUrl.ICD_10_GM_PRIMAERCODE,
                result);
          }
        }
      }

      @Nested
      class Alpha {
        @Test
        @DisplayName("empty Diagnosecode should result in empty value")
        void testEmptyDiagnosecode() {
          assertEmptyCodeValue(diagnose::setAlpha_diagnosecode, diagnose::getCodeAlpha);
        }

        @Test
        @DisplayName("non-empty Diagnosecode should be present in Coding")
        void testNonEmptyDiagnosecode() {
          String code = "h34jk2";
          String display = "alpha display";
          String diagnosecode = getCodeDisplayStr(code, display);
          diagnose.setAlpha_diagnosecode(diagnosecode);
          Coding result = diagnose.getCodeAlpha();
          assertCoding(code, CodingSystem.ALPHA_ID_DIMDI, display, result);
        }
      }

      @Nested
      class CodeSctTest {
        @Test
        @DisplayName("empty SNOMED-Code should result in empty value")
        void testEmptySnomed() {
          assertEmptyCodeValue(diagnose::setSnomed_diagnosecode, diagnose::getCodeSct);
        }

        @Test
        @DisplayName("non-empty SNOMED-Code should be present in Coding")
        void testNonEmptySnomed() {
          String code = "5478923";
          String display = "snomed display";
          String snomed = getCodeDisplayStr(code, display);
          diagnose.setSnomed_diagnosecode(snomed);
          Coding result = diagnose.getCodeSct();
          assertCoding(code, CodingSystem.SNOMED_CLINICAL_TERMS, display, result);
        }
      }

      @Nested
      class CodeOrphanetTest {
        @Test
        @DisplayName("empty Diagnosecode should result in empty value")
        void testEmptyOrphanet() {
          assertEmptyCodeValue(diagnose::setOrphanet_diagnosecode, diagnose::getCodeOrphanet);
        }

        @Test
        @DisplayName("non-empty Diagnosecode should be present in Coding")
        void testNonEmptyOrphanet() {
          String code = "532k-24";
          String display = "orphanet display";
          String orphanet = getCodeDisplayStr(code, display);
          diagnose.setOrphanet_diagnosecode(orphanet);
          Coding result = diagnose.getCodeOrphanet();
          assertCoding(code, CodingSystem.ORPHANET, display, result);
        }
      }

      @Nested
      class CodeWeitereTest {
        @Test
        @DisplayName("empty Diagnosecode should result in empty value")
        void testEmptyWeitere() {
          assertEmptyCodeValue(diagnose::setWeitere_diagnosecode, diagnose::getCodeWeitere);
        }

        @Test
        @DisplayName("non-empty Diagnosecode should be present in Coding")
        void testCodeWeitere() {
          String code = "43252";
          String display = "display";
          String system = "custom system";
          String diagnosecode = getCodeDisplayStr(code, display) + " system=\"" + system + "\"";
          diagnose.setWeitere_diagnosecode(diagnosecode);
          Coding result = diagnose.getCodeWeitere();
          assertCoding(code, system, display, result);
        }
      }
    }

    @Nested
    class NoteTest {
      @Test
      @DisplayName("empty Erlaeuterung should result in empty value")
      void testEmptyNote() {
        diagnose.setDiagnoseerlaeuterung("");
        assertEmptyValue(diagnose.getNote());
      }

      @Test
      @DisplayName("non-empty Erlaeuterung should be present in Annotation")
      void testNote() {
        // non-empty erlaeuterung
        String erlaeuterung = "some text";
        diagnose.setDiagnoseerlaeuterung(erlaeuterung);
        Annotation result = diagnose.getNote();
        assertEquals(erlaeuterung, result.getText());
      }
    }

    @Nested
    class RecordedDateTest {
      @Test
      @DisplayName("invalid Dokumentationsdatum should result in empty value")
      void testInvalidDate() {
        diagnose.setDokumentationsdatum("");
        assertEmptyValue(diagnose.getRecordedDate());
        diagnose.setDokumentationsdatum("invalid");
        assertEmptyValue(diagnose.getRecordedDate());
      }

      @Test
      @DisplayName("valid Dokumentationsdatum should be present in Date")
      void testValidDate() {
        String date = "2020-11-24";
        diagnose.setDokumentationsdatum(date);
        assertEquals(expectedDateString(date), diagnose.getRecordedDate());
      }
    }

    @Nested
    class OnsetTest {
      @Test
      @DisplayName("empty Zeitraum and Lebensphase should result in empty value")
      void testEmptyOnset() {
        diagnose.setZeitraum_bis("");
        diagnose.setZeitraum_von("");
        diagnose.setLebensphase_von("");
        diagnose.setLebensphase_bis("");
        assertEmptyValue(diagnose.getOnset());
      }

      @Test
      @DisplayName("only LebensphaseVon should be present in DateTimeType")
      void testOnlyLVon() {
        diagnose.setZeitraum_bis("");
        diagnose.setZeitraum_von("");
        diagnose.setLebensphase_bis("");
        KBVBaseStageLife vonStageLife = KBVBaseStageLife.NEONATAL;
        String lebensphaseVon = getCodeDisplayStr(vonStageLife);
        diagnose.setLebensphase_von(lebensphaseVon);
        Type result = diagnose.getOnset();
        assertNonEmptyValue(result);
        assertTrue(result instanceof DateTimeType);
        assertFalse(((DateTimeType) result).hasValue());
        assertTrue(result.hasExtension(ExtensionUrl.STAGE_LIFE));
        assertEquals(1, result.getExtension().size());
      }

      @Test
      @DisplayName("only ZeitraumVon and LebensphaseVon should be present in DateTimeType")
      void testOnlyZVonAndLVon() {
        diagnose.setZeitraum_bis("");
        diagnose.setLebensphase_bis("");
        String startDate = "2021-01-16";
        diagnose.setZeitraum_von(startDate);
        KBVBaseStageLife vonStageLife = KBVBaseStageLife.NEONATAL;
        String lebensphaseVon = getCodeDisplayStr(vonStageLife);
        diagnose.setLebensphase_von(lebensphaseVon);
        Type result = diagnose.getOnset();
        assertNonEmptyValue(result);
        assertTrue(result instanceof DateTimeType);
        assertEquals(expectedDateString(startDate), ((DateTimeType) result).getValue());
        assertTrue(result.hasExtension(ExtensionUrl.STAGE_LIFE));
        assertEquals(1, result.getExtension().size());
      }

      @Test
      @DisplayName("valid ZeitraumVon, LebensphaseVon and ZeitraumBis should be present in Period")
      void testAll() {
        String startDate = "2021-01-16";
        diagnose.setZeitraum_von(startDate);
        String endDate = "2021-02-07";
        diagnose.setZeitraum_bis(endDate);
        KBVBaseStageLife vonStageLife = KBVBaseStageLife.NEONATAL;
        String lebensphaseVon = getCodeDisplayStr(vonStageLife);
        diagnose.setLebensphase_von(lebensphaseVon);
        Type result = diagnose.getOnset();
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

      @Nested
      class LebensphaseVonTest {
        @Test
        @DisplayName("empty LebensphaseVon should result in empty value")
        void testEmptyVon() {
          assertEmptyCodeValue(diagnose::setLebensphase_von, diagnose::getLebensphaseVon);
        }

        @Test
        @DisplayName("invalid LebensphaseVon should result in empty value")
        void testInvalidVon() {
          diagnose.setLebensphase_von("invalid");
          assertEmptyValue(diagnose.getLebensphaseVon());
        }

        @Test
        @DisplayName("valid LebensphaseVon should be present in Extension")
        void testLebensphaseVon() {
          KBVBaseStageLife vonStageLife = KBVBaseStageLife.INFANCY;
          String vonCode = getCodeDisplayStr(vonStageLife);
          diagnose.setLebensphase_von(vonCode);
          Extension result = diagnose.getLebensphaseVon();
          assertExtensionWithCodeableConcept(vonStageLife, ExtensionUrl.STAGE_LIFE, result);
        }
      }

      @Nested
      class LebensphaseBis {
        @Test
        @DisplayName("empty LebensphaseBis should result in empty value")
        void testEmptyBis() {
          assertEmptyCodeValue(diagnose::setLebensphase_bis, diagnose::getLebensphaseBis);
        }

        @Test
        @DisplayName("invalid LebensphaseBis should result in empty value")
        void testInvalidBis() {
          diagnose.setLebensphase_bis("invalid");
          assertEmptyValue(diagnose.getLebensphaseBis());
        }

        @Test
        void testLebensphaseBis() {
          KBVBaseStageLife bisStageLife = KBVBaseStageLife.INFANCY;
          String bisCode = getCodeDisplayStr(bisStageLife);
          diagnose.setLebensphase_von(bisCode);
          Extension result = diagnose.getLebensphaseVon();
          assertExtensionWithCodeableConcept(bisStageLife, ExtensionUrl.STAGE_LIFE, result);
        }
      }
    }

    @Nested
    class BodySiteTest {
      @Test
      @DisplayName("empty Koerperstelle should result in empty value")
      void testEmptyBodySite() {
        assertEmptyCodeValue(diagnose::setKoerperstelle, diagnose::getBodySite);
      }

      @Test
      @DisplayName("non-empty Koerperstelle should be present in CodeableConcept")
      void testBodySite() {
        String code = "4789231";
        String display = "somewhere on the body";
        String koerperstelle = getCodeDisplayStr(code, display);
        diagnose.setKoerperstelle(koerperstelle);
        CodeableConcept result = diagnose.getBodySite();
        assertCodeableConcept(code, CodingSystem.SNOMED_CLINICAL_TERMS, display, result);
      }
    }

    @Nested
    class ClinicalStatusTest {
      @Test
      @DisplayName("empty Status should result in empty value")
      void testEmptyStatus() {
        assertEmptyCodeValue(diagnose::setKlinischer_status, diagnose::getClinicalStatus);
      }

      @Test
      @DisplayName("invalid Status should result in empty value")
      void testInvalidStatus() {
        diagnose.setKlinischer_status("invalid status");
        assertEmptyValue(diagnose.getClinicalStatus());
      }

      @Test
      @DisplayName("valid Status should be present in CodeableConcept")
      void testClinicalStatus() {
        ClinicalStatus clinicalStatus = ClinicalStatus.ACTIVE;
        String status = getCodeDisplayStr(clinicalStatus);
        diagnose.setKlinischer_status(status);
        CodeableConcept result = diagnose.getClinicalStatus();
        assertCodeableConcept(clinicalStatus, result);
      }
    }
  }
}
