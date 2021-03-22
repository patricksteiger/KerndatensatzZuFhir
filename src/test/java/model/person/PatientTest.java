package model.person;

import constants.IdentifierSystem;
import enums.IdentifierTypeCode;
import enums.MIICoreLocations;
import enums.VersichertenCode;
import helper.Logger;
import model.Person;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static util.Asserter.*;
import static util.Util.*;

class PatientTest {
  private Person person;

  @BeforeEach
  public void setUp() throws Exception {
    Logger LOGGER = Mockito.mock(Logger.class);
    setUpLoggerMock(LOGGER);
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
}
