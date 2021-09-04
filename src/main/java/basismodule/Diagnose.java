package basismodule;

import com.opencsv.bean.CsvBindByName;
import constants.Constants;
import constants.*;
import helper.FhirGenerator;
import helper.Helper;
import helper.Logger;
import helper.LoggingData;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;
import valueSets.ClinicalStatus;
import valueSets.ICD_Diagnosesicherheit;
import valueSets.ICD_Seitenlokalisation;
import valueSets.KBVBaseStageLife;

import java.util.Date;
import java.util.List;

import static helper.FhirParser.*;

/**
 * Implements version 1.0.4 released on 27.05.2021.
 *
 * @see
 *     "https://www.medizininformatik-initiative.de/Kerndatensatz/Modul_Diagnose/IGMIIKDSModulDiagnose.html"
 */
public class Diagnose implements Datablock {
  private final Logger LOGGER = new Logger(Diagnose.class);
  @CsvBindByName private String patNr;
  // ICD-10-GM Diagnose kodiert
  @CsvBindByName private String icd_diagnosecode;
  @CsvBindByName private String icd_primaercode;
  @CsvBindByName private String icd_manifestationscode;
  @CsvBindByName private String icd_ausrufezeichencode;
  @CsvBindByName private String icd_diagnosesicherheit;
  @CsvBindByName private String icd_seitenlokalisation;
  // ALPHA-ID kodiert
  @CsvBindByName private String alpha_diagnosecode;
  // ORPHANET Diagnose kodiert
  @CsvBindByName private String orphanet_diagnosecode;
  // SNOMED Diagnose kodiert
  @CsvBindByName private String snomed_diagnosecode;
  // Weitere Kodesysteme
  @CsvBindByName private String weitere_diagnosecode;
  @CsvBindByName private String koerperstelle;
  @CsvBindByName private String freitextbeschreibung;
  @CsvBindByName private String diagnoseerlaeuterung;
  @CsvBindByName private String dokumentationsdatum;
  @CsvBindByName private String klinischer_status;
  // Klinisch relevanter Zeitraum - Zeitraum
  @CsvBindByName private String zeitraum_von;
  @CsvBindByName private String zeitraum_bis;
  // klinisch relevanter Zeitraum - Lebensphase
  @CsvBindByName private String lebensphase_von;
  @CsvBindByName private String lebensphase_bis;
  @CsvBindByName private String feststellungsdatum;

  @Override
  public List<Resource> toFhirResources() {
    return Helper.listOf(this.getCondition());
  }

  public Condition getCondition() {
    Condition condition = new Condition();
    // Meta
    condition.setMeta(this.getMeta());
    // Clinical status (optional)
    condition.setClinicalStatus(this.getClinicalStatus());
    // Code
    condition.setCode(this.getCode());
    // BodySite, only includes Koerperstelle
    condition.addBodySite(this.getBodySite());
    // Subject
    condition.setSubject(this.getSubject());
    // Onset (optional)
    condition.setOnset(this.getOnset());
    // RecordedDate
    condition.setRecordedDate(this.getRecordedDate());
    // Note (optional), only includes Diagnoseerläuterung
    condition.addNote(this.getNote());
    return condition;
  }

  public Reference getSubject() {
    String ref = MIIReference.MII_PATIENT;
    return FhirGenerator.reference(ref);
  }

  public CodeableConcept getCode() {
    Coding icd = this.getCodeIcd();
    Coding alpha = this.getCodeAlpha();
    Coding sct = this.getCodeSct();
    Coding orphanet = this.getCodeOrphanet();
    Coding weitere = this.getCodeWeitere();
    if (Helper.checkAllNull(icd, alpha, sct, orphanet, weitere)) {
      return LOGGER.error("getCode", "There has to be at least 1 code");
    }
    return FhirGenerator.codeableConcept(icd, alpha, sct, orphanet, weitere)
        .setText(this.getText());
  }

  public String getText() {
    return this.getFreitextbeschreibung();
  }

  // TODO: Mehrfachcodierungskennzeichen replaces Ausruf, Manifest and Primaer?
  public Coding getCodeIcd() {
    String code = this.getIcd_diagnosecode();
    String system = CodingSystem.ICD_10_GM;
    List<Extension> extensions =
        Helper.listOf(
            this.getCodeIcdDiagnosesicherheit(),
            this.getCodeIcdSeitenlokalisation(),
            this.getCodeIcdAusrufezeichen(),
            this.getCodeIcdManifestationscode(),
            this.getCodeIcdPrimaercode());
    return optionalCodingFromSystemWithExtensions(code, system, extensions);
  }

  public Extension getCodeIcdPrimaercode() {
    String icd = this.getIcd_primaercode();
    String system = CodingSystem.ICD_10_GM;
    String version = Constants.VERSION_2020;
    String url = ExtensionUrl.ICD_10_GM_PRIMAERCODE;
    return optionalExtensionWithCoding(icd, system, version, url);
  }

  public Extension getCodeIcdManifestationscode() {
    String code = this.getIcd_manifestationscode();
    String system = CodingSystem.ICD_10_GM;
    String version = Constants.VERSION_2020;
    String url = ExtensionUrl.ICD_10_GM_MANIFESTATIONSCODE;
    return optionalExtensionWithCoding(code, system, version, url);
  }

  public Extension getCodeIcdAusrufezeichen() {
    String code = this.getIcd_ausrufezeichencode();
    String system = CodingSystem.ICD_10_GM;
    String version = Constants.VERSION_2020;
    String url = ExtensionUrl.ICD_10_GM_AUSRUFEZEICHEN;
    return optionalExtensionWithCoding(code, system, version, url);
  }

  public Extension getCodeIcdSeitenlokalisation() {
    String code = this.getIcd_seitenlokalisation();
    String url = ExtensionUrl.ICD_10_GM_SEITENLOKALISATION;
    LoggingData data =
        LoggingData.of(LOGGER, "getCodeIcdSeitenlokalisation", "icd_seitenlokalisation");
    return optionalExtensionWithCodingFromValueSet(
        code, url, ICD_Seitenlokalisation::fromCode, data);
  }

  public Extension getCodeIcdDiagnosesicherheit() {
    String code = this.getIcd_diagnosesicherheit();
    String url = ExtensionUrl.ICD_10_GM_DIAGNOSESEICHERHEIT;
    LoggingData data =
        LoggingData.of(LOGGER, "getCodeIcdDiagnosesicherheit", "icd_diagnosesicherheit");
    return optionalExtensionWithCodingFromValueSet(
        code, url, ICD_Diagnosesicherheit::fromSnomedCode, data);
  }

  public Coding getCodeAlpha() {
    String alpha = this.getAlpha_diagnosecode();
    String system = CodingSystem.ALPHA_ID;
    return optionalCodingFromSystem(alpha, system);
  }

  public Coding getCodeSct() {
    String snomed = this.getSnomed_diagnosecode();
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    return optionalCodingFromSystem(snomed, system);
  }

  public Coding getCodeOrphanet() {
    String orphanet = this.getOrphanet_diagnosecode();
    String system = CodingSystem.ORPHANET;
    return optionalCodingFromSystem(orphanet, system);
  }

  public Coding getCodeWeitere() {
    String code = this.getWeitere_diagnosecode();
    return optionalCoding(code);
  }

  public Annotation getNote() {
    String erlaeuterung = this.getDiagnoseerlaeuterung();
    return optionalAnnotation(erlaeuterung);
  }

  public Date getRecordedDate() {
    String datum = this.getDokumentationsdatum();
    LoggingData data = LoggingData.of(LOGGER, "getRecordedDate", "dokumentationsdatum");
    return date(datum, data);
  }

  public Type getOnset() {
    String zeitraumVon = this.getZeitraum_von();
    LoggingData startData = LoggingData.of(LOGGER, "getOnset", "zeitraum_von");
    DateTimeType start =
        nonEmptyDateTimeTypeWithExtension(zeitraumVon, startData, getLebensphaseVon());
    String zeitraumBis = this.getZeitraum_bis();
    LoggingData endData = LoggingData.of(LOGGER, "getOnset", "zeitraum_bis");
    DateTimeType end = nonEmptyDateTimeTypeWithExtension(zeitraumBis, endData, getLebensphaseBis());
    return optionalDateTimeTypeOrPeriod(start, end);
  }

  public Extension getLebensphaseVon() {
    String code = this.getLebensphase_von();
    String url = ExtensionUrl.STAGE_LIFE;
    LoggingData data = LoggingData.of(LOGGER, "getLebensphaseVon", "lebensphase_von");
    return optionalExtensionWithCodeFromValueSet(code, url, KBVBaseStageLife::fromCode, data);
  }

  public Extension getLebensphaseBis() {
    String code = this.getLebensphase_bis();
    String url = ExtensionUrl.STAGE_LIFE;
    LoggingData data = LoggingData.of(LOGGER, "getLebensphaseBis", "lebensphase_bis");
    return optionalExtensionWithCodeFromValueSet(code, url, KBVBaseStageLife::fromCode, data);
  }

  public CodeableConcept getBodySite() {
    String code = this.getKoerperstelle();
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    return optionalCodeableConceptFromSystem(code, system);
  }

  public CodeableConcept getClinicalStatus() {
    String status = this.getKlinischer_status();
    LoggingData data = LoggingData.of(LOGGER, "getClinicalStatus", "klinischer_status");
    return optionalCodeableConceptFromValueSet(status, ClinicalStatus::fromCode, data);
  }

  public Meta getMeta() {
    String profile = MetaProfile.DIAGNOSE_CONDITION;
    String source = MetaSource.DIAGNOSE_CONDITION;
    String versionId = MetaVersionId.DIAGNOSE_CONDITION;
    return FhirGenerator.meta(profile, source, versionId);
  }

  public String getPatNr() {
    return patNr;
  }

  public void setPatNr(String patNr) {
    this.patNr = patNr;
  }

  public String getIcd_diagnosecode() {
    return icd_diagnosecode;
  }

  public void setIcd_diagnosecode(String icd_diagnosecode) {
    this.icd_diagnosecode = icd_diagnosecode;
  }

  public String getIcd_primaercode() {
    return icd_primaercode;
  }

  public void setIcd_primaercode(String icd_primaercode) {
    this.icd_primaercode = icd_primaercode;
  }

  public String getIcd_manifestationscode() {
    return icd_manifestationscode;
  }

  public void setIcd_manifestationscode(String icd_manifestationscode) {
    this.icd_manifestationscode = icd_manifestationscode;
  }

  public String getIcd_ausrufezeichencode() {
    return icd_ausrufezeichencode;
  }

  public void setIcd_ausrufezeichencode(String icd_ausrufezeichencode) {
    this.icd_ausrufezeichencode = icd_ausrufezeichencode;
  }

  public String getIcd_diagnosesicherheit() {
    return icd_diagnosesicherheit;
  }

  public void setIcd_diagnosesicherheit(String icd_diagnosesicherheit) {
    this.icd_diagnosesicherheit = icd_diagnosesicherheit;
  }

  public String getIcd_seitenlokalisation() {
    return icd_seitenlokalisation;
  }

  public void setIcd_seitenlokalisation(String icd_seitenlokalisation) {
    this.icd_seitenlokalisation = icd_seitenlokalisation;
  }

  public String getAlpha_diagnosecode() {
    return alpha_diagnosecode;
  }

  public void setAlpha_diagnosecode(String alpha_diagnosecode) {
    this.alpha_diagnosecode = alpha_diagnosecode;
  }

  public String getOrphanet_diagnosecode() {
    return orphanet_diagnosecode;
  }

  public void setOrphanet_diagnosecode(String orphanet_diagnosecode) {
    this.orphanet_diagnosecode = orphanet_diagnosecode;
  }

  public String getSnomed_diagnosecode() {
    return snomed_diagnosecode;
  }

  public void setSnomed_diagnosecode(String snomed_diagnosecode) {
    this.snomed_diagnosecode = snomed_diagnosecode;
  }

  public String getWeitere_diagnosecode() {
    return weitere_diagnosecode;
  }

  public void setWeitere_diagnosecode(String weitere_diagnosecode) {
    this.weitere_diagnosecode = weitere_diagnosecode;
  }

  public String getKoerperstelle() {
    return koerperstelle;
  }

  public void setKoerperstelle(String koerperstelle) {
    this.koerperstelle = koerperstelle;
  }

  public String getFreitextbeschreibung() {
    return freitextbeschreibung;
  }

  public void setFreitextbeschreibung(String freitextbeschreibung) {
    this.freitextbeschreibung = freitextbeschreibung;
  }

  public String getDiagnoseerlaeuterung() {
    return diagnoseerlaeuterung;
  }

  public void setDiagnoseerlaeuterung(String diagnoseerlaeuterung) {
    this.diagnoseerlaeuterung = diagnoseerlaeuterung;
  }

  public String getDokumentationsdatum() {
    return dokumentationsdatum;
  }

  public void setDokumentationsdatum(String dokumentationsdatum) {
    this.dokumentationsdatum = dokumentationsdatum;
  }

  public String getKlinischer_status() {
    return klinischer_status;
  }

  public void setKlinischer_status(String klinischer_status) {
    this.klinischer_status = klinischer_status;
  }

  public String getZeitraum_von() {
    return zeitraum_von;
  }

  public void setZeitraum_von(String zeitraum_von) {
    this.zeitraum_von = zeitraum_von;
  }

  public String getZeitraum_bis() {
    return zeitraum_bis;
  }

  public void setZeitraum_bis(String zeitraum_bis) {
    this.zeitraum_bis = zeitraum_bis;
  }

  public String getLebensphase_von() {
    return lebensphase_von;
  }

  public void setLebensphase_von(String lebensphase_von) {
    this.lebensphase_von = lebensphase_von;
  }

  public String getLebensphase_bis() {
    return lebensphase_bis;
  }

  public void setLebensphase_bis(String lebensphase_bis) {
    this.lebensphase_bis = lebensphase_bis;
  }

  public String getFeststellungsdatum() {
    return feststellungsdatum;
  }

  public void setFeststellungsdatum(String feststellungsdatum) {
    this.feststellungsdatum = feststellungsdatum;
  }
}
