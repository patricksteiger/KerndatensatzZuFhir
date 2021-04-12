package model.person;

import constants.ExtensionUrl;
import constants.IdentifierSystem;
import enums.IdentifierTypeCode;
import enums.MIICoreLocations;
import enums.VersichertenCode;
import helper.Helper;
import helper.Logger;
import model.Person;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static util.Asserter.*;
import static util.Util.*;

class PatientTest {
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

  @Test
  void testPID() {
    // empty pid [NO LOGGING]
    person.setPatient_pid("");
    assertEmptyValue(person.getPatientPID());
    // non-empty pid
    String pid = "42285243";
    person.setPatient_pid(pid);
    Identifier pidIdentifier = person.getPatientPID();
    assertIdentifier(pid, IdentifierSystem.PID, IdentifierTypeCode.MR, pidIdentifier);
    assertTrue(pidIdentifier.hasAssigner());
    Reference assigner = pidIdentifier.getAssigner();
    assertEquals(MIICoreLocations.UKU.toString(), assigner.getDisplay());
    assertTrue(assigner.hasIdentifier());
    assertEquals(MIICoreLocations.UKU.name(), assigner.getIdentifier().getValue());
    assertEquals(IdentifierSystem.CORE_LOCATIONS, assigner.getIdentifier().getSystem());
  }

  @Test
  void testGKVAssignerReference() {
    // empty iknr
    person.setInstitutionskennzeichen_krankenkasse("");
    assertEmptyValue(person.getGKVAssignerReference());
    // non-empty iknr
    String iknr = "260326822";
    person.setInstitutionskennzeichen_krankenkasse(iknr);
    Reference result = person.getGKVAssignerReference();
    assertNonEmptyValue(result);
    assertIdentifier(
        iknr, IdentifierSystem.IKNR, Identifier.IdentifierUse.OFFICIAL, result.getIdentifier());
  }

  @Test
  void testGKV() {
    // empty gkv [NO LOGGING]
    person.setVersichertenId_gkv("");
    assertEmptyValue(person.getPatientGKV());
    // non-empty gkv, empty iknr
    person.setInstitutionskennzeichen_krankenkasse("");
    String gkv = "25436435";
    person.setVersichertenId_gkv(gkv);
    Identifier result = person.getPatientGKV();
    assertIdentifier(gkv, IdentifierSystem.VERSICHERTEN_ID_GKV, VersichertenCode.GKV, result);
    assertFalse(result.hasAssigner());
    // non-empty gkv, non-empty iknr
    String iknr = "260326821";
    person.setInstitutionskennzeichen_krankenkasse(iknr);
    person.setVersichertenId_gkv(gkv);
    result = person.getPatientGKV();
    assertIdentifier(gkv, IdentifierSystem.VERSICHERTEN_ID_GKV, VersichertenCode.GKV, result);
    assertTrue(result.hasAssigner());
    assertIdentifier(
        iknr,
        IdentifierSystem.IKNR,
        Identifier.IdentifierUse.OFFICIAL,
        result.getAssigner().getIdentifier());
  }

  @Test
  void testPKV() {
    // empty pkv [NO LOGGING]
    person.setVersichertennummer_pkv("");
    assertEmptyValue(person.getPatientPKV());
    // non-empty pkv
    String pkv = "123456";
    person.setVersichertennummer_pkv(pkv);
    Identifier result = person.getPatientPKV();
    assertIdentifier(pkv, null, VersichertenCode.PKV, result);
    assertFalse(result.hasAssigner());
    assertEquals(Identifier.IdentifierUse.SECONDARY, result.getUse());
  }

  @Test
  void testIdentifiers() {
    // empty gkv, pkv and pid
    person.setVersichertenId_gkv("");
    person.setVersichertennummer_pkv("");
    person.setPatient_pid("");
    assertAllEmptyValue(person.getPatientIdentifiers());
    // non-empty gkv, iknr, pkv and pid
    String gkv = "543151", iknr = "583467254", pkv = "123457", pid = "4536782";
    person.setVersichertenId_gkv(gkv);
    person.setInstitutionskennzeichen_krankenkasse(iknr);
    person.setVersichertennummer_pkv(pkv);
    person.setPatient_pid(pid);
    assertAllNonEmptyValue(person.getPatientIdentifiers());
  }

  @Test
  void testAddressStrassenanschrift() {
    // empty strasse, wohnort, plz, land [NO LOGGING]
    person.setStrasse("");
    person.setStrassenanschrift_wohnort("");
    person.setStrassenanschrift_plz("");
    person.setStrassenanschrift_land("");
    assertEmptyValue(person.getPatientAddressStrassenanschrift());
    // empty strasse, wohnort, plz and non-empty land
    person.setStrasse("");
    person.setStrassenanschrift_wohnort("");
    person.setStrassenanschrift_plz("");
    person.setStrassenanschrift_land("DE");
    assertEmptyValue(person.getPatientAddressStrassenanschrift());
    // non-empty strasse, wohnort, plz, land
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

  @Test
  void testAddressPostfach() {
    // empty nummer, wohnort, plz, land [NO LOGGING]
    person.setPostfachnummer("");
    person.setPostfach_wohnort("");
    person.setPostfach_plz("");
    person.setPostfach_land("");
    assertEmptyValue(person.getPatientAddressPostfach());
    // empty wohnort, plz, land
    person.setPostfachnummer("12 45 68");
    person.setPostfach_wohnort("");
    person.setPostfach_plz("");
    person.setPostfach_land("");
    assertEmptyValue(person.getPatientAddressPostfach());
    // non-empty nummer, wohnort, plz, land
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

  @Test
  void testDeceased() {
    // empty todeszeitpunkt and verstorben [NO LOGGING]
    person.setTodeszeitpunkt("");
    person.setPatient_verstorben("");
    assertEmptyValue(person.getPatientDeceased());
    // empty todeszeitpunkt and invalid verstorben
    person.setTodeszeitpunkt("");
    person.setPatient_verstorben("invalid verstorben");
    assertEmptyValue(person.getPatientDeceased());
    // empty todeszeitpunkt and valid verstorben
    person.setTodeszeitpunkt("");
    person.setPatient_verstorben("0");
    Type result = person.getPatientDeceased();
    assertNonEmptyValue(result);
    assertTrue(result instanceof BooleanType);
    assertFalse(((BooleanType) result).booleanValue());
    // invalid todeszeitpunkt does not override valid verstorben
    person.setTodeszeitpunkt("invalid zeitpunkt");
    person.setPatient_verstorben("1");
    assertNonEmptyValue(person.getPatientDeceased());
    result = person.getPatientDeceased();
    assertTrue(result instanceof BooleanType);
    assertTrue(((BooleanType) result).booleanValue());
    // valid todeszeitpunkt overrides valid verstorben
    String date = "2020-12-12";
    person.setTodeszeitpunkt(date);
    person.setPatient_verstorben("1");
    result = person.getPatientDeceased();
    assertNonEmptyValue(result);
    assertTrue(result instanceof DateTimeType);
    assertEquals(expectedDateString(date), ((DateTimeType) result).getValue());
  }

  @Test
  void testBirthDate() {
    // invalid date
    person.setGeburtsdatum("");
    assertEmptyValue(person.getPatientBirthDate());
    // valid date
    String date = "1999-12-31";
    person.setGeburtsdatum(date);
    assertEquals(expectedDateString(date), person.getPatientBirthDate());
  }

  @Test
  void testGender() {
    // empty geschlecht
    person.setAdmininistratives_geschlecht("");
    assertEmptyValue(person.getPatientGender());
    // valid geschlecht
    String geschlecht = "M";
    person.setAdmininistratives_geschlecht(geschlecht);
    assertEquals(Enumerations.AdministrativeGender.MALE, person.getPatientGender());
  }

  @Test
  void testGeburtsname() {
    // empty geburtsname [NO LOGGING]
    person.setGeburtsname("");
    assertEmptyValue(person.getPatientGeburtsname());
    // non empty
    String geburtsname = "Clemens";
    person.setGeburtsname(geburtsname);
    HumanName result = person.getPatientGeburtsname();
    assertHumanName(geburtsname, HumanName.NameUse.MAIDEN, result);
  }

  @Test
  void testNameFamilyNamenszusatz() {
    // empty namenszusatz [NO LOGGING]
    person.setNamenszusatz("");
    assertEmptyValue(person.getPatientNameFamilyNamenszusatz());
    // non-empty namenszusatz
    String namenszusatz = "RA";
    person.setNamenszusatz(namenszusatz);
    Extension result = person.getPatientNameFamilyNamenszusatz();
    assertExtensionWithStringType(namenszusatz, ExtensionUrl.NAMENSZUSATZ, result);
  }

  @Test
  void testNameFamilyNachname() {
    // empty nachnahme [NO LOGGING]
    person.setNachname("");
    assertEmptyValue(person.getPatientNameFamilyNachname());
    // non-empty nachname
    String nachname = "Van-der-Dussen";
    person.setNachname(nachname);
    Extension result = person.getPatientNameFamilyNachname();
    assertExtensionWithStringType(nachname, ExtensionUrl.NACHNAME, result);
  }

  @Test
  void testNameFamilyVorsatzwort() {
    // empty vorsatzwort [NO LOGGING]
    person.setVorsatzwort("");
    assertEmptyValue(person.getPatientNameFamilyVorsatzwort());
    // non-empty vorsatzwort
    String vorsatzwort = "bei der";
    person.setVorsatzwort(vorsatzwort);
    Extension result = person.getPatientNameFamilyVorsatzwort();
    assertExtensionWithStringType(vorsatzwort, ExtensionUrl.VORSATZWORT, result);
  }

  @Test
  void testNameFamily() {
    // empty familienname
    person.setVorsatzwort("");
    assertEmptyValue(person.getPatientNameFamily());
    // non-empty familienname
    String familienname = "Van-der-Dussen";
    person.setFamilienname(familienname);
    StringType result = person.getPatientNameFamily();
    assertNonEmptyValue(result);
    assertEquals(familienname, result.getValue());
    assertFalse(result.hasExtension());
    // non-empty familienname and nachname
    person.setFamilienname(familienname);
    person.setNachname(familienname);
    result = person.getPatientNameFamily();
    assertNonEmptyValue(result);
    assertEquals(familienname, result.getValue());
    assertEquals(1, result.getExtension().size());
    assertTrue(result.hasExtension(ExtensionUrl.NACHNAME));
  }

  @Test
  void testNameGiven() {
    // empty vorname
    person.setVorname("");
    assertEmptyValue(person.getPatientNameGiven());
    // non-empty vorname
    String firstName = "Maja", secondName = "Julia";
    String vorname = firstName + " " + secondName;
    person.setVorname(vorname);
    List<StringType> result = person.getPatientNameGiven();
    assertNonEmptyValue(result);
    assertEquals(2, result.size());
    assertEquals(firstName, result.get(0).getValue());
    assertEquals(secondName, result.get(1).getValue());
  }

  @Test
  void testNamePrefixQualifier() {
    // empty artDesPraefix [NO LOGGING]
    assertEmptyCodeValue(person::setArt_des_praefix, person::getPatientNamePrefixQualifier);
    // non-empty artDesPraefix
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

  @Test
  void testNamePrefix() {
    // empty praefix [NO LOGGING]
    person.setPraefix("");
    assertEmptyValue(person.getPatientNamePrefix());
    // non-empty praefix
    String praefix = "Prof. Dr. med.";
    person.setPraefix(praefix);
    StringType result = person.getPatientNamePrefix();
    assertNonEmptyValue(result);
    assertEquals(praefix, result.getValue());
    assertFalse(result.hasExtension());
    // non-empty praefix and artDesPraefix
    person.setPraefix(praefix);
    String artDesPraefix = "AC";
    person.setArt_des_praefix(artDesPraefix);
    result = person.getPatientNamePrefix();
    assertNonEmptyValue(result);
    assertEquals(praefix, result.getValue());
    assertEquals(1, result.getExtension().size());
    assertTrue(result.hasExtension(ExtensionUrl.PREFIX));
  }

  @Test
  void testName() {
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
}
