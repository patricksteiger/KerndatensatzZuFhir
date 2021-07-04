package model;

import com.opencsv.bean.CsvBindByName;
import constants.Constants;
import constants.*;
import helper.*;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;
import valueSets.IdentifierTypeCode;
import valueSets.MIICoreLocations;
import valueSets.VersichertenCode;
import valueSets.VitalStatus;

import java.util.Date;
import java.util.List;

import static helper.FhirParser.*;

public class Person implements Datablock {
  private final Logger LOGGER = new Logger(Person.class);
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
    patient.addName(this.getPatientGeburtsname());
    // Administratives Geschlecht, returns UNKNOWN if gender isn't set
    patient.setGender(this.getPatientGender());
    // Geburtsdatum
    patient.setBirthDate(this.getPatientBirthDate());
    // Deceased (optional)
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
    researchSubject.addIdentifier(this.getResearchSubjectSubjectIdentificationCode());
    // Status
    researchSubject.setStatus(this.getResearchSubjectStatus());
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
      LOGGER.error("getPatientIdentifiers", "At least 1 of GKV, PKV or PID has to be set!");
    }
    return identifiers;
  }

  public Meta getPatientMeta() {
    return FhirGenerator.meta(
        MetaProfile.PERSON_PATIENT, MetaSource.PERSON_PATIENT, MetaVersionId.PERSON_PATIENT);
  }

  public Reference getPatientManagingOrganization() {
    String ref = MIIReference.MII_ORGANIZATION;
    return FhirGenerator.reference(ref);
  }

  public List<Address> getPatientAddresses() {
    List<Address> addresses =
        Helper.listOf(this.getPatientAddressStrassenanschrift(), this.getPatientAddressPostfach());
    if (Helper.checkAllNull(addresses)) {
      LOGGER.error(
          "getPatientAddresses", "At least 1 of Strassenaschrift and Postfach needs to be set!");
    }
    return addresses;
  }

  public Address getPatientAddressStrassenanschrift() {
    Address.AddressType type = Address.AddressType.BOTH;
    String line = this.getStrasse();
    String city = this.getStrassenanschrift_wohnort();
    String postalCode = this.getStrassenanschrift_plz();
    String country = this.getStrassenanschrift_land();
    LoggingData data =
        LoggingData.withMessage(
            LOGGER,
            "getPatientAddressStrassenanschrift",
            "All values for line, city, postalCode and country have to be set!");
    return optionalAddress(line, city, postalCode, country, type, data);
  }

  public Address getPatientAddressPostfach() {
    Address.AddressType type = Address.AddressType.POSTAL;
    String line = this.getPostfachnummer();
    String city = this.getPostfach_wohnort();
    String postalCode = this.getPostfach_plz();
    String country = this.getPostfach_land();
    LoggingData data =
        LoggingData.withMessage(
            LOGGER,
            "getPatientAddressPostfach",
            "All values for line, city, postalCode and country have to be set!");
    return optionalAddress(line, city, postalCode, country, type, data);
  }

  public Type getPatientDeceased() {
    String todeszeitpunkt = this.getTodeszeitpunkt();
    LoggingData zeitData = LoggingData.of(LOGGER, "getPatientDeceased", "todeszeitpunkt");
    String verstorben = this.getPatient_verstorben();
    LoggingData versData = LoggingData.of(LOGGER, "getPatientDeceased", "patient_verstorben");
    return optionalDateTimeTypeOrBooleanType(todeszeitpunkt, verstorben, zeitData, versData);
  }

  public Date getPatientBirthDate() {
    String birthDate = this.getGeburtsdatum();
    LoggingData data = LoggingData.of(LOGGER, "getPatientBirthDate", "geburtsdatum");
    return date(birthDate, data);
  }

  // TODO: Refactor gender mapping
  public Enumerations.AdministrativeGender getPatientGender() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getAdmininistratives_geschlecht());
    return parsedCode.hasEmptyCode()
        ? LOGGER.emptyValue("getPatientGender", "admininistratives_geschlecht")
        : FhirHelper.getGenderMapping(parsedCode.getCode());
  }

  public HumanName getPatientGeburtsname() {
    String geburtsName = this.getGeburtsname();
    HumanName.NameUse use = HumanName.NameUse.MAIDEN;
    return optionalHumanName(geburtsName, use);
  }

  public HumanName getPatientName() {
    StringType family = this.getPatientNameFamily();
    List<StringType> given = this.getPatientNameGiven();
    StringType prefix = this.getPatientNamePrefix();
    HumanName.NameUse use = HumanName.NameUse.OFFICIAL;
    return FhirGenerator.humanName(family, given, prefix, use);
  }

  public StringType getPatientNameFamily() {
    String familyName = this.getFamilienname();
    Extension namenszusatzExt = this.getPatientNameFamilyNamenszusatz();
    Extension nachnameExt = this.getPatientNameFamilyNachname();
    Extension vorsatzwortExt = this.getPatientNameFamilyVorsatzwort();
    LoggingData data = LoggingData.of(LOGGER, "getPatientNameFamily", "familienname");
    return stringTypeWithExtensions(familyName, data, namenszusatzExt, nachnameExt, vorsatzwortExt);
  }

  public Extension getPatientNameFamilyNamenszusatz() {
    String namensZusatz = this.getNamenszusatz();
    String url = ExtensionUrl.NAMENSZUSATZ;
    return optionalExtensionWithStringType(namensZusatz, url);
  }

  public Extension getPatientNameFamilyNachname() {
    String nachName = this.getNachname();
    String url = ExtensionUrl.NACHNAME;
    return optionalExtensionWithStringType(nachName, url);
  }

  public Extension getPatientNameFamilyVorsatzwort() {
    String vorsatzWort = this.getVorsatzwort();
    String url = ExtensionUrl.VORSATZWORT;
    return optionalExtensionWithStringType(vorsatzWort, url);
  }

  // TODO: Do we have to split vorname into several names? E.g.: "Maja Julia"
  public List<StringType> getPatientNameGiven() {
    String vorName = this.getVorname();
    LoggingData data = LoggingData.of(LOGGER, "getPatientNameGiven", "vorname");
    return stringTypeListFromName(vorName, data);
  }

  public StringType getPatientNamePrefix() {
    String prefix = this.getPraefix();
    Extension prefixQualifier = this.getPatientNamePrefixQualifier();
    return optionalStringTypeWithExtension(prefix, prefixQualifier);
  }

  public Extension getPatientNamePrefixQualifier() {
    String artDesPraefix = this.getArt_des_praefix();
    String url = ExtensionUrl.PREFIX;
    return optionalExtensionWithCodeType(artDesPraefix, url);
  }

  // TODO: Is system of PID correct?
  public Identifier getPatientPID() {
    String value = this.getPatient_pid();
    String system = IdentifierSystem.PID;
    Identifier.IdentifierUse use = Identifier.IdentifierUse.USUAL;
    IdentifierTypeCode mr = IdentifierTypeCode.MR;
    Reference assignerRef = this.getPIDAssignerReference();
    return optionalIdentifierFromSystemWithCodeAndReference(value, system, use, mr, assignerRef);
  }

  // TODO: Could be IKNR or CORE-LOCATION?
  public Reference getPIDAssignerReference() {
    MIICoreLocations ulm = MIICoreLocations.UKU;
    String system = IdentifierSystem.CORE_LOCATIONS;
    Identifier identifier = FhirGenerator.identifier(ulm.name(), system);
    return FhirGenerator.reference(identifier, ulm.toString());
  }

  public Identifier getPatientGKV() {
    String value = this.getVersichertenId_gkv();
    String system = IdentifierSystem.VERSICHERTEN_ID_GKV;
    Identifier.IdentifierUse use = Identifier.IdentifierUse.OFFICIAL;
    VersichertenCode gkv = VersichertenCode.GKV;
    Reference assignerRef = this.getGKVAssignerReference();
    return optionalIdentifierFromSystemWithCodeAndReference(value, system, use, gkv, assignerRef);
  }

  public Reference getGKVAssignerReference() {
    Identifier.IdentifierUse use = Identifier.IdentifierUse.OFFICIAL;
    String system = IdentifierSystem.IKNR;
    String identifierValue = this.getInstitutionskennzeichen_krankenkasse();
    LoggingData data =
        LoggingData.of(LOGGER, "getGKVAssignerReference", "institutionskennzeichen_krankenkasse");
    return referenceWithIdentifierFromSystem(identifierValue, system, use, data);
  }

  // TODO: Is PKV System always empty?
  // TODO: What is assignerRef of PKV? Example only display: "Signal Iduna"
  public Identifier getPatientPKV() {
    String value = this.getVersichertennummer_pkv();
    String system = Constants.getEmptyValue();
    Identifier.IdentifierUse use = Identifier.IdentifierUse.SECONDARY;
    VersichertenCode pkv = VersichertenCode.PKV;
    Reference assignerRef = Constants.getEmptyValue();
    return optionalIdentifierFromSystemWithCodeAndReference(value, system, use, pkv, assignerRef);
  }

  public Meta getResearchSubjectMeta() {
    return FhirGenerator.meta(
        MetaProfile.PERSON_RESEARCH_SUBJECT,
        MetaSource.PERSON_RESEARCH_SUBJECT,
        MetaVersionId.PERSON_RESEARCH_SUBJECT);
  }

  public Reference getResearchSubjectConsent() {
    // TODO: Get id from Erweiterungsmodul consent?
    String ref = MIIReference.MII_CONSENT;
    return FhirGenerator.reference(ref);
  }

  public Reference getResearchSubjectIndividual() {
    // TODO: Get id from Patient?
    String ref = MIIReference.MII_PATIENT;
    return FhirGenerator.reference(ref);
  }

  public Reference getResearchSubjectStudy() {
    // TODO: Where to get id for ResearchStudy
    String ref = MIIReference.MII_RESEARCH_STUDY;
    return FhirGenerator.reference(ref);
  }

  public Period getResearchSubjectPeriod() {
    String beginn = this.getTeilnahme_beginn();
    LoggingData beginnData = LoggingData.of(LOGGER, "getResearchSubjectPeriod", "teilnahme_beginn");
    String ende = this.getTeilnahme_ende();
    LoggingData endedata = LoggingData.of(LOGGER, "getResearchSubjectPeriod", "teilnahme_ende");
    return periodWithOptionalEnd(beginn, ende, beginnData, endedata);
  }

  // TODO: What is valueset of teilnahme_status?
  public ResearchSubject.ResearchSubjectStatus getResearchSubjectStatus() {
    String code = this.getTeilnahme_status();
    LoggingData data = LoggingData.of(LOGGER, "getResearchSubjectStatus", "teilnahme_status");
    return researchSubjectStatus(code, data);
  }

  public Identifier getResearchSubjectSubjectIdentificationCode() {
    String value = this.getSubjekt_identifizierungscode();
    String system = IdentifierSystem.SUBJECT_IDENTIFICATION_CODE;
    IdentifierTypeCode anon = IdentifierTypeCode.ANON;
    LoggingData data =
        LoggingData.of(
            LOGGER, "getResearchSubjectSubjectIdentificationCode", "subjekt_identifizierungscode");
    return identifierFromSystemAndCodeWithCode(value, system, anon, data);
  }

  public Meta getObservationMeta() {
    return FhirGenerator.meta(
        MetaProfile.PERSON_OBSERVATION,
        MetaSource.PERSON_OBSERVATION,
        MetaVersionId.PERSON_OBSERVATION);
  }

  public Reference getObservationSubject() {
    String ref = MIIReference.MII_PATIENT;
    return FhirGenerator.reference(ref);
  }

  public Coding getObservationValue() {
    VitalStatus code = VitalStatus.UNBEKANNT;
    String patientVerstorben = this.getPatient_verstorben();
    if (Helper.checkNonEmptyString(patientVerstorben)) {
      boolean verstorben = Helper.booleanFromString(patientVerstorben).orElse(false);
      if (verstorben) {
        code = VitalStatus.VERSTORBEN;
      }
    }
    return FhirGenerator.coding(code);
  }

  public DateTimeType getObservationEffective() {
    String zeitpunkt = this.getLetzter_lebendzeitpunkt();
    LoggingData data = LoggingData.of(LOGGER, "getObservationEffective", "letzter_lebendzeitpunkt");
    return dateTimeType(zeitpunkt, data);
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
