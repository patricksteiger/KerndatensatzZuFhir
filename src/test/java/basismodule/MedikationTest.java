package basismodule;

import constants.IdentifierSystem;
import helper.Logger;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

public class MedikationTest {
  private static final String ATC_DE_SYSTEM = "http://fhir.de/CodeSystem/dimdi/atc";
  private static final String PZN_SYSTEM = "http://fhir.de/CodeSystem/ifa/pzn";
  private static Logger LOGGER;
  private Medikation medikation;

  @BeforeAll
  static void init() {
    LOGGER = Mockito.mock(Logger.class, Mockito.CALLS_REAL_METHODS);
    setUpLoggerMock(LOGGER);
  }

  @BeforeEach
  public void setUp() {
    clearInvocations(LOGGER);
    medikation = new Medikation(LOGGER);
  }

  @Nested
  class MedicationAdministrationTest {

    @Nested
    class RequestTest {
      @Test
      @DisplayName("empty Verordnung should result in empty value")
      void testEmptyV() {
        medikation.setBezug_verordnung("");
        assertEmptyValue(medikation.getMedicationAdministrationRequest());
      }

      @Test
      @DisplayName("non-empty Verordnung should be present in Reference")
      void testRequest() {
        String verordnung = "some text";
        medikation.setBezug_verordnung(verordnung);
        Reference result = medikation.getMedicationAdministrationRequest();
        assertReference(verordnung, result);
      }
    }

    @Nested
    class DosageTest {
      @Test
      @DisplayName("empty Freitext should result in empty value")
      void testEmptyT() {
        medikation.setDosierung_freitext("");
        assertEmptyValue(medikation.getMedicationAdministrationDosage());
      }

      @Test
      @DisplayName(
          "non-empty Freitext should be present in MedicationAdministrationDosageComponent")
      void testDosage() {
        String text = "some dosage text";
        medikation.setDosierung_freitext(text);
        MedicationAdministration.MedicationAdministrationDosageComponent result =
            medikation.getMedicationAdministrationDosage();
        assertNonEmptyValue(result);
        assertEquals(text, result.getText());
      }
    }

    @Nested
    class IdentifierTest {
      @Test
      @DisplayName("empty Identifikation should result in empty value")
      void testEmptyId() {
        medikation.setIdentifikation("");
        assertEmptyValue(medikation.getMedicationAdministrationIdentifier());
      }

      @Test
      @DisplayName("non-empty Identifikation should be present in Identifier")
      void testIdentifier() {
        String id = "ExampleMedicationAdministration";
        medikation.setIdentifikation(id);
        Identifier result = medikation.getMedicationAdministrationIdentifier();
        assertIdentifier(id, IdentifierSystem.EMPTY, result);
      }
    }

    @Nested
    class StatusTest {
      @Test
      @DisplayName("invalid Status should result in empty value")
      void testInvalidStatus() {
        medikation.setStatus("");
        assertEmptyValue(medikation.getMedicationAdministrationStatus());
        medikation.setStatus("incorrect status");
        assertEmptyValue(medikation.getMedicationAdministrationStatus());
      }

      @Test
      @DisplayName("valid Status should be present in MedicationAdministrationStatus")
      void testStatus() {
        String status = "abgeschlossen";
        medikation.setStatus(status);
        MedicationAdministration.MedicationAdministrationStatus result =
            medikation.getMedicationAdministrationStatus();
        assertEquals(MedicationAdministration.MedicationAdministrationStatus.COMPLETED, result);
      }
    }

    @Nested
    class ReasonCodeTest {
      @Test
      @DisplayName("empty Behandlungsgurnd should result in empty value")
      void testEmptyB() {
        assertEmptyCodeValue(
            medikation::setBehandlungsgrund, medikation::getMedicationAdministrationReasonCode);
      }

      @Test
      @DisplayName("invalid Behandlungsgrund should result in empty value")
      void testInvalidB() {
        medikation.setBehandlungsgrund("invalid reason");
        assertEmptyValue(medikation.getMedicationAdministrationReasonCode());
      }

      @Test
      @DisplayName("valid Behandlungsgrund should be present in CodeableConcept")
      void testReasonCode() {
        String expectedCode = "b", expectedDisplay = "Given as Ordered";
        medikation.setBehandlungsgrund(expectedCode);
        CodeableConcept result = medikation.getMedicationAdministrationReasonCode();
        String GIVEN_AS_ORDERED_SYSTEM =
            "http://terminology.hl7.org/CodeSystem/reason-medication-given";
        assertCodeableConcept(expectedCode, GIVEN_AS_ORDERED_SYSTEM, expectedDisplay, result);
      }
    }
  }

  @Nested
  class MedicationStatementTest {

    @Nested
    class InformationSourceTest {
      @Test
      @DisplayName("empty Organisationsname should result in empty value")
      void testEmptyO() {
        medikation.setOrganisationsname("");
        assertEmptyValue(medikation.getMedicationStatementInformationSource());
      }

      @Test
      @DisplayName("non-empty Organisationsname should be present in Reference")
      void testInformationSource() {
        String name = "organisation";
        medikation.setOrganisationsname(name);
        Reference result = medikation.getMedicationStatementInformationSource();
        assertReference(name, result);
      }
    }

    @Nested
    class DateAssertedTest {
      @Test
      @DisplayName("empty Datumeintrag should result in empty value")
      void testEmptyD() {
        medikation.setDatum_eintrag("");
        assertEmptyValue(medikation.getMedicationStatementDateAsserted());
      }

      @Test
      @DisplayName("invalid Datumeintrag should result in empty value")
      void testInvalidD() {
        medikation.setDatum_eintrag("2018-13-13");
        assertEmptyValue(medikation.getMedicationStatementDateAsserted());
      }

      @Test
      @DisplayName("valid Datumeintrag should be present in Date")
      void testDateAsserted() {
        String date = "2020-04-18";
        medikation.setDatum_eintrag(date);
        Date result = medikation.getMedicationStatementDateAsserted();
        assertEquals(expectedDateString(date), result);
      }
    }

    @Nested
    class NoteTest {
      @Test
      @DisplayName("empty Hinweis should result in empty value")
      void testEmptyH() {
        medikation.setHinweis("");
        assertEmptyValue(medikation.getMedicationStatementNote());
      }

      @Test
      @DisplayName("non-empty Hinweis should be present in Annotation")
      void testNonEmptyH() {
        String hinweis = "some notification";
        medikation.setHinweis(hinweis);
        Annotation result = medikation.getMedicationStatementNote();
        assertAnnotation(hinweis, result);
      }
    }

    @Nested
    class DosageTest {
      @Test
      @DisplayName("empty Freitext should result in empty value")
      void testEmptyT() {
        medikation.setDosierung_freitext("");
        assertEmptyValue(medikation.getMedicationStatementDosage());
      }

      @Test
      @DisplayName("non-empty Freitext should be present in Dosage")
      void testDosage() {
        String text = "dosage text";
        medikation.setDosierung_freitext(text);
        Dosage result = medikation.getMedicationStatementDosage();
        assertNonEmptyValue(result);
        assertEquals(text, result.getText());
      }
    }

    @Nested
    class EffectiveTest {
      @Test
      @DisplayName("invalid Einnahmestartzeitpunkt should result in empty value")
      void testInvalidD() {
        medikation.setEinnahme_startzeitpunkt("2018-01-50");
        assertEmptyValue(medikation.getMedicationStatementEffective());
      }

      @Test
      @DisplayName(
          "valid Einnahmezeitpunkt should be present in DateTimeType, when Einnahmeendzeitpunkt is empty")
      void testValidSAndEmptyE() {
        String start = "2004-11-23";
        medikation.setEinnahme_startzeitpunkt(start);
        medikation.setEinnahme_endzeitpunkt("");
        Type result = medikation.getMedicationStatementEffective();
        assertNonEmptyValue(result);
        assertTrue(result instanceof DateTimeType);
        DateTimeType resultDate = (DateTimeType) result;
        assertDateTimeType(expectedDateString(start), resultDate);
      }

      @Test
      @DisplayName(
          "invalid Einnahmeendzeitpunkt should result in empty value, even when Einnahmestartzeitpunkt is valid")
      void testInvalidE() {
        String start = "2017-10-13";
        medikation.setEinnahme_startzeitpunkt(start);
        medikation.setEinnahme_endzeitpunkt("2016-13-40");
        assertEmptyValue(medikation.getMedicationStatementEffective());
      }

      @Test
      @DisplayName(
          "valid Einnahmestartzeitpunkt and Einnahmeendzeitpunkt should be present in Period")
      void testEffective() {
        String start = "2004-11-23";
        medikation.setEinnahme_startzeitpunkt(start);
        String end = "2005-03-20";
        medikation.setEinnahme_endzeitpunkt(end);
        Type result = medikation.getMedicationStatementEffective();
        assertNonEmptyValue(result);
        assertTrue(result instanceof Period);
        Period resultPeriod = (Period) result;
        assertPeriod(expectedDateString(start), expectedDateString(end), resultPeriod);
      }
    }

    @Nested
    class BasedOnTest {
      @Test
      @DisplayName("empty Bezugverordung should result in empty value")
      void testEmptyB() {
        medikation.setBezug_verordnung("");
        assertEmptyValue(medikation.getMedicationStatementBasedOn());
      }

      @Test
      @DisplayName("non-empty Bezugverordnung should be present in Reference")
      void testBasedOn() {
        String verordnung = "some rules";
        medikation.setBezug_verordnung(verordnung);
        Reference result = medikation.getMedicationStatementBasedOn();
        assertReference(verordnung, result);
      }
    }

    @Nested
    class StatusTest {
      @Test
      @DisplayName("invalid Status should result in empty value")
      void testInvalidStatus() {
        medikation.setStatus("");
        assertEmptyValue(medikation.getMedicationStatementStatus());
        medikation.setStatus("invalid status");
        assertEmptyValue(medikation.getMedicationStatementStatus());
      }

      @Test
      @DisplayName("valid Status should be present in MedicationStatementStatus")
      void testStatus() {
        medikation.setStatus("aktiv");
        MedicationStatement.MedicationStatementStatus result =
            medikation.getMedicationStatementStatus();
        assertEquals(MedicationStatement.MedicationStatementStatus.ACTIVE, result);
      }
    }

    @Nested
    class IdentifierTest {
      @Test
      @DisplayName("empty Identifikation should result in empty value")
      void testEmptyId() {
        medikation.setIdentifikation("");
        assertEmptyValue(medikation.getMedicationStatementIdentifier());
      }

      @Test
      @DisplayName("non-empty Identifikation should be present in Identifier")
      void testIdentifier() {
        String id = "ExampleMedicationThiotepa";
        medikation.setIdentifikation(id);
        Identifier result = medikation.getMedicationStatementIdentifier();
        assertIdentifier(id, null, result);
      }
    }

    @Nested
    class PartOfTest {
      @Test
      @DisplayName("empty Bezugabgabe should result in empty value")
      void testEmptyB() {
        medikation.setBezug_abgabe("");
        assertEmptyValue(medikation.getMedicationStatementPartOf());
      }

      @Test
      @DisplayName("non-empty Bezugabgabe should be present in Reference")
      void testPartOf() {
        String abgabe = "some text";
        medikation.setBezug_abgabe(abgabe);
        Reference result = medikation.getMedicationStatementPartOf();
        assertReference(abgabe, result);
      }
    }
  }

  @Nested
  class MedicationTest {

    @Nested
    class IngredientTest {
      @Test
      @DisplayName("invalid Wirkstoffcode-aktiv should result in empty value")
      void testInvalidW() {
        medikation.setWirkstoff_code_aktiv("");
        assertEmptyValue(medikation.getMedicationIngredient());
      }

      @Test
      @DisplayName("valid Wirkstoffcode-aktiv should be present in MedicationIngredientComponent")
      void testIngredient() {
        medikation.setWirkstoff_code_aktiv("00002");
        Medication.MedicationIngredientComponent result = medikation.getMedicationIngredient();
        assertNonEmptyValue(result);
        assertTrue(result.hasItem());
      }

      @Nested
      class WirkstofftypTest {
        @Test
        @DisplayName("empty Wirkstoffcode-allgemein should result in empty value")
        void testEmptyW() {
          assertEmptyCodeValue(
              medikation::setWirkstoff_code_allgemein,
              medikation::getMedicationIngredientExtension);
        }

        @Test
        @DisplayName("invalid Wirkstoffcode-allgemein should result in empty value")
        void testInvalidW() {
          medikation.setWirkstoff_code_allgemein(getCodeStr("invalid wirkstoff code"));
          assertEmptyValue(medikation.getMedicationIngredientExtension());
        }

        @Test
        @DisplayName("valid Wirkstoffcode-allgemein should be present in Coding of Extension")
        void testIngredientExtension() {
          String expectedCode = "MIN", expectedDisplay = "Kombinationswirkstoff";
          medikation.setWirkstoff_code_allgemein(getCodeDisplayStr(expectedCode, expectedDisplay));
          Extension result = medikation.getMedicationIngredientExtension();
          String WIRKSTOFFTYP_URL =
              "https://www.medizininformatik-initiative.de/fhir/core/modul-medikation/StructureDefinition/wirkstofftyp";
          String WIRKSTOFF_SYSTEM =
              "https://www.medizininformatik-initiative.de/fhir/core/modul-medikation/CodeSystem/wirkstofftyp";
          assertExtensionWithCoding(
              expectedCode, WIRKSTOFF_SYSTEM, expectedDisplay, WIRKSTOFFTYP_URL, result);
        }
      }

      @Nested
      class ItemTest {
        @Test
        @DisplayName("empty Wirkstoffcode-aktiv should result in empty value")
        void testEmptyW() {
          assertEmptyCodeValue(
              medikation::setWirkstoff_code_aktiv, medikation::getMedicationIngredientItem);
        }

        @Test
        @DisplayName(
            "non-empty Wirkstoffcode-aktiv and Wirkstoffname should be present in CodeableConcept")
        void testNonEmptyWAndW() {
          String code = "00002",
              system = "http://fhir.de/CodeSystem/ask",
              display = "Acetylsalicylsäure";
          medikation.setWirkstoff_code_aktiv(getCodeSystemStr(code, system));
          medikation.setWirkstoff_name_aktiv(display);
          CodeableConcept result = medikation.getMedicationIngredientItem();
          assertCodeableConcept(code, system, display, result);
        }

        @Test
        @DisplayName(
            "non-empty Wirkstoffcode-aktiv and explicitly given Wirkstoffname should be present in CodeableConcept")
        void testIngredientItem() {
          String code = "00002",
              system = "http://fhir.de/CodeSystem/ask",
              display = "Acetylsalicylsäure";
          medikation.setWirkstoff_code_aktiv(getCodeDisplaySystemStr(code, display, system));
          medikation.setWirkstoff_name_aktiv("ignored display");
          CodeableConcept result = medikation.getMedicationIngredientItem();
          assertCodeableConcept(code, system, display, result);
        }
      }

      @Nested
      class StrengthTest {
        @Test
        @DisplayName("empty Wirkstoffmenge should result in empty value")
        void testEmptyM() {
          medikation.setWirkstoff_menge("");
          assertEmptyValue(medikation.getMedicationIngredientStrength());
        }

        @Test
        @DisplayName("invalid Wirkstoffmenge should result in empty value")
        void testInvalidM() {
          medikation.setWirkstoff_menge(getValueUnitStr("12/0", "mmol/L"));
          assertEmptyValue(medikation.getMedicationIngredientStrength());
        }

        @Test
        @DisplayName("valid Wirkstoffmenge should be present in Ratio")
        void testIngredientStrength() {
          String value = "3/4", unit = "mmol/L";
          medikation.setWirkstoff_menge(getValueUnitStr(value, unit));
          Ratio result = medikation.getMedicationIngredientStrength();
          assertRatio("3", "mmol", "4", "L", result);
        }
      }
    }

    @Nested
    class AmountTest {
      @Test
      @DisplayName("empty Wirkstaerke should result in empty value")
      void testEmptyW() {
        medikation.setArzneimittel_wirkstaerke("");
        assertEmptyValue(medikation.getMedicationAmount());
      }

      @Test
      @DisplayName("invalid Wirkstaerke should result in empty value")
      void testInvalidW() {
        medikation.setArzneimittel_wirkstaerke(getValueUnitStr("//", "mmol/L"));
        assertEmptyValue(medikation.getMedicationAmount());
      }

      @Test
      @DisplayName("valid Wirkstaerke should be present in Ratio")
      void testAmount() {
        String value = "5/7", unit = "mmol/L";
        medikation.setArzneimittel_wirkstaerke(getValueUnitStr(value, unit));
        Ratio result = medikation.getMedicationAmount();
        assertRatio("5", "mmol", "7", "L", result);
      }
    }

    @Nested
    class FormTest {
      @Test
      @DisplayName("empty Darreichungsform should result in empty value")
      void testEmptyD() {
        assertEmptyCodeValue(medikation::setDarreichungsform, medikation::getMedicationForm);
      }

      @Test
      @DisplayName("invalid Darreichungsform should result in empty value")
      void testInvalidD() {
        medikation.setDarreichungsform("invalid code");
        assertEmptyValue(medikation.getMedicationForm());
      }

      @Test
      @DisplayName("valid Darreichungsform should be present in CodeableConcept")
      void testForm() {
        String code = "10219000", display = "Tablet";
        medikation.setDarreichungsform(getCodeDisplayStr(code, display));
        CodeableConcept result = medikation.getMedicationForm();
        String EDQM_SYSTEM = "http://standardterms.edqm.eu";
        assertCodeableConcept(code, EDQM_SYSTEM, display, result);
      }
    }

    @Nested
    class CodeTest {
      @Test
      @DisplayName(
          "empty Arzneimittelcode, Arzneimittelname and Freitext should result in empty value")
      void testEmpty() {
        medikation.setArzneimittel_name("");
        medikation.setRezeptur_freitextzeile("");
        assertEmptyCodeValue(medikation::setArzneimittel_code, medikation::getMedicationCode);
      }

      @Test
      @DisplayName(
          "non-empty Freitext should be present in CodeableConcept, even when Arzneimittelcode and -name are empty")
      void testText() {
        medikation.setArzneimittel_name("");
        medikation.setArzneimittel_code("");
        String text = "some medication info";
        medikation.setRezeptur_freitextzeile(text);
        CodeableConcept result = medikation.getMedicationCode();
        assertNonEmptyValue(result);
        assertFalse(result.hasCoding());
        assertEquals(text, result.getText());
      }

      @Test
      @DisplayName(
          "non-empty Arzneimittelcode and Pharma-Arzneimittelname should be present in CodeableConcept")
      void testNonEmptyPharmaCode() {
        medikation.setRezeptur_freitextzeile("");
        String code = "06312077",
            system = PZN_SYSTEM,
            display = "ASS 100 - 1a Pharma TAH Tabletten";
        medikation.setArzneimittel_code(getCodeDisplaySystemStr(code, display, system));
        CodeableConcept result = medikation.getMedicationCode();
        assertCodeableConcept(code, system, display, result);
      }

      @Test
      @DisplayName(
          "non-empty Arzneimittelcode and empty Arzneimittelname should be present as AtcCe-Code in CodeableConcept")
      void testNonEmptyAtcCode() {
        String code = "B01AC06";
        String display = "acetylsalicylic acid";
        medikation.setArzneimittel_code(getCodeDisplayStr(code, display));
        CodeableConcept result = medikation.getMedicationCode();
        assertCodeableConcept(code, ATC_DE_SYSTEM, display, result);
      }

      @Nested
      class PharmaTest {
        @Test
        @DisplayName("empty Arzneimittelcode should result in empty value")
        void testEmptyCode() {
          assertEmptyCodeValue(
              medikation::setArzneimittel_code, medikation::getMedicationCodePharma);
        }

        @Test
        @DisplayName(
            "non-empty Arzneimittelcode without display and Arzneimittelname should be present in Coding")
        void testNonEmptyCAndN() {
          String code = "06312077", display = "ASS 100 - 1a Pharma TAH Tabletten";
          medikation.setArzneimittel_code(getCodeStr(code));
          medikation.setArzneimittel_name(display);
          Coding result = medikation.getMedicationCodePharma();
          assertCoding(code, PZN_SYSTEM, display, result);
        }

        @Test
        @DisplayName(
            "non-empty Arzneimittelcode with display should be present in Coding, even when Arzneimittelname is given")
        void testCodePharma() {
          String code = "06312077", display = "ASS 100 - 1a Pharma TAH Tabletten";
          medikation.setArzneimittel_code(getCodeDisplayStr(code, display));
          medikation.setArzneimittel_name("ignored display");
          Coding result = medikation.getMedicationCodePharma();
          assertCoding(code, PZN_SYSTEM, display, result);
        }
      }

      @Nested
      class AtcClassDeTest {
        @Test
        @DisplayName("empty Arzneimittelcode should result in empty value")
        void testEmptyC() {
          assertEmptyCodeValue(
              medikation::setArzneimittel_code, medikation::getMedicationCodeAtcDE);
        }

        @Test
        @DisplayName(
            "non-empty Arzneimittelcode without display and Arzneimittelname should be present in Coding")
        void testNonEmptyCAndN() {
          String code = "B01AC06", display = "acetylsalicylic acid";
          medikation.setArzneimittel_code(getCodeStr(code));
          medikation.setArzneimittel_name(display);
          Coding result = medikation.getMedicationCodeAtcDE();
          assertCoding(code, ATC_DE_SYSTEM, display, result);
        }

        @Test
        @DisplayName(
            "non-empty Arzneimittelcode with display should be present in Coding, even when Arzneimittelname is given")
        void testCodeAtcDE() {
          String code = "B01AC06", display = "acetylsalicylic acid";
          medikation.setArzneimittel_code(getCodeDisplayStr(code, display));
          medikation.setArzneimittel_name("ignored display");
          Coding result = medikation.getMedicationCodeAtcDE();
          assertCoding(code, ATC_DE_SYSTEM, display, result);
        }
      }
    }
  }
}
