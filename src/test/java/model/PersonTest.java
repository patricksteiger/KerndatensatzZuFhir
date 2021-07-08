package model;

import constants.CodingCode;
import constants.CodingSystem;
import constants.ExtensionUrl;
import constants.IdentifierSystem;
import helper.Helper;
import helper.Logger;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import valueSets.IdentifierTypeCode;
import valueSets.MIICoreLocations;
import valueSets.VersichertenCode;
import valueSets.VitalStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

class PersonTest {
  private static Logger LOGGER;
  private Person person;

  @BeforeAll
  static void init() {
    LOGGER = Mockito.mock(Logger.class, Mockito.CALLS_REAL_METHODS);
    setUpLoggerMock(LOGGER);
  }

  @BeforeEach
  public void setUp() throws Exception {
    clearInvocations(LOGGER);
    person = new Person();
    setMockLoggerField(person, LOGGER);
  }

  @Nested
  class ObservationTest {

    @Nested
    class ValueTest {
      @Test
      @DisplayName("empty Verstorben should result in empty value")
      void testEmptyValue() {
        person.setPatient_verstorben("");
        Coding result = person.getObservationValue();
        assertCoding(VitalStatus.UNBEKANNT, result);
      }

      @Test
      @DisplayName("if Verstorben is \"1\", T should be present in Coding")
      void testValue() {
        person.setPatient_verstorben("1");
        Coding result = person.getObservationValue();
        assertCoding(VitalStatus.VERSTORBEN, result);
      }
    }

    @Nested
    class EffectiveTest {
      @Test
      @DisplayName("invalid Zeitpunkt should result in empty value")
      void testInvalidEffective() {
        person.setLetzter_lebendzeitpunkt("2014-20-31");
        assertEmptyValue(person.getObservationEffective());
      }

      @Test
      @DisplayName("valid Zeitpunkt should be present in DateTimeType")
      void testValidEffective() {
        String zeitpunkt = "2013-09-12";
        person.setLetzter_lebendzeitpunkt(zeitpunkt);
        DateTimeType result = person.getObservationEffective();
        assertDateTimeType(expectedDateString(zeitpunkt), result);
      }
    }

    @Nested
    class CodeTest {
      @Test
      @DisplayName("code is fixed CodeableConcept: 67162-8")
      void testCode() {
        CodeableConcept result = person.getObservationCode();
        assertCodeableConcept(CodingCode.LOINC_OBSERVATION, CodingSystem.LOINC, null, result);
      }
    }

    @Nested
    class CategoryTest {
      @Test
      @DisplayName("category is fixed CodeableConcept: survey")
      void testCategory() {
        CodeableConcept result = person.getObservationCategory();
        assertCodeableConcept(CodingCode.SURVEY, CodingSystem.OBSERVATION_CATEGORY, null, result);
      }
    }

    @Nested
    class StatusTest {
      @Test
      @DisplayName("status is fixed value: FINAL")
      void testStatus() {
        assertEquals(Observation.ObservationStatus.FINAL, person.getObservationStatus());
      }
    }
  }

  @Nested
  class PatientTest {

    @Nested
    class IdentifierTest {
      @Test
      @DisplayName("empty PID, PKV, and GKV should result in empty values")
      void testEmptyIds() {
        person.setVersichertenId_gkv("");
        person.setVersichertennummer_pkv("");
        person.setPatient_pid("");
        assertAllEmptyValue(person.getPatientIdentifiers());
      }

      @Test
      @DisplayName("non-empty PID, GKV, IKNR and PKV should be present as Identifier")
      void testIdentifiers() {
        String gkv = "543151", iknr = "583467254", pkv = "123457", pid = "4536782";
        person.setVersichertenId_gkv(gkv);
        person.setInstitutionskennzeichen_krankenkasse(iknr);
        person.setVersichertennummer_pkv(pkv);
        person.setPatient_pid(pid);
        assertAllNonEmptyValue(person.getPatientIdentifiers());
      }

      @Nested
      class PIDTest {
        @Test
        @DisplayName("empty PID should result in empty value")
        void testEmptyPID() {
          person.setPatient_pid("");
          assertEmptyValue(person.getPatientPID());
        }

        @Test
        @DisplayName("non-empty PID should be present in Identifier with Core-ReferenceAssigner")
        void testNonEmptyPID() {
          String pid = "42285243";
          person.setPatient_pid(pid);
          Identifier pidIdentifier = person.getPatientPID();
          assertIdentifier(pid, IdentifierSystem.PID, IdentifierTypeCode.MR, pidIdentifier);
          assertTrue(pidIdentifier.hasAssigner());
          Reference assigner = pidIdentifier.getAssigner();
          assertEquals(MIICoreLocations.UKU.getDisplay(), assigner.getDisplay());
          assertTrue(assigner.hasIdentifier());
          assertEquals(MIICoreLocations.UKU.getCode(), assigner.getIdentifier().getValue());
          assertEquals(IdentifierSystem.CORE_LOCATIONS, assigner.getIdentifier().getSystem());
        }
      }

      @Nested
      class GKVTest {
        @Test
        @DisplayName("empty GKV should result in empty value")
        void testEmptyGKV() {
          person.setVersichertenId_gkv("");
          assertEmptyValue(person.getPatientGKV());
        }

        @Test
        @DisplayName(
            "non-empty GKV should be present in Identifier, while empty IKNR should not be present")
        void testNonEmptyGKVEmptyIKNR() {
          person.setInstitutionskennzeichen_krankenkasse("");
          String gkv = "25436435";
          person.setVersichertenId_gkv(gkv);
          Identifier result = person.getPatientGKV();
          assertIdentifier(gkv, IdentifierSystem.VERSICHERTEN_ID_GKV, VersichertenCode.GKV, result);
          assertFalse(result.hasAssigner());
        }

        @Test
        @DisplayName("non-empty GKV and IKNR should be present in Identifier")
        void testGKV() {
          String gkv = "25489123";
          person.setVersichertenId_gkv(gkv);
          String iknr = "260326821";
          person.setInstitutionskennzeichen_krankenkasse(iknr);
          person.setVersichertenId_gkv(gkv);
          Identifier result = person.getPatientGKV();
          assertIdentifier(gkv, IdentifierSystem.VERSICHERTEN_ID_GKV, VersichertenCode.GKV, result);
          assertTrue(result.hasAssigner());
          assertIdentifier(
              iknr,
              IdentifierSystem.IKNR,
              Identifier.IdentifierUse.OFFICIAL,
              result.getAssigner().getIdentifier());
        }

        @Nested
        class GKVAssignerReferenceTest {
          @Test
          @DisplayName("empty IKNR should result in empty value")
          void testEmptyIKNR() {
            person.setInstitutionskennzeichen_krankenkasse("");
            assertEmptyValue(person.getGKVAssignerReference());
          }

          @Test
          @DisplayName("non-empty IKNR should be present in Reference")
          void testGKVAssignerReference() {
            String iknr = "260326822";
            person.setInstitutionskennzeichen_krankenkasse(iknr);
            Reference result = person.getGKVAssignerReference();
            assertNonEmptyValue(result);
            assertIdentifier(
                iknr,
                IdentifierSystem.IKNR,
                Identifier.IdentifierUse.OFFICIAL,
                result.getIdentifier());
          }
        }
      }

      @Nested
      class PKVTest {
        @Test
        @DisplayName("empty PKV should result in empty value")
        void testEmpty() {
          person.setVersichertennummer_pkv("");
          assertEmptyValue(person.getPatientPKV());
        }

        @Test
        @DisplayName("non-empty PKV should be present in Identifier")
        void testNonEmpty() {
          String pkv = "123456";
          person.setVersichertennummer_pkv(pkv);
          Identifier result = person.getPatientPKV();
          assertIdentifier(pkv, null, VersichertenCode.PKV, result);
          assertFalse(result.hasAssigner());
          assertEquals(Identifier.IdentifierUse.SECONDARY, result.getUse());
        }
      }
    }

    @Nested
    class AddressTest {
      @Nested
      class AddressStrassenanschriftTest {
        @Test
        @DisplayName("empty Address-info should result in empty value")
        void testEmptyA() {
          person.setStrasse("");
          person.setStrassenanschrift_wohnort("");
          person.setStrassenanschrift_plz("");
          person.setStrassenanschrift_land("");
          assertEmptyValue(person.getPatientAddressStrassenanschrift());
        }

        @Test
        @DisplayName("non-empty Land and empty Address-info-rest should result in empty value")
        void testNonEmptyLandAndEmptyA() {
          person.setStrasse("");
          person.setStrassenanschrift_wohnort("");
          person.setStrassenanschrift_plz("");
          person.setStrassenanschrift_land("DE");
          assertEmptyValue(person.getPatientAddressStrassenanschrift());
        }

        @Test
        @DisplayName("non-empty Address-info should be present in Address")
        void testNonEmptyA() {
          String strasse = "Teststraße 2", wohnort = "Köln", plz = "50823", land = "DE";
          person.setStrasse(strasse);
          person.setStrassenanschrift_wohnort(wohnort);
          person.setStrassenanschrift_plz(plz);
          person.setStrassenanschrift_land(land);
          Address result = person.getPatientAddressStrassenanschrift();
          assertNonEmptyValue(result);
          assertEquals(Address.AddressType.BOTH, result.getType());
          assertEquals(1, result.getLine().size());
          assertEquals(strasse, result.getLine().get(0).getValue());
          assertEquals(wohnort, result.getCity());
          assertEquals(plz, result.getPostalCode());
          assertEquals(land, result.getCountry());
        }
      }

      @Nested
      class AddressPostfachTest {
        @Test
        @DisplayName("empty Address-info should result in empty value")
        void testEmptyA() {
          person.setPostfachnummer("");
          person.setPostfach_wohnort("");
          person.setPostfach_plz("");
          person.setPostfach_land("");
          assertEmptyValue(person.getPatientAddressPostfach());
        }

        @Test
        @DisplayName(
            "non-empty Postfachnummer and empty Address-info-rest should result in empty value")
        void testNonEmptyPFNAndEmptyA() {
          person.setPostfachnummer("12 45 68");
          person.setPostfach_wohnort("");
          person.setPostfach_plz("");
          person.setPostfach_land("");
          assertEmptyValue(person.getPatientAddressPostfach());
        }

        @Test
        @DisplayName("non-empty Address-info should be present in Address")
        void testNonEmptyA() {
          String nummer = "1 23 45", wohnort = "Lalaland", plz = "123456", land = "DE";
          person.setPostfachnummer(nummer);
          person.setPostfach_wohnort(wohnort);
          person.setPostfach_plz(plz);
          person.setPostfach_land(land);
          Address result = person.getPatientAddressPostfach();
          assertNonEmptyValue(result);
          assertEquals(Address.AddressType.POSTAL, result.getType());
          assertEquals(1, result.getLine().size());
          assertEquals(nummer, result.getLine().get(0).getValue());
          assertEquals(plz, result.getPostalCode());
          assertEquals(land, result.getCountry());
        }
      }
    }

    @Nested
    class Deceased {
      @Test
      @DisplayName("empty Todeszeitpunkt and Verstorben should result in empty value")
      void testEmpty() {
        person.setTodeszeitpunkt("");
        person.setPatient_verstorben("");
        assertEmptyValue(person.getPatientDeceased());
      }

      @Test
      @DisplayName("empty Todeszeitpunkt and invalid Verstorben should result in empty value")
      void testInvalidV() {
        person.setTodeszeitpunkt("");
        person.setPatient_verstorben("invalid verstorben");
        assertEmptyValue(person.getPatientDeceased());
      }

      @Test
      @DisplayName("empty Todeszeitpunkt and valid Verstorben should result in BooleanType")
      void testValidV() {
        person.setTodeszeitpunkt("");
        person.setPatient_verstorben("0");
        Type result = person.getPatientDeceased();
        assertNonEmptyValue(result);
        assertTrue(result instanceof BooleanType);
        assertFalse(((BooleanType) result).booleanValue());
      }

      @Test
      @DisplayName("invalid Todeszeitpunkt and valid Verstorben should result in BooleanType")
      void testInvalidZValidV() {
        person.setTodeszeitpunkt("invalid zeitpunkt");
        person.setPatient_verstorben("1");
        assertNonEmptyValue(person.getPatientDeceased());
        Type result = person.getPatientDeceased();
        assertTrue(result instanceof BooleanType);
        assertTrue(((BooleanType) result).booleanValue());
      }

      @Test
      @DisplayName("valid Todeszeitpunkt and valid Verstorben should result in DateTimeType")
      void testDeceased() {
        String date = "2020-12-12";
        person.setTodeszeitpunkt(date);
        person.setPatient_verstorben("1");
        Type result = person.getPatientDeceased();
        assertNonEmptyValue(result);
        assertTrue(result instanceof DateTimeType);
        assertEquals(expectedDateString(date), ((DateTimeType) result).getValue());
      }
    }

    @Nested
    class BirthDateTest {
      @Test
      @DisplayName("invalid Geburtsdatum should result in empty value")
      void testInvalidD() {
        person.setGeburtsdatum("");
        assertEmptyValue(person.getPatientBirthDate());
      }

      @Test
      @DisplayName("valid Geburtsdatum should be present in Date")
      void testValidD() {
        String date = "1999-12-31";
        person.setGeburtsdatum(date);
        assertEquals(expectedDateString(date), person.getPatientBirthDate());
      }
    }

    @Nested
    class GenderTest {
      @Test
      @DisplayName("empty Administratives Geschlecht should result in empty value")
      void testEmptyG() {
        person.setAdmininistratives_geschlecht(null);
        assertEmptyValue(person.getPatientGender());
        person.setAdmininistratives_geschlecht("");
        assertEmptyValue(person.getPatientGender());
      }

      @Test
      @DisplayName("valid Administratives Geschlecht should be present in Gender")
      void testValidG() {
        String geschlecht = "M";
        person.setAdmininistratives_geschlecht(geschlecht);
        Enumeration<Enumerations.AdministrativeGender> result = person.getPatientGender();
        assertNonEmptyValue(result);
        assertEquals("male", result.getCode());
      }

      @Test
      @DisplayName(
          "valid Administratives Geschlecht X should be present in gender with Extension containing Coding")
      void testX() {
        person.setAdmininistratives_geschlecht("X");
        Enumeration<Enumerations.AdministrativeGender> result = person.getPatientGender();
        assertNonEmptyValue(result);
        assertEquals("other", result.getCode());
        String EXTENSION_URL = "http://fhir.de/StructureDefinition/gender-amtlich-de";
        assertTrue(result.hasExtension(EXTENSION_URL));
        Extension extension = result.getExtensionByUrl(EXTENSION_URL);
        String CODING_SYSTEM = "http://fhir.de/CodeSystem/gender-amtlich-de";
        assertExtensionWithCoding("X", CODING_SYSTEM, "unbestimmt", EXTENSION_URL, extension);
      }
    }

    @Nested
    class NameTest {
      @Nested
      class GeburtsnameTest {
        @Test
        @DisplayName("empty Geburtsname should result in empty value")
        void testEmptyN() {
          person.setGeburtsname("");
          assertEmptyValue(person.getPatientGeburtsname());
        }

        @Test
        @DisplayName("non-empty Geburtsname should be present in HumanName")
        void testValidN() {
          String geburtsname = "Clemens";
          person.setGeburtsname(geburtsname);
          HumanName result = person.getPatientGeburtsname();
          assertHumanName(geburtsname, HumanName.NameUse.MAIDEN, result);
        }
      }

      @Nested
      class NameHumanNameTest {
        @Test
        @DisplayName("non-empty Familienname, Vorname and Praefix should be present in HumanName")
        void testNonEmptyN() {
          String familienname = "Van-der-Dussel";
          person.setFamilienname(familienname);
          String firstName = "Maja", secondName = "Julia", vorname = firstName + "  " + secondName;
          person.setVorname(vorname);
          String praefix = "Prof. Dr. med.";
          person.setPraefix(praefix);
          HumanName result = person.getPatientName();
          assertHumanName(
              familienname,
              Helper.listOf(firstName, secondName),
              praefix,
              HumanName.NameUse.OFFICIAL,
              result);
        }

        @Nested
        class FamilyTest {
          @Test
          @DisplayName("empty Familienname should result in empty value")
          void testEmptyF() {
            person.setVorsatzwort("");
            assertEmptyValue(person.getPatientNameFamily());
          }

          @Test
          @DisplayName("non-empty Familienname should be present in StringType")
          void testNonEmptyFamilienname() {
            String familienname = "Van-der-Dussen";
            person.setFamilienname(familienname);
            StringType result = person.getPatientNameFamily();
            assertNonEmptyValue(result);
            assertEquals(familienname, result.getValue());
            assertFalse(result.hasExtension());
          }

          @Test
          @DisplayName("non-empty Familien- and Nachname should be present in StringType")
          void testNameFamily() {
            String familienname = "Van-der-Dussen";
            person.setFamilienname(familienname);
            person.setNachname(familienname);
            StringType result = person.getPatientNameFamily();
            assertNonEmptyValue(result);
            assertEquals(familienname, result.getValue());
            assertEquals(1, result.getExtension().size());
            assertTrue(result.hasExtension(ExtensionUrl.NACHNAME));
          }

          @Nested
          class NamenszusatzTest {
            @Test
            @DisplayName("empty Namenszusatz should result in empty value")
            void testEmptyN() {
              person.setNamenszusatz("");
              assertEmptyValue(person.getPatientNameFamilyNamenszusatz());
            }

            @Test
            @DisplayName("non-empty Namenszusatz should be present in Extension")
            void testNonEmptyN() {
              String namenszusatz = "RA";
              person.setNamenszusatz(namenszusatz);
              Extension result = person.getPatientNameFamilyNamenszusatz();
              assertExtensionWithStringType(namenszusatz, ExtensionUrl.NAMENSZUSATZ, result);
            }
          }

          @Nested
          class NachnameTest {
            @Test
            @DisplayName("empty Nachname should result in empty value")
            void testEmptyN() {
              person.setNachname("");
              assertEmptyValue(person.getPatientNameFamilyNachname());
            }

            @Test
            @DisplayName("non-empty Nachname should be present in Extension")
            void testNonEmptyN() {
              String nachname = "Van-der-Dussen";
              person.setNachname(nachname);
              Extension result = person.getPatientNameFamilyNachname();
              assertExtensionWithStringType(nachname, ExtensionUrl.NACHNAME, result);
            }
          }

          @Nested
          class VorsatzwortTest {
            @Test
            @DisplayName("empty Vorsatzwort should result in empty value")
            void testEmptyV() {
              person.setVorsatzwort("");
              assertEmptyValue(person.getPatientNameFamilyVorsatzwort());
            }

            @Test
            @DisplayName("non-empty Vorsatzwort should be present in Extension")
            void testNonEmptyV() {
              String vorsatzwort = "bei der";
              person.setVorsatzwort(vorsatzwort);
              Extension result = person.getPatientNameFamilyVorsatzwort();
              assertExtensionWithStringType(vorsatzwort, ExtensionUrl.VORSATZWORT, result);
            }
          }
        }

        @Nested
        class GivenTest {
          @Test
          @DisplayName("empty Vorname should result in empty value")
          void testEmptyN() {
            person.setVorname("");
            assertEmptyValue(person.getPatientNameGiven());
          }

          @Test
          @DisplayName(
              "non-empty Vorname with multiple names should be present in List of StringType")
          void testNameGiven() {
            String firstName = "Maja", secondName = "Julia";
            String vorname = firstName + " " + secondName;
            person.setVorname(vorname);
            List<StringType> result = person.getPatientNameGiven();
            assertNonEmptyValue(result);
            assertEquals(2, result.size());
            assertEquals(firstName, result.get(0).getValue());
            assertEquals(secondName, result.get(1).getValue());
          }
        }

        @Nested
        class PrefixTest {
          @Test
          @DisplayName("empty Praefix should result in empty value")
          void testEmptyP() {
            person.setPraefix("");
            assertEmptyValue(person.getPatientNamePrefix());
          }

          @Test
          @DisplayName("non-empty Praefix should be present in StringType")
          void testNonEmptyP() {
            String praefix = "Prof. Dr. med.";
            person.setPraefix(praefix);
            StringType result = person.getPatientNamePrefix();
            assertNonEmptyValue(result);
            assertEquals(praefix, result.getValue());
            assertFalse(result.hasExtension());
          }

          @Test
          @DisplayName("non-empty Praefix and ArtDesPraefix should be present in StringType")
          void testNonEmptyPAndA() {
            String praefix = "Prof. Dr. med.";
            person.setPraefix(praefix);
            String artDesPraefix = "AC";
            person.setArt_des_praefix(artDesPraefix);
            StringType result = person.getPatientNamePrefix();
            assertNonEmptyValue(result);
            assertEquals(praefix, result.getValue());
            assertEquals(1, result.getExtension().size());
            assertTrue(result.hasExtension(ExtensionUrl.PREFIX));
          }

          @Nested
          class PrefixQualifierTest {
            @Test
            @DisplayName("empty ArtDesPraefix should result in empty value")
            void testEmptyP() {
              assertEmptyCodeValue(
                  person::setArt_des_praefix, person::getPatientNamePrefixQualifier);
            }

            @Test
            @DisplayName("non-empty ArtDesPraefix should be present in Extension")
            void testNonEmptyP() {
              String artDesPraefix = "AC";
              String code = getCodeStr(artDesPraefix);
              person.setArt_des_praefix(code);
              Extension result = person.getPatientNamePrefixQualifier();
              assertNonEmptyValue(result);
              assertEquals(ExtensionUrl.PREFIX, result.getUrl());
              assertTrue(result.hasValue());
              assertTrue(result.getValue() instanceof CodeType);
              CodeType type = (CodeType) result.getValue();
              assertEquals(artDesPraefix, type.getCode());
            }
          }
        }
      }
    }
  }

  @Nested
  class ResearchSubjectTest {

    @Nested
    class PeriodTest {
      @Test
      @DisplayName("invalid Teilnahmebeginn should result in empty value")
      void testInvalidP() {
        person.setTeilnahme_beginn("");
        assertEmptyValue(person.getResearchSubjectPeriod());
      }

      @Test
      @DisplayName("valid Teilnahmebeginn should be present in Period, when Teilnahmeende is empty")
      void testValidBAndEmptyE() {
        String beginn = "2020-11-13";
        person.setTeilnahme_beginn(beginn);
        person.setTeilnahme_ende("");
        Period result = person.getResearchSubjectPeriod();
        assertPeriod(expectedDateString(beginn), null, result);
      }

      @Test
      @DisplayName(
          "invalid Teilnahmeende should result in empty value, even if Teilnahmebeginn is valid")
      void testInvalidE() {
        String beginn = "2020-11-13";
        person.setTeilnahme_beginn(beginn);
        person.setTeilnahme_ende("invalid date");
        assertEmptyValue(person.getResearchSubjectPeriod());
      }

      @Test
      @DisplayName("valid Teilnahmebeginn and -ende should be present in Period")
      void testPeriod() {
        String beginn = "2020-11-13";
        String ende = "2020-11-15";
        person.setTeilnahme_beginn(beginn);
        person.setTeilnahme_ende(ende);
        Period result = person.getResearchSubjectPeriod();
        assertPeriod(expectedDateString(beginn), expectedDateString(ende), result);
      }
    }

    @Nested
    class StatusTest {
      @Test
      @DisplayName("invalid Status should result in empty value")
      void testEmptyStatus() {
        assertEmptyCodeValue(person::setTeilnahme_status, person::getResearchSubjectStatus);
      }

      @Test
      @DisplayName("valid Status should returned as ResearchSubjectStatus")
      void testStatus() {
        String status = "candidate";
        person.setTeilnahme_status(status);
        ResearchSubject.ResearchSubjectStatus result = person.getResearchSubjectStatus();
        assertEquals(ResearchSubject.ResearchSubjectStatus.CANDIDATE, result);
      }
    }

    @Nested
    class SubjectIdentificationCodeTest {
      @Test
      @DisplayName("empty Identifizierungscode should result in empty value")
      void testEmptyId() {
        assertEmptyCodeValue(
            person::setSubjekt_identifizierungscode,
            person::getResearchSubjectSubjectIdentificationCode);
      }

      @Test
      @DisplayName("non-empty Identifizierungscode should be present in Identifier")
      void testSubjectIdentificationCode() {
        String subjektIdentifikationsCode = "12345";
        String code = getCodeStr(subjektIdentifikationsCode);
        person.setSubjekt_identifizierungscode(code);
        Identifier result = person.getResearchSubjectSubjectIdentificationCode();
        assertIdentifier(
            subjektIdentifikationsCode,
            IdentifierSystem.SUBJECT_IDENTIFICATION_CODE,
            IdentifierTypeCode.ANON,
            result);
      }
    }
  }
}
