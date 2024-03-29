package basismodule;

import com.opencsv.bean.CsvBindByName;
import constants.*;
import helper.FhirGenerator;
import helper.Helper;
import helper.Logger;
import helper.LoggingData;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;
import valueSets.*;

import java.util.List;

import static helper.FhirParser.*;

/**
 * Implements version 1.0.0 released on 15.03.2021.
 *
 * @see
 *     "https://www.medizininformatik-initiative.de/Kerndatensatz/Modul_Fall/IGMIIKDSModulFall.html"
 */
public class Fall implements Datablock {
  private final Logger LOGGER;

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

  public Fall() {
    LOGGER = new Logger(Fall.class);
  }

  public Fall(Logger logger) {
    LOGGER = logger;
  }

  @Override
  public List<Resource> toFhirResources() {
    Encounter einrichtung = this.getEinrichtungsEncounter();
    Encounter abteilung = this.getAbteilungsEncounter();
    Encounter versorgungsstelle = this.getVersorgungsstellenEncounter();
    return Helper.listOf(einrichtung, abteilung, versorgungsstelle);
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
    // Identifier (optional)
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
    LoggingData startData =
        LoggingData.of(
            LOGGER,
            "getVersorgungsstellenEncounterPeriod",
            "versorgungsstellenkontakt_beginndatum");
    String enddatum = this.getVersorgungsstellenkontakt_enddatum();
    LoggingData endData =
        LoggingData.of(
            LOGGER, "getVersorgungsstellenEncounterPeriod", "versorgungsstellenkontakt_enddatum");
    return periodWithOptionalEnd(beginndatum, enddatum, startData, endData);
  }

  public CodeableConcept getVersorgungsstellenEncounterType() {
    String ebene = this.getVersorgungsstellenkontakt_ebene();
    LoggingData data =
        LoggingData.of(
            LOGGER, "getVersorgungsstellenEncounterType", "versorgungsstellenkontakt_ebene");
    return optionalCodeableConceptFromValueSet(ebene, Kontaktebene::fromCode, data);
  }

  public Coding getVersorgungsstellenEncounterClass() {
    String code = this.getVersorgungsstellenkontakt_klasse();
    LoggingData data =
        LoggingData.of(
            LOGGER, "getVersorgungsstellenEncounterClass", "versorgungsstellenkontakt_klasse");
    return codingFromValueSet(code, EncounterClassDE::fromCode, data);
  }

  public Encounter.EncounterStatus getVersorgungsstellenEncounterStatus() {
    return Encounter.EncounterStatus.FINISHED;
  }

  public Identifier getVersorgungsstellenEncounterIdentifier() {
    String aufnahmenummer = this.getVersorgungsstellenkontakt_aufnahmenummer();
    IdentifierTypeCode typeCode = IdentifierTypeCode.VN;
    String system = IdentifierSystem.EMPTY;
    return optionalIdentifierFromSystemWithCoding(aufnahmenummer, system, typeCode);
  }

  public Encounter.EncounterHospitalizationComponent getEinrichtungsEncounterHospitalization() {
    Encounter.EncounterHospitalizationComponent hospitalization =
        new Encounter.EncounterHospitalizationComponent();
    hospitalization.setAdmitSource(this.getEinrichtungsEncounterAdmitSource());
    hospitalization.setDischargeDisposition(this.getEinrichtungsEncounterDischargeDisposition());
    return hospitalization;
  }

  public CodeableConcept getEinrichtungsEncounterAdmitSource() {
    String code = this.getEinrichtungskontakt_aufnahmeanlass();
    LoggingData data =
        LoggingData.of(
            LOGGER, "getEinrichtungsEncounterAdmitSource", "einrichtungskontakt_aufnahmeanlass");
    return optionalCodeableConceptFromValueSet(code, Aufnahmeanlass::fromCode, data);
  }

  public CodeableConcept getEinrichtungsEncounterDischargeDisposition() {
    String code = this.getEinrichtungskontakt_entlassungsgrund();
    LoggingData data =
        LoggingData.of(
            LOGGER,
            "getEinrichtungsEncounterDischargeDisposition",
            "einrichtungskontakt_entlassungsgrund");
    return optionalCodeableConceptFromValueSet(code, Entlassungsgrund::fromCode, data);
  }

  public CodeableConcept getEinrichtungsEncounterReasonCode() {
    String aufnahmegrund = this.getEinrichtungskontakt_aufnahmegrund();
    LoggingData data =
        LoggingData.of(
            LOGGER, "getEinrichtungsEncounterReasonCode", "einrichtungskontakt_aufnahmegrund");
    return optionalCodeableConceptFromValueSet(aufnahmegrund, Aufnahmegrund::fromCode, data);
  }

  public Period getEinrichtungsEncounterPeriod() {
    String beginndatum = this.getEinrichtungskontakt_beginndatum();
    LoggingData startData =
        LoggingData.of(LOGGER, "getEinrichtungsEncounterPeriod", "einrichtungskontakt_beginndatum");
    String enddatum = this.getEinrichtungskontakt_enddatum();
    LoggingData endData =
        LoggingData.of(LOGGER, "getEinrichtungsEncounterPeriod", "einrichtungskontakt_enddatum");
    return periodWithOptionalEnd(beginndatum, enddatum, startData, endData);
  }

  public Reference getEinrichtungsEncounterSubject() {
    String ref = MIIReference.MII_PATIENT;
    return FhirGenerator.reference(ref);
  }

  public CodeableConcept getEinrichtungsEncounterServiceType() {
    Fachabteilung abteilung = Fachabteilung.INNERE_MEDIZIN;
    return FhirGenerator.codeableConcept(abteilung);
  }

  public CodeableConcept getEinrichtungsEncounterType() {
    String ebene = this.getEinrichtungskontakt_ebene();
    LoggingData data =
        LoggingData.of(LOGGER, "getEinrichtungsEncounterType", "einrichtungskontakt_ebene");
    return optionalCodeableConceptFromValueSet(ebene, Kontaktebene::fromCode, data);
  }

  public Coding getEinrichtungsEncounterClass() {
    String code = this.getEinrichtungskontakt_klasse();
    LoggingData data =
        LoggingData.of(LOGGER, "getEinrichtungsEncounterClass", "einrichtungskontakt_klasse");
    return codingFromValueSet(code, EncounterClassDE::fromCode, data);
  }

  public Encounter.EncounterStatus getEinrichtungsEncounterStatus() {
    return Encounter.EncounterStatus.FINISHED;
  }

  public Identifier getEinrichtungsEncounterIdentifier() {
    String aufnahmenummer = this.getEinrichtungskontakt_aufnahmenummer();
    String system = IdentifierSystem.EMPTY;
    IdentifierTypeCode typeCode = IdentifierTypeCode.VN;
    return optionalIdentifierFromSystemWithCoding(aufnahmenummer, system, typeCode);
  }

  public Meta getEinrichtungsEncounterMeta() {
    String profile = MetaProfile.FALL_ENCOUNTER;
    String source = MetaSource.FALL_EINRICHTUNG_ENCOUNTER;
    String versionId = MetaVersionId.FALL_EINRICHTUNG_ENCOUNTER;
    return FhirGenerator.meta(profile, source, versionId);
  }

  public Period getAbteilungsEncounterPeriod() {
    String beginndatum = this.getAbteilungskontakt_beginndatum();
    LoggingData startData =
        LoggingData.of(LOGGER, "getAbteilungsEncounterPeriod", "abteilungskontakt_beginndatum");
    String enddatum = this.getAbteilungskontakt_enddatum();
    LoggingData endData =
        LoggingData.of(LOGGER, "getAbteilungsEncounterPeriod", "abteilungskontakt_enddatum");
    return periodWithOptionalEnd(beginndatum, enddatum, startData, endData);
  }

  public CodeableConcept getAbteilungsEncounterType() {
    String ebene = this.getAbteilungskontakt_ebene();
    LoggingData data =
        LoggingData.of(LOGGER, "getAbteilungsEncounterType", "abteilungskontakt_ebene");
    return optionalCodeableConceptFromValueSet(ebene, Kontaktebene::fromCode, data);
  }

  public Reference getAbteilungsEncounterSubject() {
    return this.getEinrichtungsEncounterSubject();
  }

  public CodeableConcept getAbteilungsEncounterServiceType() {
    String abteilungsCode = this.getAbteilungskontakt_fachabteilungsschluessel();
    String system = CodingSystem.FALL_FACHABTEILUNGSSCHLUESSEL;
    LoggingData data = LoggingData.of(LOGGER, "getAbteilungsEncounterServiceType", "");
    return optionalCodeableConceptFromValueSetOrSystem(
        abteilungsCode, system, Fachabteilung::fromCode, data);
  }

  public Coding getAbteilungsEncounterClass() {
    String code = this.getAbteilungskontakt_klasse();
    LoggingData data =
        LoggingData.of(LOGGER, "getAbteilungsEncounterClass", "abteilungskontakt_klasse");
    return codingFromValueSet(code, EncounterClassDE::fromCode, data);
  }

  public Encounter.EncounterStatus getAbteilungsEncounterStatus() {
    return Encounter.EncounterStatus.FINISHED;
  }

  // TODO: Is ACME-Patient system always correct? It is not a fixed value
  public Identifier getAbteilungsEncounterIdentifier() {
    String aufnahmenummer = this.getAbteilungskontakt_aufnahmenummer();
    String system = IdentifierSystem.EMPTY;
    IdentifierTypeCode typeCode = IdentifierTypeCode.VN;
    return optionalIdentifierFromSystemWithCoding(aufnahmenummer, system, typeCode);
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
