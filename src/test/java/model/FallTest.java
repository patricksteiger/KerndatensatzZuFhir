package model;

import constants.CodingSystem;
import constants.IdentifierSystem;
import enums.*;
import helper.Logger;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

public class FallTest {
  private static Logger LOGGER;
  private Fall fall;

  @BeforeAll
  static void init() {
    LOGGER = Mockito.mock(Logger.class, Mockito.CALLS_REAL_METHODS);
    setUpLoggerMock(LOGGER);
  }

  @BeforeEach
  public void setUp() throws Exception {
    clearInvocations(LOGGER);
    fall = new Fall();
    setMockLoggerField(fall, LOGGER);
  }

  @Nested
  class AbteilungsEncounterTest {

    @Nested
    class PeriodTest {
      @Test
      @DisplayName("empty Beginndatum should result in empty Period")
      void testEmptyStartPeriod() {
        fall.setAbteilungskontakt_beginndatum("");
        assertEmptyValue(fall.getAbteilungsEncounterPeriod());
      }

      @Test
      @DisplayName("invalid Beginndatum should result in empty Period")
      void testInvalidStartPeriod() {
        fall.setAbteilungskontakt_beginndatum("invalid");
        assertEmptyValue(fall.getAbteilungsEncounterPeriod());
      }

      @Test
      @DisplayName("valid Beginndatum should be present in Period even if Enddatum is empty")
      void testValidStartEmptyEndPeriod() {
        fall.setAbteilungskontakt_enddatum("");
        String startDate = "2021-02-20";
        fall.setAbteilungskontakt_beginndatum(startDate);
        Period result = fall.getAbteilungsEncounterPeriod();
        assertPeriod(expectedDateString(startDate), null, result);
      }

      @Test
      @DisplayName("invalid Enddatum should result in empty Period")
      void testInvalidEnd() {
        String startDate = "2018-11-18";
        fall.setAbteilungskontakt_beginndatum(startDate);
        String endDate = "invalid date";
        fall.setAbteilungskontakt_enddatum(endDate);
        assertEmptyValue(fall.getAbteilungsEncounterPeriod());
      }

      @Test
      @DisplayName("valid Beginndatum and Enddatum should be present in Period")
      void testValidStartAndValidEndPeriod() {
        String startDate = "2020-12-10";
        fall.setAbteilungskontakt_beginndatum(startDate);
        String endDate = "2021-02-23";
        fall.setAbteilungskontakt_enddatum(endDate);
        Period result = fall.getAbteilungsEncounterPeriod();
        assertPeriod(expectedDateString(startDate), expectedDateString(endDate), result);
      }
    }

    @Nested
    class ServiceTypeTest {
      @Test
      @DisplayName("empty Fachabteilungsschlüssel should result in empty CodeableConcept")
      void testEmptyServiceType() {
        assertEmptyCodeValue(
            fall::setAbteilungskontakt_fachabteilungsschluessel,
            fall::getAbteilungsEncounterServiceType);
      }

      @Test
      @DisplayName(
          "Fachabteilungsschlüssel not present in incomplete ValueSet should still be present in CodeableConcept")
      void testInvalidServiceType() {
        String code = "invalid";
        String display = "invalid display";
        String schluessel = getCodeDisplayStr(code, display);
        fall.setAbteilungskontakt_fachabteilungsschluessel(schluessel);
        CodeableConcept result = fall.getAbteilungsEncounterServiceType();
        assertCodeableConcept(code, CodingSystem.FALL_FACHABTEILUNGSSCHLUESSEL, display, result);
      }

      @Test
      @DisplayName("valid Fachabteilungsschlüssel should be present in CodeableConcept")
      void testValidServiceType() {
        Fachabteilung fachabteilung = Fachabteilung.AUGENHEILKUNDE;
        String code = fachabteilung.getCode();
        String display = fachabteilung.getDisplay();
        String schluessel = getCodeDisplayStr(code, display);
        fall.setAbteilungskontakt_fachabteilungsschluessel(schluessel);
        CodeableConcept result = fall.getAbteilungsEncounterServiceType();
        assertCodeableConcept(fachabteilung, result);
      }
    }

    @Nested
    class TypeTest {
      @Test
      @DisplayName("empty Ebene should result in empty CodeableConcept")
      void testEmptyType() {
        assertEmptyCodeValue(fall::setAbteilungskontakt_ebene, fall::getAbteilungsEncounterType);
      }

      @Test
      @DisplayName("non-empty Ebene should be present in CodeableConcept")
      void testNonEmptyType() {
        String code = "abteilungskontakt";
        String ebene = getCodeStr(code);
        fall.setAbteilungskontakt_ebene(ebene);
        CodeableConcept result = fall.getAbteilungsEncounterType();
        assertCodeableConcept(Kontaktebene.ABTEILUNG, result);
      }
    }

    @Nested
    class IdentifierTest {
      @Test
      @DisplayName("empty Aufnahmenummer should result in empty Identifier")
      void testEmptyIdentifier() {
        fall.setAbteilungskontakt_aufnahmenummer(null);
        assertEmptyValue(fall.getAbteilungsEncounterIdentifier());
        fall.setAbteilungskontakt_aufnahmenummer("");
        assertEmptyValue(fall.getAbteilungsEncounterIdentifier());
      }

      @Test
      @DisplayName("non-empty Aufnahmenummer should be present in Identifier")
      void testNonEmptyIdentifier() {
        String aufnahmenummer = "1a2b";
        fall.setAbteilungskontakt_aufnahmenummer(aufnahmenummer);
        Identifier result = fall.getAbteilungsEncounterIdentifier();
        assertIdentifier(
            aufnahmenummer, IdentifierSystem.ACME_PATIENT, IdentifierTypeCode.VN, result);
      }
    }

    @Nested
    class StatusTest {
      @Test
      @DisplayName("status is fixed value: FINISHED")
      void testStatus() {
        assertEquals(Encounter.EncounterStatus.FINISHED, fall.getAbteilungsEncounterStatus());
      }
    }

    @Nested
    class ClassTest {
      @Test
      @DisplayName("empty Klasse should result in empty Coding")
      void testEmptyClass() {
        assertEmptyCodeValue(fall::setAbteilungskontakt_klasse, fall::getAbteilungsEncounterClass);
      }

      @Test
      @DisplayName("non-empty Klasse should be present in Coding")
      void testClass() {
        String code = "1a2b";
        String display = "correct display";
        String klasse = getCodeDisplayStr(code, display);
        fall.setAbteilungskontakt_klasse(klasse);
        Coding result = fall.getAbteilungsEncounterClass();
        assertCoding(code, CodingSystem.ENCOUNTER_CLASS_DE, display, result);
      }
    }
  }

  @Nested
  class EinrichtungsEncounterTest {

    @Nested
    class IdentifierTest {
      @Test
      @DisplayName("empty Aufnahmenummer should result in empty Identifier")
      void testEmptyIdentifier() {
        fall.setEinrichtungskontakt_aufnahmenummer("");
        assertEmptyValue(fall.getEinrichtungsEncounterIdentifier());
        fall.setEinrichtungskontakt_aufnahmenummer(null);
        assertEmptyValue(fall.getEinrichtungsEncounterIdentifier());
      }

      @Test
      @DisplayName("non-empty Aufnahmenummer should be present in Identifier")
      void testNonEmptyIdentifier() {
        String aufnahmenummer = "123";
        fall.setEinrichtungskontakt_aufnahmenummer(aufnahmenummer);
        Identifier result = fall.getEinrichtungsEncounterIdentifier();
        assertIdentifier(
            aufnahmenummer, IdentifierSystem.ACME_PATIENT, IdentifierTypeCode.VN, result);
      }
    }

    @Nested
    class StatusTest {
      @Test
      @DisplayName("status is fixed value: FINISHED")
      void testStatus() {
        assertEquals(Encounter.EncounterStatus.FINISHED, fall.getEinrichtungsEncounterStatus());
      }
    }

    @Nested
    class ClassTest {
      @Test
      @DisplayName("empty Klasse should result in empty Coding")
      void testEmptyClass() {
        fall.setEinrichtungskontakt_klasse(null);
        assertEmptyValue(fall.getEinrichtungsEncounterClass());
        fall.setEinrichtungskontakt_klasse("");
        assertEmptyValue(fall.getEinrichtungsEncounterClass());
      }

      @Test
      @DisplayName("non-empty Klasse should be present in Coding")
      void testNonEmptyClass() {
        String code = "12345";
        String display = "very good klasse";
        String klasse = getCodeDisplayStr(code, display);
        fall.setEinrichtungskontakt_klasse(klasse);
        Coding result = fall.getEinrichtungsEncounterClass();
        assertCoding(code, CodingSystem.ENCOUNTER_CLASS_DE, display, result);
      }
    }

    @Nested
    class TypeTest {
      @Test
      @DisplayName("empty Ebene should result in empty CodeableConcept")
      void testEmptyType() {
        fall.setEinrichtungskontakt_ebene(null);
        assertEmptyValue(fall.getEinrichtungsEncounterType());
        fall.setEinrichtungskontakt_ebene("");
        assertEmptyValue(fall.getEinrichtungsEncounterType());
      }

      @Test
      @DisplayName("non-empty Ebene should be present in CodeableConcept")
      void testNonEmptyType() {
        String code = "einrichtungskontakt";
        String ebene = getCodeStr(code);
        fall.setEinrichtungskontakt_ebene(ebene);
        CodeableConcept result = fall.getEinrichtungsEncounterType();
        assertCodeableConcept(Kontaktebene.EINRICHTUNG, result);
      }
    }

    @Nested
    class ServiceTypeTest {
      @Test
      @DisplayName("serviceType is fixed value: 0100 (Innere Medizin)")
      void testServiceType() {
        assertCodeableConcept(
            Fachabteilung.INNERE_MEDIZIN, fall.getEinrichtungsEncounterServiceType());
      }
    }

    @Nested
    class ReasonCodeTest {
      @Test
      @DisplayName("empty Aufnahmegrund should result in empty CodeableConcept")
      void testEmptyReasonCode() {
        assertEmptyCodeValue(
            fall::setEinrichtungskontakt_aufnahmegrund, fall::getEinrichtungsEncounterReasonCode);
      }

      @Test
      @DisplayName("invalid Aufnahmegrund should result in empty CodeableConcept")
      void testInvalidReasonCode() {
        String code = getCodeStr("invalid");
        fall.setEinrichtungskontakt_aufnahmegrund(code);
        assertEmptyValue(fall.getEinrichtungsEncounterReasonCode());
      }

      @Test
      @DisplayName("valid Aufnahmegrund should be present in CodeableConcept")
      void testValidReasonCode() {
        Aufnahmegrund aufnahmegrund = Aufnahmegrund.G01;
        String code = getCodeStr(aufnahmegrund.getCode());
        fall.setEinrichtungskontakt_aufnahmegrund(code);
        CodeableConcept result = fall.getEinrichtungsEncounterReasonCode();
        assertCodeableConcept(aufnahmegrund, result);
      }
    }

    @Nested
    class PeriodTest {
      @Test
      @DisplayName("empty Beginndatum should result in empty period")
      void testEmptyStart() {
        fall.setEinrichtungskontakt_beginndatum("");
        assertEmptyValue(fall.getEinrichtungsEncounterPeriod());
      }

      @Test
      @DisplayName("valid Beginndatum should be present in Period, even if Enddatum is empty")
      void testValidStartAndEmptyEnd() {
        String startDate = "2021-03-15";
        fall.setEinrichtungskontakt_beginndatum(startDate);
        Period result = fall.getEinrichtungsEncounterPeriod();
        assertPeriod(expectedDateString(startDate), null, result);
      }

      @Test
      @DisplayName("valid Beginndatum and Enddatum should be present in Period")
      void testValidStartAndEnd() {
        String startDate = "2020-09-19";
        fall.setEinrichtungskontakt_beginndatum(startDate);
        String endDate = "2021-04-17";
        fall.setEinrichtungskontakt_enddatum(endDate);
        Period result = fall.getEinrichtungsEncounterPeriod();
        assertPeriod(expectedDateString(startDate), expectedDateString(endDate), result);
      }

      @Test
      @DisplayName("empty Beginndatum and valid Enddatum should result in empty Period")
      void testPeriod() {
        fall.setEinrichtungskontakt_beginndatum("");
        String endDate = "2021-04-17";
        fall.setEinrichtungskontakt_enddatum(endDate);
        Period result = fall.getEinrichtungsEncounterPeriod();
        assertEmptyValue(result);
      }
    }

    @Nested
    class HospitalizationTest {
      @Test
      @DisplayName(
          "empty Aufnahmeanlass and Entlassungsgrund should not be present in EncounterHospitalizationComponent")
      void testEmptyHospitalization() {
        fall.setEinrichtungskontakt_aufnahmeanlass("");
        fall.setEinrichtungskontakt_entlassungsgrund("");
        Encounter.EncounterHospitalizationComponent result =
            fall.getEinrichtungsEncounterHospitalization();
        assertFalse(result.hasAdmitSource());
        assertFalse(result.hasDischargeDisposition());
      }

      @Test
      @DisplayName(
          "valid Aufnahmeanlass and Entlassungsgrund should be present in EncounterHospitalizationComponent")
      void testHospitalization() {
        Aufnahmeanlass aufnahmeanlass = Aufnahmeanlass.GEBURT;
        Entlassungsgrund entlassungsgrund = Entlassungsgrund.G011;
        fall.setEinrichtungskontakt_aufnahmeanlass(getCodeDisplayStr(aufnahmeanlass));
        fall.setEinrichtungskontakt_entlassungsgrund(getCodeDisplayStr(entlassungsgrund));
        Encounter.EncounterHospitalizationComponent result =
            fall.getEinrichtungsEncounterHospitalization();
        assertTrue(result.hasAdmitSource());
        assertTrue(result.hasDischargeDisposition());
      }

      @Nested
      class DischargeDispositionTest {
        @Test
        @DisplayName("empty Entlassungsgrund results in empty CodeableConcept")
        void testEmptyDischargeDisposition() {
          assertEmptyCodeValue(
              fall::setEinrichtungskontakt_entlassungsgrund,
              fall::getEinrichtungsEncounterDischargeDisposition);
        }

        @Test
        @DisplayName("invalid Entlassungsgrund results in empty CodeableConcept")
        void testInvalidDischargeDisposition() {
          String entlassungsgrund = getCodeDisplayStr("invalid", "failed display");
          fall.setEinrichtungskontakt_entlassungsgrund(entlassungsgrund);
          assertEmptyValue(fall.getEinrichtungsEncounterDischargeDisposition());
        }

        @Test
        @DisplayName("valid Entlassungsgrund should be present in CodeableConcept")
        void testDischargeDisposition() {
          Entlassungsgrund grund = Entlassungsgrund.G15;
          String entlassungsgrund = getCodeDisplayStr(grund);
          fall.setEinrichtungskontakt_entlassungsgrund(entlassungsgrund);
          CodeableConcept result = fall.getEinrichtungsEncounterDischargeDisposition();
          assertCodeableConcept(grund, result);
        }
      }

      @Nested
      class AdmitSourceTest {
        @Test
        @DisplayName("empty Aufnahmeanlass should result in empty CodeableConcept")
        void testEmptyAdmitSource() {
          assertEmptyCodeValue(
              fall::setEinrichtungskontakt_aufnahmeanlass,
              fall::getEinrichtungsEncounterAdmitSource);
        }

        @Test
        @DisplayName("invalid Aufnahmeanlass should result in empty CodeableConcept")
        void testInvalidAdmitSource() {
          String anlass = getCodeDisplayStr("invalid", "invalid display");
          fall.setEinrichtungskontakt_aufnahmeanlass(anlass);
          assertEmptyValue(fall.getEinrichtungsEncounterAdmitSource());
        }

        @Test
        @DisplayName("valid Aufnahmeanlass should be present in CodeableConcept")
        void testAdmitSource() {
          Aufnahmeanlass aufnahmeanlass = Aufnahmeanlass.NOTFALL;
          String anlass = getCodeDisplayStr(aufnahmeanlass);
          fall.setEinrichtungskontakt_aufnahmeanlass(anlass);
          CodeableConcept result = fall.getEinrichtungsEncounterAdmitSource();
          assertCodeableConcept(aufnahmeanlass, result);
        }
      }
    }
  }

  @Nested
  class VersorgungsstellenEncounterTest {

    @Nested
    class IdentifierTest {
      @Test
      @DisplayName("empty Aufnahmenummer should result in empty Identifier")
      void testEmptyIdentifier() {
        fall.setVersorgungsstellenkontakt_aufnahmenummer(null);
        assertEmptyValue(fall.getVersorgungsstellenEncounterIdentifier());
        fall.setVersorgungsstellenkontakt_aufnahmenummer("");
        assertEmptyValue(fall.getVersorgungsstellenEncounterIdentifier());
      }

      @Test
      @DisplayName("non-empty Aufnahmenummer should be present in Identifier")
      void testIdentifier() {
        String aufnahmenummer = "1a2b";
        fall.setVersorgungsstellenkontakt_aufnahmenummer(aufnahmenummer);
        Identifier result = fall.getVersorgungsstellenEncounterIdentifier();
        assertIdentifier(
            aufnahmenummer, IdentifierSystem.ACME_PATIENT, IdentifierTypeCode.VN, result);
      }
    }

    @Nested
    class StatusTest {
      @Test
      @DisplayName("status is fixed value: FINISHED")
      void testStatus() {
        assertEquals(
            Encounter.EncounterStatus.FINISHED, fall.getVersorgungsstellenEncounterStatus());
      }
    }

    @Nested
    class ClassTest {
      @Test
      @DisplayName("empty Klasse should result in empty Coding")
      void testEmptyClass() {
        assertEmptyCodeValue(
            fall::setVersorgungsstellenkontakt_klasse, fall::getVersorgungsstellenEncounterClass);
      }

      @Test
      @DisplayName("non-empty Klasse should be present in Coding")
      void testClass() {
        String code = "1a2c";
        String display = "display";
        String klasse = getCodeDisplayStr(code, display);
        fall.setVersorgungsstellenkontakt_klasse(klasse);
        Coding result = fall.getVersorgungsstellenEncounterClass();
        assertCoding(code, CodingSystem.ENCOUNTER_CLASS_DE, display, result);
      }
    }

    @Nested
    class TypeTest {
      @Test
      @DisplayName("empty Ebene should result in empty CodeableConcept")
      void testEmptyClass() {
        assertEmptyCodeValue(
            fall::setVersorgungsstellenkontakt_ebene, fall::getVersorgungsstellenEncounterType);
      }

      @Test
      @DisplayName("non-empty Ebene should be present in CodeacleConcept")
      void testNonEmptyClass() {
        String code = "versorgungsstellenkontakt";
        String ebene = getCodeStr(code);
        fall.setVersorgungsstellenkontakt_ebene(ebene);
        CodeableConcept result = fall.getVersorgungsstellenEncounterType();
        assertCodeableConcept(Kontaktebene.VERSORGUNGSSTELLE, result);
      }
    }

    @Nested
    class PeriodTest {
      @Test
      @DisplayName("empty Beginndatum should result in empty Period")
      void testEmptyStart() {
        fall.setVersorgungsstellenkontakt_beginndatum("");
        assertEmptyValue(fall.getVersorgungsstellenEncounterPeriod());
      }

      @Test
      @DisplayName("invalid Beginndatum should result in empty Period")
      void testInvalidStart() {
        fall.setVersorgungsstellenkontakt_beginndatum("invalid");
        assertEmptyValue(fall.getVersorgungsstellenEncounterPeriod());
      }

      @Test
      @DisplayName("valid Beginndatum should be present in Period, even if Enddatum is empty")
      void testValidStartAndEmptyEnd() {
        fall.setVersorgungsstellenkontakt_enddatum("");
        String startDate = "2021-02-04";
        fall.setVersorgungsstellenkontakt_beginndatum(startDate);
        Period result = fall.getVersorgungsstellenEncounterPeriod();
        assertPeriod(expectedDateString(startDate), null, result);
      }

      @Test
      @DisplayName("valid Beginndatum and Enddatum should be present in Period")
      void testPeriod() {
        String startDate = "2021-03-04";
        fall.setVersorgungsstellenkontakt_beginndatum(startDate);
        String endDate = "2021-03-14";
        fall.setVersorgungsstellenkontakt_enddatum(endDate);
        Period result = fall.getVersorgungsstellenEncounterPeriod();
        assertPeriod(expectedDateString(startDate), expectedDateString(endDate), result);
      }
    }
  }
}
