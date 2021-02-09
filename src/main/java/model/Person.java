package model;

import com.opencsv.bean.CsvBindByName;
import constants.*;
import enums.IdentifierTypeCode;
import enums.MIICoreLocations;
import enums.VersichertenCode;
import enums.VitalStatus;
import helper.FhirGenerator;
import helper.FhirHelper;
import helper.Helper;
import helper.ParsedCode;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Person implements Datablock {
  @CsvBindByName private String patNr;
  // Name
  @CsvBindByName private String vorname;
  @CsvBindByName private String nachname;
  @CsvBindByName private String familienname;
  @CsvBindByName private String vorsatzwort;
  @CsvBindByName private String namenszusatz;
  @CsvBindByName private String praefix;
  @CsvBindByName private String art_des_praefix;
  @CsvBindByName private String geburtsname;
  // Demographie
  @CsvBindByName private String admininistratives_geschlecht;
  @CsvBindByName private String geburtsdatum;
  // Demographie - Adresse
  @CsvBindByName private String postfachnummer;
  @CsvBindByName private String postfach_wohnort;
  @CsvBindByName private String postfach_plz;
  @CsvBindByName private String postfach_land;
  @CsvBindByName private String strassenanschrift_land;
  @CsvBindByName private String strassenanschrift_plz;
  @CsvBindByName private String strassenanschrift_wohnort;
  @CsvBindByName private String strasse;
  // Demographie - Vitalstatus
  @CsvBindByName private String patient_verstorben;
  @CsvBindByName private String todeszeitpunkt;
  @CsvBindByName private String informationsquelle;
  @CsvBindByName private String letzter_lebendzeitpunkt;
  // PatientIn
  @CsvBindByName private String patient_pid;
  @CsvBindByName private String patient_pid_kontext;
  @CsvBindByName private String versichertenId_gkv;
  @CsvBindByName private String versichertennummer_pkv;
  @CsvBindByName private String institutionskennzeichen_krankenkasse;
  @CsvBindByName private String versicherungstyp;
  // ProbandIn
  @CsvBindByName private String bezeichnung_studie;
  @CsvBindByName private String subjekt_identifizierungscode;
  @CsvBindByName private String rechtsgrundlage;
  @CsvBindByName private String teilnahme_beginn;
  @CsvBindByName private String teilnahme_ende;
  @CsvBindByName private String teilnahme_status;

  @Override
  public List<Resource> toFhirResources() {
    return Helper.listOf(this.getPatient(), this.getResearchSubject(), this.getObservation());
  }

  public Patient getPatient() {
    Patient patient = new Patient();
    // Meta
    patient.setMeta(this.getPatientMeta());
    // Identifier
    this.getPatientIdentifiers().forEach(patient::addIdentifier);
    // Name
    patient.addName(this.getPatientName());
    // Geburtsname
    patient.addName(this.getPatientGeburtsName());
    // Administratives Geschlecht, returns UNKNOWN if gender isn't set
    patient.setGender(this.getPatientGender());
    // Geburtsdatum
    patient.setBirthDate(this.getPatientBirthDate());
    // Deceased
    patient.setDeceased(this.getPatientDeceased());
    // Address
    this.getPatientAddresses().forEach(patient::addAddress);
    // Managing organization
    patient.setManagingOrganization(this.getPatientManagingOrganization());
    return patient;
  }

  public ResearchSubject getResearchSubject() {
    ResearchSubject researchSubject = new ResearchSubject();
    // Meta
    researchSubject.setMeta(this.getResearchSubjectMeta());
    // Subject identification code
    researchSubject.addIdentifier(this.getSubjectIdentificationCode());
    // Status
    researchSubject.setStatus(this.getStatus());
    // Period
    researchSubject.setPeriod(this.getResearchSubjectPeriod());
    // Individual
    researchSubject.setIndividual(this.getResearchSubjectIndividual());
    // Consent
    researchSubject.setConsent(this.getResearchSubjectConsent());
    // Study (optional)
    researchSubject.setStudy(this.getResearchSubjectStudy());
    return researchSubject;
  }

  public Observation getObservation() {
    Observation observation = new Observation();
    // Meta
    observation.setMeta(this.getObservationMeta());
    // Status
    observation.setStatus(this.getObservationStatus());
    // Category
    observation.addCategory(this.getObservationCategory());
    // Code
    observation.setCode(this.getObservationCode());
    // Subject
    observation.setSubject(this.getObservationSubject());
    // Effective
    observation.setEffective(this.getObservationEffective());
    // Value
    observation.setValue(this.getObservationValue());
    return observation;
  }

  public List<Identifier> getPatientIdentifiers() {
    List<Identifier> identifiers =
        Helper.listOf(this.getPatientGKV(), this.getPatientPKV(), this.getPatientPID());
    if (Helper.checkAllNull(identifiers)) {
      throw new IllegalStateException(
          "Person: At least 1 identifier out of PID, GKV and PKV needs to be set");
    }
    return identifiers;
  }

  public Meta getPatientMeta() {
    return FhirGenerator.meta(
        MetaProfile.PERSON_PATIENT, MetaSource.PERSON_PATIENT, MetaVersionId.PERSON_PATIENT);
  }

  public Reference getPatientManagingOrganization() {
    return new Reference().setReference("Organization/" + MIICoreLocations.UKU.toString());
  }

  public List<Address> getPatientAddresses() {
    return Helper.listOf(
        this.getPatientAddressStrassenanschrift(), this.getPatientAddressPostfach());
  }

  public Address getPatientAddressStrassenanschrift() {
    Address.AddressType type = Address.AddressType.BOTH;
    String line = this.getStrasse();
    String city = this.getStrassenanschrift_wohnort();
    String postalCode = this.getStrassenanschrift_plz();
    String country = this.getStrassenanschrift_land();
    return FhirGenerator.address(type, line, city, postalCode, country);
  }

  public Address getPatientAddressPostfach() {
    Address.AddressType type = Address.AddressType.POSTAL;
    String line = this.getPostfachnummer();
    String city = this.getPostfach_wohnort();
    String postalCode = this.getPostfach_plz();
    String country = this.getPostfach_land();
    return FhirGenerator.address(type, line, city, postalCode, country);
  }

  public Type getPatientDeceased() {
    String todeszeitpunkt = this.getTodeszeitpunkt();
    if (Helper.checkNonEmptyString(todeszeitpunkt)) {
      Date deceasedTime = Helper.getDateFromISO(todeszeitpunkt);
      return FhirGenerator.dateTimeType(deceasedTime);
    } else {
      boolean deceased = Helper.booleanFromString(this.getPatient_verstorben());
      return new BooleanType(deceased);
    }
  }

  public Date getPatientBirthDate() {
    String birthDate = this.getGeburtsdatum();
    if (Helper.checkEmptyString(birthDate)) {
      return null;
    }
    return Helper.getDateFromISO(birthDate);
  }

  public Enumerations.AdministrativeGender getPatientGender() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getAdmininistratives_geschlecht());
    String gender = parsedCode.getCode();
    if (Helper.checkEmptyString(gender)) {
      return null;
    }
    return FhirHelper.getGenderMapping(gender);
  }

  public HumanName getPatientGeburtsName() {
    if (Helper.checkEmptyString(this.getGeburtsname())) {
      return null;
    }
    HumanName.NameUse use = HumanName.NameUse.MAIDEN;
    List<Extension> family = this.getPatientNameMaidenFamily();
    return FhirGenerator.humanName(use, family);
  }

  public List<Extension> getPatientNameMaidenFamily() {
    List<Extension> family = new ArrayList<>();
    if (Helper.checkNonEmptyString(this.getGeburtsname())) {
      StringType nachname = new StringType(this.getGeburtsname());
      family.add(FhirGenerator.extension(ExtensionUrl.NACHNAME, nachname));
    }
    return family;
  }

  public HumanName getPatientName() {
    HumanName.NameUse use = HumanName.NameUse.OFFICIAL;
    List<Extension> family = this.getPatientNameFamily();
    List<String> given = this.getPatientNameGiven();
    List<Extension> artDesPrefix = this.getPatientNamePrefix();
    String prefix = this.getPraefix();
    return FhirGenerator.humanName(use, family, given, artDesPrefix, prefix);
  }

  public List<Extension> getPatientNameFamily() {
    List<Extension> family = new ArrayList<>();
    if (Helper.checkNonEmptyString(this.getNamenszusatz())) {
      StringType zusatz = new StringType(this.getNamenszusatz());
      family.add(FhirGenerator.extension(ExtensionUrl.NAMENSZUSATZ, zusatz));
    }
    if (Helper.checkNonEmptyString(this.getNachname())) {
      StringType nachname = new StringType(this.getNachname());
      family.add(FhirGenerator.extension(ExtensionUrl.NACHNAME, nachname));
    }
    if (Helper.checkNonEmptyString(this.getVorsatzwort())) {
      StringType vorsatzwort = new StringType(this.getVorsatzwort());
      family.add(FhirGenerator.extension(ExtensionUrl.VORSATZWORT, vorsatzwort));
    }
    return family;
  }

  public List<String> getPatientNameGiven() {
    List<String> given = new ArrayList<>();
    if (Helper.checkNonEmptyString(this.getVorname())) given.add(this.getVorname());
    return given;
  }

  public List<Extension> getPatientNamePrefix() {
    List<Extension> prefix = new ArrayList<>();
    ParsedCode parsedCode = ParsedCode.fromString(this.getArt_des_praefix());
    String code = parsedCode.getCode();
    if (Helper.checkNonEmptyString(code)) {
      StringType artDesPrefix = new StringType(code);
      prefix.add(FhirGenerator.extension(ExtensionUrl.PREFIX, artDesPrefix));
    }
    return prefix;
  }

  public Identifier getPatientPID() {
    String value = this.getPatient_pid();
    if (Helper.checkEmptyString(value)) {
      return null;
    }
    IdentifierTypeCode code = IdentifierTypeCode.MR;
    Coding pidCoding = FhirGenerator.coding(code);
    CodeableConcept type = FhirGenerator.codeableConcept(pidCoding);
    String system = IdentifierSystem.PID;
    Reference assignerRef = this.getPatientOrganizationReference();
    Identifier.IdentifierUse use = Identifier.IdentifierUse.USUAL;
    return FhirGenerator.identifier(value, system, type, assignerRef, use);
  }

  public Identifier getPatientGKV() {
    String value = this.getVersichertenId_gkv();
    if (Helper.checkEmptyString(value)) {
      return null;
    }
    VersichertenCode gkv = VersichertenCode.GKV;
    Coding gkvCoding = FhirGenerator.coding(gkv);
    CodeableConcept type = FhirGenerator.codeableConcept(gkvCoding);
    String system = IdentifierSystem.VERSICHERTEN_ID_GKV;
    Reference assignerRef = this.getPatientOrganizationReference();
    Identifier.IdentifierUse use = Identifier.IdentifierUse.OFFICIAL;
    return FhirGenerator.identifier(value, system, type, assignerRef, use);
  }

  public Identifier getPatientPKV() {
    String value = this.getVersichertennummer_pkv();
    if (Helper.checkEmptyString(value)) {
      return null;
    }
    String system = IdentifierSystem.VERSICHERTEN_ID_GKV;
    VersichertenCode pkv = VersichertenCode.PKV;
    Coding pkvCoding = FhirGenerator.coding(pkv);
    CodeableConcept type = new CodeableConcept().addCoding(pkvCoding);
    Reference assignerRef = this.getPatientOrganizationReference();
    Identifier.IdentifierUse use = Identifier.IdentifierUse.SECONDARY;
    return FhirGenerator.identifier(value, system, type, assignerRef, use);
  }

  public Reference getPatientOrganizationReference() {
    String type = ReferenceType.ORGANIZATION;
    // Identifier
    Identifier.IdentifierUse use = Identifier.IdentifierUse.OFFICIAL;
    String system = IdentifierSystem.ORGANIZATION_REFERENCE_ID;
    IdentifierTypeCode identifierTypeCode = IdentifierTypeCode.XX;
    Coding coding = FhirGenerator.coding(identifierTypeCode);
    CodeableConcept identifierType = new CodeableConcept().addCoding(coding);
    String identifierValue = this.getInstitutionskennzeichen_krankenkasse();
    Identifier identifier =
        FhirGenerator.identifier(identifierValue, system, identifierType, null, use);
    return FhirGenerator.reference(type, identifier);
  }

  public Meta getResearchSubjectMeta() {
    return FhirGenerator.meta(
        MetaProfile.PERSON_RESEARCH_SUBJECT,
        MetaSource.PERSON_RESEARCH_SUBJECT,
        MetaVersionId.PERSON_RESEARCH_SUBJECT);
  }

  public Reference getResearchSubjectConsent() {
    String ref = MIIReference.CONSENT_MII;
    return FhirGenerator.reference(ref);
  }

  public Reference getResearchSubjectIndividual() {
    String ref = MIIReference.PATIENT_MII;
    return FhirGenerator.reference(ref);
  }

  public Reference getResearchSubjectStudy() {
    String ref = MIIReference.RESEARCH_STUDY_MII;
    return FhirGenerator.reference(ref);
  }

  public Period getResearchSubjectPeriod() {
    Date start = Helper.getDateFromISO(this.getTeilnahme_beginn());
    if (Helper.checkNonEmptyString(this.getTeilnahme_ende())) {
      Date end = Helper.getDateFromISO(this.getTeilnahme_ende());
      return FhirGenerator.period(start, end);
    }
    return FhirGenerator.period(start);
  }

  public ResearchSubject.ResearchSubjectStatus getStatus() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getTeilnahme_status());
    try {
      return ResearchSubject.ResearchSubjectStatus.fromCode(parsedCode.getCode());
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Status \"" + this.getTeilnahme_status() + "\" is not a valid Teilnahmestatus");
    }
  }

  public Identifier getSubjectIdentificationCode() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getSubjekt_identifizierungscode());
    String value = parsedCode.getCode();
    String system = IdentifierSystem.SUBJECT_IDENTIFICATION_CODE;
    IdentifierTypeCode code = IdentifierTypeCode.RI;
    Coding coding = FhirGenerator.coding(code);
    CodeableConcept type = FhirGenerator.codeableConcept(coding);
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    Identifier.IdentifierUse use = Identifier.IdentifierUse.USUAL;
    return FhirGenerator.identifier(value, system, type, assignerRef, use);
  }

  public Meta getObservationMeta() {
    return FhirGenerator.meta(
        MetaProfile.PERSON_OBSERVATION,
        MetaSource.PERSON_OBSERVATION,
        MetaVersionId.PERSON_OBSERVATION);
  }

  public Reference getObservationSubject() {
    String patientNummer = this.getPatNr();
    if (Helper.checkEmptyString(patientNummer)) {
      throw new IllegalStateException("Person: patNr needs to be non-empty");
    }
    String ref = MIIReference.getPatient(patientNummer);
    return FhirGenerator.reference(ref);
  }

  public Coding getObservationValue() {
    VitalStatus code = VitalStatus.UNBEKANNT;
    String patientVerstorben = this.getPatient_verstorben();
    if (Helper.checkNonEmptyString(patientVerstorben)) {
      boolean verstorben = Helper.booleanFromString(patientVerstorben);
      if (verstorben) {
        code = VitalStatus.VERSTORBEN;
      }
    }
    return FhirGenerator.coding(code);
  }

  public DateTimeType getObservationEffective() {
    Date effective = Helper.getDateFromISO(this.getLetzter_lebendzeitpunkt());
    return FhirGenerator.dateTimeType(effective);
  }

  public CodeableConcept getObservationCode() {
    String code = CodingCode.LOINC_OBSERVATION;
    String system = CodingSystem.LOINC;
    Coding loinc = FhirGenerator.coding(code, system);
    return FhirGenerator.codeableConcept(loinc);
  }

  public CodeableConcept getObservationCategory() {
    String code = CodingCode.SURVEY;
    String system = CodingSystem.OBSERVATION_CATEGORY;
    Coding survey = FhirGenerator.coding(code, system);
    return FhirGenerator.codeableConcept(survey);
  }

  public Observation.ObservationStatus getObservationStatus() {
    return Observation.ObservationStatus.FINAL;
  }

  public String getPatNr() {
    return patNr;
  }

  public void setPatNr(String patNr) {
    this.patNr = patNr;
  }

  public String getVorname() {
    return vorname;
  }

  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  public String getNachname() {
    return nachname;
  }

  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  public String getFamilienname() {
    return familienname;
  }

  public void setFamilienname(String familienname) {
    this.familienname = familienname;
  }

  public String getVorsatzwort() {
    return vorsatzwort;
  }

  public void setVorsatzwort(String vorsatzwort) {
    this.vorsatzwort = vorsatzwort;
  }

  public String getNamenszusatz() {
    return namenszusatz;
  }

  public void setNamenszusatz(String namenszusatz) {
    this.namenszusatz = namenszusatz;
  }

  public String getPraefix() {
    return praefix;
  }

  public void setPraefix(String praefix) {
    this.praefix = praefix;
  }

  public String getArt_des_praefix() {
    return art_des_praefix;
  }

  public void setArt_des_praefix(String art_des_praefix) {
    this.art_des_praefix = art_des_praefix;
  }

  public String getGeburtsname() {
    return geburtsname;
  }

  public void setGeburtsname(String geburtsname) {
    this.geburtsname = geburtsname;
  }

  public String getAdmininistratives_geschlecht() {
    return admininistratives_geschlecht;
  }

  public void setAdmininistratives_geschlecht(String admininistratives_geschlecht) {
    this.admininistratives_geschlecht = admininistratives_geschlecht;
  }

  public String getGeburtsdatum() {
    return geburtsdatum;
  }

  public void setGeburtsdatum(String geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }

  public String getPostfachnummer() {
    return postfachnummer;
  }

  public void setPostfachnummer(String postfachnummer) {
    this.postfachnummer = postfachnummer;
  }

  public String getPostfach_wohnort() {
    return postfach_wohnort;
  }

  public void setPostfach_wohnort(String postfach_wohnort) {
    this.postfach_wohnort = postfach_wohnort;
  }

  public String getPostfach_plz() {
    return postfach_plz;
  }

  public void setPostfach_plz(String postfach_plz) {
    this.postfach_plz = postfach_plz;
  }

  public String getPostfach_land() {
    return postfach_land;
  }

  public void setPostfach_land(String postfach_land) {
    this.postfach_land = postfach_land;
  }

  public String getStrassenanschrift_land() {
    return strassenanschrift_land;
  }

  public void setStrassenanschrift_land(String strassenanschrift_land) {
    this.strassenanschrift_land = strassenanschrift_land;
  }

  public String getStrassenanschrift_plz() {
    return strassenanschrift_plz;
  }

  public void setStrassenanschrift_plz(String strassenanschrift_plz) {
    this.strassenanschrift_plz = strassenanschrift_plz;
  }

  public String getStrassenanschrift_wohnort() {
    return strassenanschrift_wohnort;
  }

  public void setStrassenanschrift_wohnort(String strassenanschrift_wohnort) {
    this.strassenanschrift_wohnort = strassenanschrift_wohnort;
  }

  public String getStrasse() {
    return strasse;
  }

  public void setStrasse(String strasse) {
    this.strasse = strasse;
  }

  public String getPatient_verstorben() {
    return patient_verstorben;
  }

  public void setPatient_verstorben(String patient_verstorben) {
    this.patient_verstorben = patient_verstorben;
  }

  public String getTodeszeitpunkt() {
    return todeszeitpunkt;
  }

  public void setTodeszeitpunkt(String todeszeitpunkt) {
    this.todeszeitpunkt = todeszeitpunkt;
  }

  public String getInformationsquelle() {
    return informationsquelle;
  }

  public void setInformationsquelle(String informationsquelle) {
    this.informationsquelle = informationsquelle;
  }

  public String getLetzter_lebendzeitpunkt() {
    return letzter_lebendzeitpunkt;
  }

  public void setLetzter_lebendzeitpunkt(String letzter_lebendzeitpunkt) {
    this.letzter_lebendzeitpunkt = letzter_lebendzeitpunkt;
  }

  public String getPatient_pid() {
    return patient_pid;
  }

  public void setPatient_pid(String patient_pid) {
    this.patient_pid = patient_pid;
  }

  public String getPatient_pid_kontext() {
    return patient_pid_kontext;
  }

  public void setPatient_pid_kontext(String patient_pid_kontext) {
    this.patient_pid_kontext = patient_pid_kontext;
  }

  public String getVersichertenId_gkv() {
    return versichertenId_gkv;
  }

  public void setVersichertenId_gkv(String versichertenId_gkv) {
    this.versichertenId_gkv = versichertenId_gkv;
  }

  public String getVersichertennummer_pkv() {
    return versichertennummer_pkv;
  }

  public void setVersichertennummer_pkv(String versichertennummer_pkv) {
    this.versichertennummer_pkv = versichertennummer_pkv;
  }

  public String getInstitutionskennzeichen_krankenkasse() {
    return institutionskennzeichen_krankenkasse;
  }

  public void setInstitutionskennzeichen_krankenkasse(String institutionskennzeichen_krankenkasse) {
    this.institutionskennzeichen_krankenkasse = institutionskennzeichen_krankenkasse;
  }

  public String getVersicherungstyp() {
    return versicherungstyp;
  }

  public void setVersicherungstyp(String versicherungstyp) {
    this.versicherungstyp = versicherungstyp;
  }

  public String getBezeichnung_studie() {
    return bezeichnung_studie;
  }

  public void setBezeichnung_studie(String bezeichnung_studie) {
    this.bezeichnung_studie = bezeichnung_studie;
  }

  public String getSubjekt_identifizierungscode() {
    return subjekt_identifizierungscode;
  }

  public void setSubjekt_identifizierungscode(String subjekt_identifizierungscode) {
    this.subjekt_identifizierungscode = subjekt_identifizierungscode;
  }

  public String getRechtsgrundlage() {
    return rechtsgrundlage;
  }

  public void setRechtsgrundlage(String rechtsgrundlage) {
    this.rechtsgrundlage = rechtsgrundlage;
  }

  public String getTeilnahme_beginn() {
    return teilnahme_beginn;
  }

  public void setTeilnahme_beginn(String teilnahme_beginn) {
    this.teilnahme_beginn = teilnahme_beginn;
  }

  public String getTeilnahme_ende() {
    return teilnahme_ende;
  }

  public void setTeilnahme_ende(String teilnahme_ende) {
    this.teilnahme_ende = teilnahme_ende;
  }

  public String getTeilnahme_status() {
    return teilnahme_status;
  }

  public void setTeilnahme_status(String teilnahme_status) {
    this.teilnahme_status = teilnahme_status;
  }
}
