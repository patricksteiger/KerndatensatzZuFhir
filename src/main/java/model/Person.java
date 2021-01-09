package model;

import com.opencsv.bean.CsvBindByName;
import constants.*;
import enums.IdentifierTypeCode;
import enums.MIICoreLocations;
import enums.VersichertenCode;
import enums.VitalStatus;
import helper.FhirHelper;
import helper.Helper;
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
    // Identifier - PID
    if (Helper.checkNonEmptyString(this.getPatient_pid())) patient.addIdentifier(this.getPID());
    // Identifier - GKV
    if (Helper.checkNonEmptyString(this.getVersichertenId_gkv()))
      patient.addIdentifier(this.getGKV());
    // Identifier - PKV
    if (Helper.checkNonEmptyString(this.getVersichertennummer_pkv()))
      patient.addIdentifier(this.getPKV());
    // Name
    patient.addName(this.getName());
    // Geburtsname
    if (Helper.checkNonEmptyString(this.getGeburtsname())) patient.addName(this.getGeburtsName());
    // Administratives Geschlecht, returns UNKNOWN if gender isn't set
    patient.setGender(FhirHelper.getGenderMapping(this.getAdmininistratives_geschlecht()));
    // patient.addExtension(this.getGender());
    // Geburtsdatum
    patient.setBirthDate(Helper.getDateFromISO(this.getGeburtsdatum()));
    // patient.addExtension(this.getBirthDate());
    // Deceased
    patient.setDeceased(this.getDeceased());
    // Address
    this.getAddresses().forEach(patient::addAddress);
    // Managing organization
    patient.setManagingOrganization(this.getManagingOrganization());
    return patient;
  }

  public Meta getPatientMeta() {
    return FhirHelper.generateMeta(MetaProfile.PATIENT, MetaSource.PATIENT, MetaVersionId.PATIENT);
  }

  public Reference getManagingOrganization() {
    return new Reference().setReference("Organization/" + MIICoreLocations.UKU.toString());
  }

  public List<Address> getAddresses() {
    return Helper.listOf(this.getStrassenanschrift(), this.getPostfach());
  }

  public Address getStrassenanschrift() {
    Address.AddressType type = Address.AddressType.BOTH;
    String line = this.getStrasse();
    String city = this.getStrassenanschrift_wohnort();
    String postalCode = this.getStrassenanschrift_plz();
    String country = this.getStrassenanschrift_land();
    return FhirHelper.generateAddress(type, line, city, postalCode, country);
  }

  public Address getPostfach() {
    Address.AddressType type = Address.AddressType.POSTAL;
    String line = this.getPostfachnummer();
    String city = this.getPostfach_wohnort();
    String postalCode = this.getPostfach_plz();
    String country = this.getPostfach_land();
    return FhirHelper.generateAddress(type, line, city, postalCode, country);
  }

  public Type getDeceased() {
    if (Helper.checkNonEmptyString(this.getTodeszeitpunkt())) {
      Date deceasedTime = Helper.getDateFromISO(this.getTodeszeitpunkt());
      return FhirHelper.generateDate(deceasedTime);
    } else {
      boolean deceased = Helper.booleanFromString(this.getPatient_verstorben());
      return new BooleanType(deceased);
    }
  }

  public Extension getBirthDate() {
    Date birthDate = Helper.getDateFromISO(this.getGeburtsdatum());
    DateTimeType type = FhirHelper.generateDate(birthDate);
    return FhirHelper.generateExtension(ExtensionUrl.BIRTH_DATE, type);
  }

  public Extension getGender() {
    Enumerations.AdministrativeGender gender =
        FhirHelper.getGenderMapping(this.getAdmininistratives_geschlecht());
    Coding coding =
        FhirHelper.generateCoding(gender.name(), gender.getSystem(), gender.getDisplay());
    return FhirHelper.generateExtension(ExtensionUrl.GENDER, coding);
  }

  public HumanName getGeburtsName() {
    HumanName.NameUse use = HumanName.NameUse.MAIDEN;
    List<Extension> family = this.getMaidenFamily();
    return FhirHelper.generateHumanName(use, family);
  }

  public List<Extension> getMaidenFamily() {
    List<Extension> family = new ArrayList<>();
    if (Helper.checkNonEmptyString(this.getGeburtsname())) {
      StringType nachname = new StringType(this.getGeburtsname());
      family.add(FhirHelper.generateExtension(ExtensionUrl.NACHNAME, nachname));
    }
    return family;
  }

  public HumanName getName() {
    HumanName.NameUse use = HumanName.NameUse.OFFICIAL;
    List<Extension> family = this.getFamily();
    List<String> given = this.getGiven();
    List<Extension> artDesPrefix = this.getPrefix();
    String prefix = this.getPraefix();
    return FhirHelper.generateHumanName(use, family, given, artDesPrefix, prefix);
  }

  public List<Extension> getFamily() {
    List<Extension> family = new ArrayList<>();
    if (Helper.checkNonEmptyString(this.getNamenszusatz())) {
      StringType zusatz = new StringType(this.getNamenszusatz());
      family.add(FhirHelper.generateExtension(ExtensionUrl.NAMENSZUSATZ, zusatz));
    }
    if (Helper.checkNonEmptyString(this.getNachname())) {
      StringType nachname = new StringType(this.getNachname());
      family.add(FhirHelper.generateExtension(ExtensionUrl.NACHNAME, nachname));
    }
    if (Helper.checkNonEmptyString(this.getVorsatzwort())) {
      StringType vorsatzwort = new StringType(this.getVorsatzwort());
      family.add(FhirHelper.generateExtension(ExtensionUrl.VORSATZWORT, vorsatzwort));
    }
    return family;
  }

  public List<String> getGiven() {
    List<String> given = new ArrayList<>();
    if (Helper.checkNonEmptyString(this.getVorname())) given.add(this.getVorname());
    return given;
  }

  public List<Extension> getPrefix() {
    List<Extension> prefix = new ArrayList<>();
    if (Helper.checkNonEmptyString(this.getArt_des_praefix())) {
      StringType artDesPrefix = new StringType(this.getArt_des_praefix());
      prefix.add(FhirHelper.generateExtension(ExtensionUrl.PREFIX, artDesPrefix));
    }
    return prefix;
  }

  public Identifier getPID() {
    String value = this.getPatient_pid();
    String system = IdentifierSystem.PID;
    IdentifierTypeCode code = IdentifierTypeCode.MR;
    Coding pidCoding =
        FhirHelper.generateCoding(code.getCode(), CodingSystem.PID, code.getDisplay());
    CodeableConcept type = new CodeableConcept().addCoding(pidCoding);
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    Identifier.IdentifierUse use = Identifier.IdentifierUse.USUAL;
    return FhirHelper.generateIdentifier(value, system, type, assignerRef, use);
  }

  public Identifier getGKV() {
    String value = this.getVersichertenId_gkv();
    String system = IdentifierSystem.VERSICHERTEN_ID_GKV;
    VersichertenCode gkv = VersichertenCode.GKV;
    Coding gkvCoding =
        FhirHelper.generateCoding(gkv.getCode(), CodingSystem.IDENTIFIER_TYPE_DE, gkv.getDisplay());
    CodeableConcept type = new CodeableConcept().addCoding(gkvCoding);
    Reference assignerRef = FhirHelper.getOrganizationAssignerReference();
    Identifier.IdentifierUse use = Identifier.IdentifierUse.OFFICIAL;
    return FhirHelper.generateIdentifier(value, system, type, assignerRef, use);
  }

  public Identifier getPKV() {
    String value = this.getVersichertennummer_pkv();
    String system = IdentifierSystem.VERSICHERTEN_ID_GKV;
    VersichertenCode pkv = VersichertenCode.PKV;
    Coding pkvCoding =
        FhirHelper.generateCoding(pkv.getCode(), CodingSystem.IDENTIFIER_TYPE_DE, pkv.getDisplay());
    CodeableConcept type = new CodeableConcept().addCoding(pkvCoding);
    Reference assignerRef = FhirHelper.getOrganizationAssignerReference();
    Identifier.IdentifierUse use = Identifier.IdentifierUse.SECONDARY;
    return FhirHelper.generateIdentifier(value, system, type, assignerRef, use);
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
    if (Helper.checkNonEmptyString(this.getPatNr()))
      researchSubject.setIndividual(this.getResearchSubjectIndividual());
    // Consent
    if (Helper.checkNonEmptyString(this.getRechtsgrundlage()))
      researchSubject.setConsent(this.getResearchSubjectConsent());
    return researchSubject;
  }

  public Meta getResearchSubjectMeta() {
    return FhirHelper.generateMeta(
        MetaProfile.RESEARCH_SUBJECT, MetaSource.RESEARCH_SUBJECT, MetaVersionId.RESEARCH_SUBJECT);
  }

  public Reference getResearchSubjectConsent() {
    String type = ReferenceType.CONSENT;
    // FIXME: System is still missing!
    Identifier identifier = FhirHelper.generateIdentifier(this.getRechtsgrundlage(), "");
    return FhirHelper.generateReference(type, identifier);
  }

  public Reference getResearchSubjectIndividual() {
    String type = ReferenceType.PATIENT;
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    Identifier subjectId =
        FhirHelper.generateIdentifier(this.getPatNr(), IdentifierSystem.LOCAL_PID, assignerRef);
    return FhirHelper.generateReference(type, subjectId);
  }

  public Period getResearchSubjectPeriod() {
    Date start = Helper.getDateFromISO(this.getTeilnahme_beginn());
    if (Helper.checkNonEmptyString(this.getTeilnahme_ende())) {
      Date end = Helper.getDateFromISO(this.getTeilnahme_ende());
      return FhirHelper.generatePeriod(start, end);
    }
    return FhirHelper.generatePeriod(start);
  }

  public ResearchSubject.ResearchSubjectStatus getStatus() {
    return ResearchSubject.ResearchSubjectStatus.CANDIDATE;
  }

  public Identifier getSubjectIdentificationCode() {
    String value = this.getSubjekt_identifizierungscode();
    String system = IdentifierSystem.SUBJECT_IDENTIFICATION_CODE;
    IdentifierTypeCode code = IdentifierTypeCode.RI;
    Coding coding =
        FhirHelper.generateCoding(
            code.getCode(), CodingSystem.IDENTIFIER_TYPE_DE, code.getDisplay());
    CodeableConcept type = new CodeableConcept().addCoding(coding);
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    Identifier.IdentifierUse use = Identifier.IdentifierUse.USUAL;
    return FhirHelper.generateIdentifier(value, system, type, assignerRef, use);
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
    if (Helper.checkNonEmptyString(this.getPatNr()))
      observation.setSubject(this.getObservationSubject());
    // Effective
    observation.setEffective(this.getObservationEffective());
    // Value
    observation.setValue(this.getObservationValue());
    return observation;
  }

  public Meta getObservationMeta() {
    return FhirHelper.generateMeta(
        MetaProfile.OBSERVATION, MetaSource.OBSERVATION, MetaVersionId.OBSERVATION);
  }

  public Reference getObservationSubject() {
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    Identifier subjectId =
        FhirHelper.generateIdentifier(this.getPatNr(), IdentifierSystem.LOCAL_PID, assignerRef);
    return FhirHelper.generateReference(subjectId);
  }

  public Coding getObservationValue() {
    VitalStatus code = VitalStatus.UNBEKANNT;
    if (Helper.checkNonEmptyString(this.getPatient_verstorben())) {
      try {
        boolean verstorben = Helper.booleanFromString(this.getPatient_verstorben());
        if (verstorben) code = VitalStatus.VERSTORBEN;
      } catch (IllegalArgumentException e) {
      }
    }
    String system = CodingSystem.VITALSTATUS;
    return FhirHelper.generateCoding(code.getCode(), system, code.getDisplay());
  }

  public DateTimeType getObservationEffective() {
    Date effective = Helper.getDateFromISO(this.getLetzter_lebendzeitpunkt());
    return FhirHelper.generateDate(effective);
  }

  public CodeableConcept getObservationCode() {
    String code = CodingCode.LOINC_OBSERVATION;
    String system = CodingSystem.LOINC;
    Coding loinc = FhirHelper.generateCoding(code, system);
    CodeableConcept observationCode = new CodeableConcept().addCoding(loinc);
    return observationCode;
  }

  public CodeableConcept getObservationCategory() {
    String code = CodingCode.SURVEY;
    Coding survey = FhirHelper.generateCoding(code, CodingSystem.OBSERVATION_CATEGORY);
    CodeableConcept category = new CodeableConcept().addCoding(survey);
    return category;
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
