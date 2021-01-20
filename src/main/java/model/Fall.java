package model;

import constants.*;
import enums.*;
import helper.FhirHelper;
import helper.Helper;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Fall implements Datablock {
  private String patNr;
  // Einrichtungskontakt
  private String einrichtungskontakt_ebene;
  private String einrichtungskontakt_klasse;
  private String einrichtungskontakt_patienten_identifikator;
  private String einrichtungskontakt_aufnahmenummer;
  private String einrichtungskontakt_aufnahmeanlass;
  private String einrichtungskontakt_aufnahmegrund;
  private String einrichtungskontakt_beginndatum;
  private String einrichtungskontakt_enddatum;
  private String einrichtungskontakt_entlassungsgrund;
  // Abteilungskontakt
  private String abteilungskontakt_ebene;
  private String abteilungskontakt_klasse;
  private String abteilungskontakt_patienten_identifikator;
  private String abteilungskontakt_aufnahmenummer;
  private String abteilungskontakt_fachabteilungsschluessel;
  private String abteilungskontakt_beginndatum;
  private String abteilungskontakt_enddatum;
  // Versorgungsstellenkontakt
  private String versorgungsstellenkontakt_ebene;
  private String versorgungsstellenkontakt_klasse;
  private String versorgungsstellenkontakt_patienten_identifikator;
  private String versorgungsstellenkontakt_aufnahmenummer;
  private String versorgungsstellenkontakt_beginndatum;
  private String versorgungsstellenkontakt_enddatum;
  // Organisationseinheit - Einrichtung
  private String einrichtungsidentifikator;
  // Organisationseinheit - Einrichtung - Abteilung
  private String abteilungsidentifikator;
  // Organisationseinheit - Einrichtung - Abteilung - Versorgungsstelle
  private String versorgungsstellenidentifikator;
  // Abrechnungsfall
  private String abrechnungsfallnummer;
  private String abrechnungsfall_startdatum;
  private String abrechnungsfall_enddatum;
  private String abrechnungsfall_zieleinrichtung;
  private String abrechnungsfall_aufnahmenummer;
  private String abrechnungsfall_fallzusammenfuehrung;

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
    if (Helper.checkNonEmptyString(this.getEinrichtungskontakt_aufnahmenummer()))
      encounter.addIdentifier(this.getEinrichtungsEncounterIdentifier());
    // Status
    encounter.setStatus(this.getEinrichtungsEncounterStatus());
    // Class
    encounter.setClass_(this.getEinrichtungsEncounterClass());
    // Type (optional)
    if (Helper.checkNonEmptyString(this.getEinrichtungskontakt_ebene()))
      encounter.addType(this.getEinrichtungsEncounterType());
    // ServiceType, always Innere Medizin
    encounter.setServiceType(this.getEinrichtungsEncounterServiceType());
    // Subject
    encounter.setSubject(this.getEinrichtungsEncounterSubject());
    // Period
    encounter.setPeriod(this.getEinrichtungsEncounterPeriod());
    // ReasonCode (optional)
    if (Helper.checkNonEmptyString(this.getEinrichtungskontakt_aufnahmegrund()))
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
    if (Helper.checkNonEmptyString(this.getAbteilungskontakt_aufnahmenummer()))
      encounter.addIdentifier(this.getAbteilungsEncounterIdentifier());
    // Status
    encounter.setStatus(this.getAbteilungsEncounterStatus());
    // Class
    encounter.setClass_(this.getAbteilungsEncounterClass());
    // Type (optional)
    if (Helper.checkNonEmptyString(this.getAbteilungskontakt_ebene()))
      encounter.addType(this.getAbteilungsEncounterType());
    // ServiceType (optional)
    if (Helper.checkNonEmptyString(this.getAbteilungskontakt_fachabteilungsschluessel()))
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
    if (Helper.checkNonEmptyString(this.getVersorgungsstellenkontakt_aufnahmenummer()))
      encounter.addIdentifier(this.getVersorgungsstellenEncounterIdentifier());
    // Status
    encounter.setStatus(this.getVersorgungsstellenEncounterStatus());
    // Class
    encounter.setClass_(this.getVersorgungsstellenEncounterClass());
    // Type (optional)
    if (Helper.checkNonEmptyString(this.getVersorgungsstellenkontakt_ebene()))
      encounter.addType(this.getVersorgungsstellenEncounterType());
    // Subject
    encounter.setSubject(this.getVersorgungsstellenEncounterSubject());
    // Period
    encounter.setPeriod(this.getVersorgungsstellenEncounterPeriod());
    return encounter;
  }

  public Reference getVersorgungsstellenEncounterSubject() {
    String type = ReferenceType.PATIENT;
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    String identifierValue = this.getVersorgungsstellenkontakt_patienten_identifikator();
    Identifier subjectId =
        FhirHelper.generateIdentifier(identifierValue, IdentifierSystem.LOCAL_PID, assignerRef);
    return FhirHelper.generateReference(type, subjectId);
  }

  public Period getVersorgungsstellenEncounterPeriod() {
    Date start = Helper.getDateFromISO(this.getVersorgungsstellenkontakt_beginndatum());
    if (Helper.checkNonEmptyString(this.getVersorgungsstellenkontakt_enddatum())) {
      Date end = Helper.getDateFromISO(this.getVersorgungsstellenkontakt_enddatum());
      return FhirHelper.generatePeriod(start, end);
    } else {
      return FhirHelper.generatePeriod(start);
    }
  }

  public CodeableConcept getVersorgungsstellenEncounterType() {
    String code = this.getVersorgungsstellenkontakt_ebene();
    String system = CodingSystem.FALL_KONTAKTEBENE;
    Coding kontaktebene = FhirHelper.generateCoding(code, system);
    return new CodeableConcept().addCoding(kontaktebene);
  }

  public Coding getVersorgungsstellenEncounterClass() {
    String code = this.getVersorgungsstellenkontakt_klasse();
    String system = CodingSystem.ENCOUNTER_CLASS_DE;
    return FhirHelper.generateCoding(code, system);
  }

  public Encounter.EncounterStatus getVersorgungsstellenEncounterStatus() {
    return Encounter.EncounterStatus.FINISHED;
  }

  public Identifier getVersorgungsstellenEncounterIdentifier() {
    IdentifierTypeCode typeCode = IdentifierTypeCode.VN;
    Coding vnType =
        FhirHelper.generateCoding(typeCode.getCode(), typeCode.getSystem(), typeCode.getDisplay());
    String value = this.getVersorgungsstellenkontakt_aufnahmenummer();
    String system = IdentifierSystem.ACME_PATIENT;
    return FhirHelper.generateIdentifier(value, system, vnType);
  }

  public Encounter.EncounterHospitalizationComponent getEinrichtungsEncounterHospitalization() {
    Encounter.EncounterHospitalizationComponent hospitalization =
        new Encounter.EncounterHospitalizationComponent();
    if (Helper.checkNonEmptyString(this.getEinrichtungskontakt_aufnahmeanlass()))
      hospitalization.setAdmitSource(this.getEinrichtungsEncounterAdmitSource());
    if (Helper.checkNonEmptyString(this.getEinrichtungskontakt_entlassungsgrund()))
      hospitalization.setDischargeDisposition(this.getEinrichtungsEncounterDischargeDisposition());
    return hospitalization;
  }

  public CodeableConcept getEinrichtungsEncounterAdmitSource() {
    String code = this.getEinrichtungskontakt_aufnahmeanlass();
    Aufnahmeanlass anlass = Aufnahmeanlass.fromCode(code);
    Coding aufnahmegrund =
        FhirHelper.generateCoding(anlass.getCode(), anlass.getSystem(), anlass.getDisplay());
    return new CodeableConcept().addCoding(aufnahmegrund);
  }

  public CodeableConcept getEinrichtungsEncounterDischargeDisposition() {
    String code = this.getEinrichtungskontakt_entlassungsgrund();
    Entlassungsgrund grund = Entlassungsgrund.fromCode(code);
    Coding entlassungsgrund =
        FhirHelper.generateCoding(grund.getCode(), grund.getSystem(), grund.getDisplay());
    return new CodeableConcept().addCoding(entlassungsgrund);
  }

  public CodeableConcept getEinrichtungsEncounterReasonCode() {
    String code = this.getEinrichtungskontakt_aufnahmegrund();
    Aufnahmegrund grund = Aufnahmegrund.fromCode(code);
    Coding aufnahmegrund =
        FhirHelper.generateCoding(grund.getCode(), grund.getSystem(), grund.getDisplay());
    return new CodeableConcept().addCoding(aufnahmegrund);
  }

  public Period getEinrichtungsEncounterPeriod() {
    Date start = Helper.getDateFromISO(this.getEinrichtungskontakt_beginndatum());
    if (Helper.checkNonEmptyString(this.getEinrichtungskontakt_enddatum())) {
      Date end = Helper.getDateFromISO(this.getEinrichtungskontakt_enddatum());
      return FhirHelper.generatePeriod(start, end);
    } else {
      return FhirHelper.generatePeriod(start);
    }
  }

  public Reference getEinrichtungsEncounterSubject() {
    String type = ReferenceType.PATIENT;
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    String identifierValue = this.getEinrichtungskontakt_patienten_identifikator();
    Identifier subjectId =
        FhirHelper.generateIdentifier(identifierValue, IdentifierSystem.LOCAL_PID, assignerRef);
    return FhirHelper.generateReference(type, subjectId);
  }

  public CodeableConcept getEinrichtungsEncounterServiceType() {
    Fachabteilung abteilung = Fachabteilung.INNERE_MEDIZIN;
    Coding fachabteilungsschluessel =
        FhirHelper.generateCoding(
            abteilung.getCode(), abteilung.getSystem(), abteilung.getDisplay());
    return new CodeableConcept().addCoding(fachabteilungsschluessel);
  }

  public CodeableConcept getEinrichtungsEncounterType() {
    String code = this.getEinrichtungskontakt_ebene();
    String system = CodingSystem.FALL_KONTAKTEBENE;
    Coding kontaktebene = FhirHelper.generateCoding(code, system);
    return new CodeableConcept().addCoding(kontaktebene);
  }

  public Coding getEinrichtungsEncounterClass() {
    String code = this.getEinrichtungskontakt_klasse();
    String system = CodingSystem.ENCOUNTER_CLASS_DE;
    return FhirHelper.generateCoding(code, system);
  }

  public Encounter.EncounterStatus getEinrichtungsEncounterStatus() {
    return Encounter.EncounterStatus.FINISHED;
  }

  public Identifier getEinrichtungsEncounterIdentifier() {
    IdentifierTypeCode typeCode = IdentifierTypeCode.VN;
    Coding vnType =
        FhirHelper.generateCoding(typeCode.getCode(), typeCode.getSystem(), typeCode.getDisplay());
    String value = this.getEinrichtungskontakt_aufnahmenummer();
    String system = IdentifierSystem.ACME_PATIENT;
    return FhirHelper.generateIdentifier(value, system, vnType);
  }

  public Meta getEinrichtungsEncounterMeta() {
    String profile = MetaProfile.FALL_ENCOUNTER;
    String source = MetaSource.FALL_EINRICHTUNG_ENCOUNTER;
    String versionId = MetaVersionId.FALL_EINRICHTUNG_ENCOUNTER;
    return FhirHelper.generateMeta(profile, source, versionId);
  }

  public Period getAbteilungsEncounterPeriod() {
    Date start = Helper.getDateFromISO(this.getAbteilungskontakt_beginndatum());
    if (Helper.checkNonEmptyString(this.getAbteilungskontakt_enddatum())) {
      Date end = Helper.getDateFromISO(this.getAbteilungskontakt_enddatum());
      return FhirHelper.generatePeriod(start, end);
    } else {
      return FhirHelper.generatePeriod(start);
    }
  }

  public CodeableConcept getAbteilungsEncounterType() {
    String code = this.getAbteilungskontakt_ebene();
    String system = CodingSystem.FALL_KONTAKTEBENE;
    Coding kontaktebene = FhirHelper.generateCoding(code, system);
    return new CodeableConcept().addCoding(kontaktebene);
  }

  public Reference getAbteilungsEncounterSubject() {
    String type = ReferenceType.PATIENT;
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    String identifierValue = this.getAbteilungskontakt_patienten_identifikator();
    Identifier subjectId =
        FhirHelper.generateIdentifier(identifierValue, IdentifierSystem.LOCAL_PID, assignerRef);
    return FhirHelper.generateReference(type, subjectId);
  }

  public CodeableConcept getAbteilungsEncounterServiceType() {
    String schluessel = this.getAbteilungskontakt_fachabteilungsschluessel();
    Optional<Fachabteilung> abteilungFromCode = Fachabteilung.fromCode(schluessel);
    Coding fachabteilungsschluessel = null;
    if (abteilungFromCode.isPresent()) {
      Fachabteilung abteilung = abteilungFromCode.get();
      fachabteilungsschluessel =
          FhirHelper.generateCoding(
              abteilung.getCode(), abteilung.getSystem(), abteilung.getDisplay());
    } else {
      String code = this.getAbteilungskontakt_fachabteilungsschluessel();
      String system = CodingSystem.FALL_FACHABTEILUNGSSCHLUESSEL;
      fachabteilungsschluessel = FhirHelper.generateCoding(code, system);
    }
    return new CodeableConcept().addCoding(fachabteilungsschluessel);
  }

  public Coding getAbteilungsEncounterClass() {
    String code = this.getAbteilungskontakt_klasse();
    String system = CodingSystem.ENCOUNTER_CLASS_DE;
    return FhirHelper.generateCoding(code, system);
  }

  public Encounter.EncounterStatus getAbteilungsEncounterStatus() {
    return Encounter.EncounterStatus.FINISHED;
  }

  public Identifier getAbteilungsEncounterIdentifier() {
    IdentifierTypeCode typeCode = IdentifierTypeCode.VN;
    Coding vnType =
        FhirHelper.generateCoding(typeCode.getCode(), typeCode.getSystem(), typeCode.getDisplay());
    String value = this.getAbteilungskontakt_aufnahmenummer();
    String system = IdentifierSystem.ACME_PATIENT;
    return FhirHelper.generateIdentifier(value, system, vnType);
  }

  public Meta getAbteilungsEncounterMeta() {
    String profile = MetaProfile.FALL_ENCOUNTER;
    String source = MetaSource.FALL_ABTEILUNG_ENCOUNTER;
    String versionId = MetaVersionId.FALL_ABTEILUNG_ENCOUNTER;
    return FhirHelper.generateMeta(profile, source, versionId);
  }

  public Meta getVersorgungsstellenEncounterMeta() {
    String profile = MetaProfile.FALL_ENCOUNTER;
    String source = MetaSource.FALL_VERSORGUNGSSTELLE_ENCOUNTER;
    String versionId = MetaVersionId.FALL_VERSORGUNGSSTELLE_ENCOUNTER;
    return FhirHelper.generateMeta(profile, source, versionId);
  }

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
