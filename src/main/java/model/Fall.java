package model;

import com.opencsv.bean.CsvBindByName;
import constants.*;
import enums.*;
import helper.FhirGenerator;
import helper.Helper;
import helper.Logger;
import helper.ParsedCode;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Fall implements Datablock {
  private final Logger LOGGER = new Logger(Fall.class);
  private final String EINRICHTUNGS_ENCOUNTER = "EinrichtungsEncounter";
  private final String ABTEILUNGS_ENCOUNTER = "AbteilungsEncounter";
  private final String VERSORGUNGSSTELLEN_ENCOUNTER = "VersorgungsstellenEncounter";

  @CsvBindByName private String patNr;
  // Einrichtungskontakt
  @CsvBindByName private String einrichtungskontakt_ebene;
  @CsvBindByName private String einrichtungskontakt_klasse;
  @CsvBindByName private String einrichtungskontakt_patienten_identifikator;
  @CsvBindByName private String einrichtungskontakt_aufnahmenummer;
  @CsvBindByName private String einrichtungskontakt_aufnahmeanlass;
  @CsvBindByName private String einrichtungskontakt_aufnahmegrund;
  @CsvBindByName private String einrichtungskontakt_beginndatum;
  @CsvBindByName private String einrichtungskontakt_enddatum;
  @CsvBindByName private String einrichtungskontakt_entlassungsgrund;
  // Abteilungskontakt
  @CsvBindByName private String abteilungskontakt_ebene;
  @CsvBindByName private String abteilungskontakt_klasse;
  @CsvBindByName private String abteilungskontakt_patienten_identifikator;
  @CsvBindByName private String abteilungskontakt_aufnahmenummer;
  @CsvBindByName private String abteilungskontakt_fachabteilungsschluessel;
  @CsvBindByName private String abteilungskontakt_beginndatum;
  @CsvBindByName private String abteilungskontakt_enddatum;
  // Versorgungsstellenkontakt
  @CsvBindByName private String versorgungsstellenkontakt_ebene;
  @CsvBindByName private String versorgungsstellenkontakt_klasse;
  @CsvBindByName private String versorgungsstellenkontakt_patienten_identifikator;
  @CsvBindByName private String versorgungsstellenkontakt_aufnahmenummer;
  @CsvBindByName private String versorgungsstellenkontakt_beginndatum;
  @CsvBindByName private String versorgungsstellenkontakt_enddatum;
  // Organisationseinheit - Einrichtung
  @CsvBindByName private String einrichtungsidentifikator;
  // Organisationseinheit - Einrichtung - Abteilung
  @CsvBindByName private String abteilungsidentifikator;
  // Organisationseinheit - Einrichtung - Abteilung - Versorgungsstelle
  @CsvBindByName private String versorgungsstellenidentifikator;
  // Abrechnungsfall
  @CsvBindByName private String abrechnungsfallnummer;
  @CsvBindByName private String abrechnungsfall_startdatum;
  @CsvBindByName private String abrechnungsfall_enddatum;
  @CsvBindByName private String abrechnungsfall_zieleinrichtung;
  @CsvBindByName private String abrechnungsfall_aufnahmenummer;
  @CsvBindByName private String abrechnungsfall_fallzusammenfuehrung;

  @Override
  public List<Resource> toFhirResources() {
    Encounter einrichtung = this.getEinrichtungsEncounter();
    Encounter abteilung = this.getAbteilungsEncounter();
    Encounter versorgungsstelle = this.getVersorgungsstellenEncounter();
    return Helper.listOf(einrichtung, abteilung, versorgungsstelle, this.getOrganization());
  }

  public Encounter getEinrichtungsEncounter() {
    Encounter encounter = new Encounter();
    // Meta
    encounter.setMeta(this.getEinrichtungsEncounterMeta());
    // Identifier (optional)
    encounter.addIdentifier(this.getEinrichtungsEncounterIdentifier());
    // Status
    encounter.setStatus(this.getEinrichtungsEncounterStatus());
    // Class
    encounter.setClass_(this.getEinrichtungsEncounterClass());
    // Type (optional)
    encounter.addType(this.getEinrichtungsEncounterType());
    // ServiceType, always Innere Medizin
    encounter.setServiceType(this.getEinrichtungsEncounterServiceType());
    // Subject
    encounter.setSubject(this.getEinrichtungsEncounterSubject());
    // Period
    encounter.setPeriod(this.getEinrichtungsEncounterPeriod());
    // ReasonCode (optional)
    encounter.addReasonCode(this.getEinrichtungsEncounterReasonCode());
    // Hospitalization (optional)
    encounter.setHospitalization(this.getEinrichtungsEncounterHospitalization());
    return encounter;
  }

  public Encounter getAbteilungsEncounter() {
    Encounter encounter = new Encounter();
    // Meta
    encounter.setMeta(this.getAbteilungsEncounterMeta());
    // Identifier (optional)
    encounter.addIdentifier(this.getAbteilungsEncounterIdentifier());
    // Status
    encounter.setStatus(this.getAbteilungsEncounterStatus());
    // Class
    encounter.setClass_(this.getAbteilungsEncounterClass());
    // Type (optional)
    encounter.addType(this.getAbteilungsEncounterType());
    // ServiceType (optional)
    encounter.setServiceType(this.getAbteilungsEncounterServiceType());
    // Subject
    encounter.setSubject(this.getAbteilungsEncounterSubject());
    // Period
    encounter.setPeriod(this.getAbteilungsEncounterPeriod());
    return encounter;
  }

  public Encounter getVersorgungsstellenEncounter() {
    Encounter encounter = new Encounter();
    // Meta
    encounter.setMeta(this.getVersorgungsstellenEncounterMeta());
    // Identifier
    encounter.addIdentifier(this.getVersorgungsstellenEncounterIdentifier());
    // Status
    encounter.setStatus(this.getVersorgungsstellenEncounterStatus());
    // Class
    encounter.setClass_(this.getVersorgungsstellenEncounterClass());
    // Type (optional)
    encounter.addType(this.getVersorgungsstellenEncounterType());
    // Subject
    encounter.setSubject(this.getVersorgungsstellenEncounterSubject());
    // Period
    encounter.setPeriod(this.getVersorgungsstellenEncounterPeriod());
    return encounter;
  }

  public Reference getVersorgungsstellenEncounterSubject() {
    return this.getEinrichtungsEncounterSubject();
  }

  public Period getVersorgungsstellenEncounterPeriod() {
    String beginndatum = this.getVersorgungsstellenkontakt_beginndatum();
    Date start =
        Helper.getDateFromISO(beginndatum)
            .orElse(
                LOGGER.error(
                    "getVersorgungsstellenEncounterPeriod",
                    "versorgungsstellenkontakt_beginndatum",
                    beginndatum));
    String enddatum = this.getVersorgungsstellenkontakt_enddatum();
    if (Helper.checkNonEmptyString(enddatum)) {
      Date end =
          Helper.getDateFromISO(enddatum)
              .orElse(
                  LOGGER.error(
                      "getVersorgungsstellenEncounterPeriod",
                      "versorgungsstellenkontakt_enddatum",
                      enddatum));
      return FhirGenerator.period(start, end);
    } else {
      return FhirGenerator.period(start);
    }
  }

  public CodeableConcept getVersorgungsstellenEncounterType() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getVersorgungsstellenkontakt_ebene());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return null;
    }
    String system = CodingSystem.FALL_KONTAKTEBENE;
    String display = parsedCode.getDisplay();
    Coding kontaktebene = FhirGenerator.coding(code, system, display);
    return FhirGenerator.codeableConcept(kontaktebene);
  }

  public Coding getVersorgungsstellenEncounterClass() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getVersorgungsstellenkontakt_klasse());
    String code = parsedCode.getCode();
    String system = CodingSystem.ENCOUNTER_CLASS_DE;
    String display = parsedCode.getDisplay();
    return FhirGenerator.coding(code, system, display);
  }

  public Encounter.EncounterStatus getVersorgungsstellenEncounterStatus() {
    return Encounter.EncounterStatus.FINISHED;
  }

  public Identifier getVersorgungsstellenEncounterIdentifier() {
    String aufnahmenummer = this.getVersorgungsstellenkontakt_aufnahmenummer();
    if (Helper.checkEmptyString(aufnahmenummer)) {
      return null;
    }
    IdentifierTypeCode typeCode = IdentifierTypeCode.VN;
    Coding vnType = FhirGenerator.coding(typeCode);
    String system = IdentifierSystem.ACME_PATIENT;
    return FhirGenerator.identifier(aufnahmenummer, system, vnType);
  }

  public Encounter.EncounterHospitalizationComponent getEinrichtungsEncounterHospitalization() {
    Encounter.EncounterHospitalizationComponent hospitalization =
        new Encounter.EncounterHospitalizationComponent();
    hospitalization.setAdmitSource(this.getEinrichtungsEncounterAdmitSource());
    hospitalization.setDischargeDisposition(this.getEinrichtungsEncounterDischargeDisposition());
    return hospitalization;
  }

  public CodeableConcept getEinrichtungsEncounterAdmitSource() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getEinrichtungskontakt_aufnahmeanlass());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return null;
    }
    return Aufnahmeanlass.fromCode(code)
        .map(FhirGenerator::coding)
        .map(FhirGenerator::codeableConcept)
        .orElse(
            LOGGER.error(
                "getEinrichtungsEncounterAdmitSource", "einrichtungskontakt_aufnahmeanlass", code));
  }

  public CodeableConcept getEinrichtungsEncounterDischargeDisposition() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getEinrichtungskontakt_entlassungsgrund());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return null;
    }
    return Entlassungsgrund.fromCode(code)
        .map(FhirGenerator::coding)
        .map(FhirGenerator::codeableConcept)
        .orElse(
            LOGGER.error(
                "getEinrichtungsEncounterDischargeDisposition",
                "einrichtungskontakt_entlassungsgrund",
                code));
  }

  public CodeableConcept getEinrichtungsEncounterReasonCode() {
    String aufnahmegrund = this.getEinrichtungskontakt_aufnahmegrund();
    ParsedCode parsedCode = ParsedCode.fromString(aufnahmegrund);
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return null;
    }
    return Aufnahmegrund.fromCode(code)
        .map(FhirGenerator::coding)
        .map(FhirGenerator::codeableConcept)
        .orElse(
            LOGGER.error(
                "getEinrichtungsEncounterReasonCode",
                "einrichtungskontakt_aufnahmegrund",
                aufnahmegrund));
  }

  public Period getEinrichtungsEncounterPeriod() {
    String beginndatum = this.getEinrichtungskontakt_beginndatum();
    Date start =
        Helper.getDateFromISO(beginndatum)
            .orElse(
                LOGGER.error(
                    "getEinrichtungsEncounterPeriod",
                    "einrichtungskontakt_beginndatum",
                    beginndatum));
    String enddatum = this.getEinrichtungskontakt_enddatum();
    if (Helper.checkNonEmptyString(enddatum)) {
      Date end =
          Helper.getDateFromISO(enddatum)
              .orElse(
                  LOGGER.error(
                      "getEinrichtungsEncounterPeriod", "einrichtungskontakt_enddatum", enddatum));
      return FhirGenerator.period(start, end);
    } else {
      return FhirGenerator.period(start);
    }
  }

  public Reference getEinrichtungsEncounterSubject() {
    String patientNr = this.getPatNr();
    if (Helper.checkEmptyString(patientNr)) {
      throw new IllegalStateException("Fall: patNr needs to be set");
    }
    String ref = MIIReference.getPatient(patNr);
    return FhirGenerator.reference(ref);
  }

  public CodeableConcept getEinrichtungsEncounterServiceType() {
    Fachabteilung abteilung = Fachabteilung.INNERE_MEDIZIN;
    Coding fachabteilungsschluessel = FhirGenerator.coding(abteilung);
    return FhirGenerator.codeableConcept(fachabteilungsschluessel);
  }

  public CodeableConcept getEinrichtungsEncounterType() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getEinrichtungskontakt_ebene());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return null;
    }
    String system = CodingSystem.FALL_KONTAKTEBENE;
    String display = parsedCode.getDisplay();
    Coding kontaktebene = FhirGenerator.coding(code, system, display);
    return FhirGenerator.codeableConcept(kontaktebene);
  }

  public Coding getEinrichtungsEncounterClass() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getEinrichtungskontakt_klasse());
    String code = parsedCode.getCode();
    String system = CodingSystem.ENCOUNTER_CLASS_DE;
    String display = parsedCode.getDisplay();
    return FhirGenerator.coding(code, system, display);
  }

  public Encounter.EncounterStatus getEinrichtungsEncounterStatus() {
    return Encounter.EncounterStatus.FINISHED;
  }

  public Identifier getEinrichtungsEncounterIdentifier() {
    String aufnahmenummer = this.getEinrichtungskontakt_aufnahmenummer();
    if (Helper.checkEmptyString(aufnahmenummer)) {
      return null;
    }
    IdentifierTypeCode typeCode = IdentifierTypeCode.VN;
    Coding vnType = FhirGenerator.coding(typeCode);
    String value = aufnahmenummer;
    String system = IdentifierSystem.ACME_PATIENT;
    return FhirGenerator.identifier(value, system, vnType);
  }

  public Meta getEinrichtungsEncounterMeta() {
    String profile = MetaProfile.FALL_ENCOUNTER;
    String source = MetaSource.FALL_EINRICHTUNG_ENCOUNTER;
    String versionId = MetaVersionId.FALL_EINRICHTUNG_ENCOUNTER;
    return FhirGenerator.meta(profile, source, versionId);
  }

  public Period getAbteilungsEncounterPeriod() {
    String beginndatum = this.getAbteilungskontakt_beginndatum();
    Date start =
        Helper.getDateFromISO(beginndatum)
            .orElse(
                LOGGER.error(
                    "getAbteilungsEncounterPeriod", "abteilungskontakt_beginndatum", beginndatum));
    String enddatum = this.getAbteilungskontakt_enddatum();
    if (Helper.checkNonEmptyString(enddatum)) {
      Date end =
          Helper.getDateFromISO(enddatum)
              .orElse(
                  LOGGER.error(
                      "getAbteilungsEncounterPeriod", "abteilungskontakt_enddatum", enddatum));
      return FhirGenerator.period(start, end);
    } else {
      return FhirGenerator.period(start);
    }
  }

  public CodeableConcept getAbteilungsEncounterType() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getAbteilungskontakt_ebene());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return null;
    }
    String system = CodingSystem.FALL_KONTAKTEBENE;
    String display = parsedCode.getDisplay();
    Coding kontaktebene = FhirGenerator.coding(code, system, display);
    return FhirGenerator.codeableConcept(kontaktebene);
  }

  public Reference getAbteilungsEncounterSubject() {
    return this.getEinrichtungsEncounterSubject();
  }

  public CodeableConcept getAbteilungsEncounterServiceType() {
    ParsedCode parsedCode =
        ParsedCode.fromString(this.getAbteilungskontakt_fachabteilungsschluessel());
    String abteilungsCode = parsedCode.getCode();
    if (Helper.checkEmptyString(abteilungsCode)) {
      return null;
    }
    String system = CodingSystem.FALL_FACHABTEILUNGSSCHLUESSEL;
    String display = parsedCode.getDisplay();
    Optional<Fachabteilung> abteilungFromCode = Fachabteilung.fromCode(abteilungsCode);
    Coding fachabteilungsschluessel =
        abteilungFromCode
            .map(FhirGenerator::coding)
            .orElse(FhirGenerator.coding(abteilungsCode, system, display));
    return FhirGenerator.codeableConcept(fachabteilungsschluessel);
  }

  public Coding getAbteilungsEncounterClass() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getAbteilungskontakt_klasse());
    String code = parsedCode.getCode();
    String system = CodingSystem.ENCOUNTER_CLASS_DE;
    String display = parsedCode.getDisplay();
    return FhirGenerator.coding(code, system, display);
  }

  public Encounter.EncounterStatus getAbteilungsEncounterStatus() {
    return Encounter.EncounterStatus.FINISHED;
  }

  public Identifier getAbteilungsEncounterIdentifier() {
    String aufnahmenummer = this.getAbteilungskontakt_aufnahmenummer();
    if (Helper.checkEmptyString(aufnahmenummer)) {
      return null;
    }
    IdentifierTypeCode typeCode = IdentifierTypeCode.VN;
    Coding vnType = FhirGenerator.coding(typeCode);
    String value = aufnahmenummer;
    String system = IdentifierSystem.ACME_PATIENT;
    return FhirGenerator.identifier(value, system, vnType);
  }

  public Meta getAbteilungsEncounterMeta() {
    String profile = MetaProfile.FALL_ENCOUNTER;
    String source = MetaSource.FALL_ABTEILUNG_ENCOUNTER;
    String versionId = MetaVersionId.FALL_ABTEILUNG_ENCOUNTER;
    return FhirGenerator.meta(profile, source, versionId);
  }

  public Meta getVersorgungsstellenEncounterMeta() {
    String profile = MetaProfile.FALL_ENCOUNTER;
    String source = MetaSource.FALL_VERSORGUNGSSTELLE_ENCOUNTER;
    String versionId = MetaVersionId.FALL_VERSORGUNGSSTELLE_ENCOUNTER;
    return FhirGenerator.meta(profile, source, versionId);
  }

  // TODO: What is organization?
  public Organization getOrganization() {
    Organization organization = new Organization();
    return organization;
  }

  public String getPatNr() {
    return patNr;
  }

  public void setPatNr(String patNr) {
    this.patNr = patNr;
  }

  public String getEinrichtungskontakt_ebene() {
    return einrichtungskontakt_ebene;
  }

  public void setEinrichtungskontakt_ebene(String einrichtungskontakt_ebene) {
    this.einrichtungskontakt_ebene = einrichtungskontakt_ebene;
  }

  public String getEinrichtungskontakt_klasse() {
    return einrichtungskontakt_klasse;
  }

  public void setEinrichtungskontakt_klasse(String einrichtungskontakt_klasse) {
    this.einrichtungskontakt_klasse = einrichtungskontakt_klasse;
  }

  public String getEinrichtungskontakt_patienten_identifikator() {
    return einrichtungskontakt_patienten_identifikator;
  }

  public void setEinrichtungskontakt_patienten_identifikator(
      String einrichtungskontakt_patienten_identifikator) {
    this.einrichtungskontakt_patienten_identifikator = einrichtungskontakt_patienten_identifikator;
  }

  public String getEinrichtungskontakt_aufnahmenummer() {
    return einrichtungskontakt_aufnahmenummer;
  }

  public void setEinrichtungskontakt_aufnahmenummer(String einrichtungskontakt_aufnahmenummer) {
    this.einrichtungskontakt_aufnahmenummer = einrichtungskontakt_aufnahmenummer;
  }

  public String getEinrichtungskontakt_aufnahmeanlass() {
    return einrichtungskontakt_aufnahmeanlass;
  }

  public void setEinrichtungskontakt_aufnahmeanlass(String einrichtungskontakt_aufnahmeanlass) {
    this.einrichtungskontakt_aufnahmeanlass = einrichtungskontakt_aufnahmeanlass;
  }

  public String getEinrichtungskontakt_aufnahmegrund() {
    return einrichtungskontakt_aufnahmegrund;
  }

  public void setEinrichtungskontakt_aufnahmegrund(String einrichtungskontakt_aufnahmegrund) {
    this.einrichtungskontakt_aufnahmegrund = einrichtungskontakt_aufnahmegrund;
  }

  public String getEinrichtungskontakt_beginndatum() {
    return einrichtungskontakt_beginndatum;
  }

  public void setEinrichtungskontakt_beginndatum(String einrichtungskontakt_beginndatum) {
    this.einrichtungskontakt_beginndatum = einrichtungskontakt_beginndatum;
  }

  public String getEinrichtungskontakt_enddatum() {
    return einrichtungskontakt_enddatum;
  }

  public void setEinrichtungskontakt_enddatum(String einrichtungskontakt_enddatum) {
    this.einrichtungskontakt_enddatum = einrichtungskontakt_enddatum;
  }

  public String getEinrichtungskontakt_entlassungsgrund() {
    return einrichtungskontakt_entlassungsgrund;
  }

  public void setEinrichtungskontakt_entlassungsgrund(String einrichtungskontakt_entlassungsgrund) {
    this.einrichtungskontakt_entlassungsgrund = einrichtungskontakt_entlassungsgrund;
  }

  public String getAbteilungskontakt_ebene() {
    return abteilungskontakt_ebene;
  }

  public void setAbteilungskontakt_ebene(String abteilungskontakt_ebene) {
    this.abteilungskontakt_ebene = abteilungskontakt_ebene;
  }

  public String getAbteilungskontakt_klasse() {
    return abteilungskontakt_klasse;
  }

  public void setAbteilungskontakt_klasse(String abteilungskontakt_klasse) {
    this.abteilungskontakt_klasse = abteilungskontakt_klasse;
  }

  public String getAbteilungskontakt_patienten_identifikator() {
    return abteilungskontakt_patienten_identifikator;
  }

  public void setAbteilungskontakt_patienten_identifikator(
      String abteilungskontakt_patienten_identifikator) {
    this.abteilungskontakt_patienten_identifikator = abteilungskontakt_patienten_identifikator;
  }

  public String getAbteilungskontakt_aufnahmenummer() {
    return abteilungskontakt_aufnahmenummer;
  }

  public void setAbteilungskontakt_aufnahmenummer(String abteilungskontakt_aufnahmenummer) {
    this.abteilungskontakt_aufnahmenummer = abteilungskontakt_aufnahmenummer;
  }

  public String getAbteilungskontakt_fachabteilungsschluessel() {
    return abteilungskontakt_fachabteilungsschluessel;
  }

  public void setAbteilungskontakt_fachabteilungsschluessel(
      String abteilungskontakt_fachabteilungsschluessel) {
    this.abteilungskontakt_fachabteilungsschluessel = abteilungskontakt_fachabteilungsschluessel;
  }

  public String getAbteilungskontakt_beginndatum() {
    return abteilungskontakt_beginndatum;
  }

  public void setAbteilungskontakt_beginndatum(String abteilungskontakt_beginndatum) {
    this.abteilungskontakt_beginndatum = abteilungskontakt_beginndatum;
  }

  public String getAbteilungskontakt_enddatum() {
    return abteilungskontakt_enddatum;
  }

  public void setAbteilungskontakt_enddatum(String abteilungskontakt_enddatum) {
    this.abteilungskontakt_enddatum = abteilungskontakt_enddatum;
  }

  public String getVersorgungsstellenkontakt_ebene() {
    return versorgungsstellenkontakt_ebene;
  }

  public void setVersorgungsstellenkontakt_ebene(String versorgungsstellenkontakt_ebene) {
    this.versorgungsstellenkontakt_ebene = versorgungsstellenkontakt_ebene;
  }

  public String getVersorgungsstellenkontakt_klasse() {
    return versorgungsstellenkontakt_klasse;
  }

  public void setVersorgungsstellenkontakt_klasse(String versorgungsstellenkontakt_klasse) {
    this.versorgungsstellenkontakt_klasse = versorgungsstellenkontakt_klasse;
  }

  public String getVersorgungsstellenkontakt_patienten_identifikator() {
    return versorgungsstellenkontakt_patienten_identifikator;
  }

  public void setVersorgungsstellenkontakt_patienten_identifikator(
      String versorgungsstellenkontakt_patienten_identifikator) {
    this.versorgungsstellenkontakt_patienten_identifikator =
        versorgungsstellenkontakt_patienten_identifikator;
  }

  public String getVersorgungsstellenkontakt_aufnahmenummer() {
    return versorgungsstellenkontakt_aufnahmenummer;
  }

  public void setVersorgungsstellenkontakt_aufnahmenummer(
      String versorgungsstellenkontakt_aufnahmenummer) {
    this.versorgungsstellenkontakt_aufnahmenummer = versorgungsstellenkontakt_aufnahmenummer;
  }

  public String getVersorgungsstellenkontakt_beginndatum() {
    return versorgungsstellenkontakt_beginndatum;
  }

  public void setVersorgungsstellenkontakt_beginndatum(
      String versorgungsstellenkontakt_beginndatum) {
    this.versorgungsstellenkontakt_beginndatum = versorgungsstellenkontakt_beginndatum;
  }

  public String getVersorgungsstellenkontakt_enddatum() {
    return versorgungsstellenkontakt_enddatum;
  }

  public void setVersorgungsstellenkontakt_enddatum(String versorgungsstellenkontakt_enddatum) {
    this.versorgungsstellenkontakt_enddatum = versorgungsstellenkontakt_enddatum;
  }

  public String getEinrichtungsidentifikator() {
    return einrichtungsidentifikator;
  }

  public void setEinrichtungsidentifikator(String einrichtungsidentifikator) {
    this.einrichtungsidentifikator = einrichtungsidentifikator;
  }

  public String getAbteilungsidentifikator() {
    return abteilungsidentifikator;
  }

  public void setAbteilungsidentifikator(String abteilungsidentifikator) {
    this.abteilungsidentifikator = abteilungsidentifikator;
  }

  public String getVersorgungsstellenidentifikator() {
    return versorgungsstellenidentifikator;
  }

  public void setVersorgungsstellenidentifikator(String versorgungsstellenidentifikator) {
    this.versorgungsstellenidentifikator = versorgungsstellenidentifikator;
  }

  public String getAbrechnungsfallnummer() {
    return abrechnungsfallnummer;
  }

  public void setAbrechnungsfallnummer(String abrechnungsfallnummer) {
    this.abrechnungsfallnummer = abrechnungsfallnummer;
  }

  public String getAbrechnungsfall_startdatum() {
    return abrechnungsfall_startdatum;
  }

  public void setAbrechnungsfall_startdatum(String abrechnungsfall_startdatum) {
    this.abrechnungsfall_startdatum = abrechnungsfall_startdatum;
  }

  public String getAbrechnungsfall_enddatum() {
    return abrechnungsfall_enddatum;
  }

  public void setAbrechnungsfall_enddatum(String abrechnungsfall_enddatum) {
    this.abrechnungsfall_enddatum = abrechnungsfall_enddatum;
  }

  public String getAbrechnungsfall_zieleinrichtung() {
    return abrechnungsfall_zieleinrichtung;
  }

  public void setAbrechnungsfall_zieleinrichtung(String abrechnungsfall_zieleinrichtung) {
    this.abrechnungsfall_zieleinrichtung = abrechnungsfall_zieleinrichtung;
  }

  public String getAbrechnungsfall_aufnahmenummer() {
    return abrechnungsfall_aufnahmenummer;
  }

  public void setAbrechnungsfall_aufnahmenummer(String abrechnungsfall_aufnahmenummer) {
    this.abrechnungsfall_aufnahmenummer = abrechnungsfall_aufnahmenummer;
  }

  public String getAbrechnungsfall_fallzusammenfuehrung() {
    return abrechnungsfall_fallzusammenfuehrung;
  }

  public void setAbrechnungsfall_fallzusammenfuehrung(String abrechnungsfall_fallzusammenfuehrung) {
    this.abrechnungsfall_fallzusammenfuehrung = abrechnungsfall_fallzusammenfuehrung;
  }
}
