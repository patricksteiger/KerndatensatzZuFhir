package model;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import constants.Constants;
import constants.*;
import enums.ICD_Diagnosesicherheit;
import enums.ICD_Seitenlokalisation;
import enums.KBVBaseStageLife;
import helper.FhirGenerator;
import helper.FhirHelper;
import helper.Helper;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.util.Date;
import java.util.List;

public class Diagnose implements Datablock {
  private String patNr;
  // ICD-10-GM Diagnose kodiert
  private String icd_diagnosecode;
  private String icd_primaercode;
  private String icd_manifestationscode;
  private String icd_ausrufezeichencode;
  private String icd_diagnosesicherheit;
  private String icd_seitenlokalisation;
  // ALPHA-ID kodiert
  private String alpha_diagnosecode;
  // ORPHANET Diagnose kodiert
  private String orphanet_diagnosecode;
  // SNOMED Diagnose kodiert
  private String snomed_diagnosecode;
  // Weitere Kodesysteme
  private String weitere_diagnosecode;
  private String koerperstelle;
  private String freitextbeschreibung;
  private String diagnoseerlaeuterung;
  private String dokumentationsdatum;
  private String klinischer_status;
  // Klinisch relevanter Zeitraum - Zeitraum
  private String zeitraum_von;
  private String zeitraum_bis;
  // klinisch relevanter Zeitraum - Lebensphase
  private String lebensphase_von;
  private String lebensphase_bis;
  private String feststellungsdatum;

  @Override
  public List<Resource> toFhirResources() {
    return Helper.listOf(this.getCondition());
  }

  public Condition getCondition() {
    Condition condition = new Condition();
    // Meta
    condition.setMeta(this.getMeta());
    // Clinical status (optional)
    if (Helper.checkNonEmptyString(this.getKlinischer_status()))
      condition.setClinicalStatus(this.getClinicalStatus());
    // Code
    condition.setCode(this.getCode());
    // BodySite, only includes Koerperstelle
    condition.addBodySite(this.getBodySite());
    // Subject
    condition.setSubject(this.getSubject());
    // Onset (optional)
    if (Helper.checkAnyNonEmptyStrings(
        this.getZeitraum_von(),
        this.getZeitraum_bis(),
        this.getLebensphase_von(),
        this.getLebensphase_bis())) condition.setOnset(this.getOnset());
    // RecordedDate
    condition.setRecordedDate(this.getRecordedDate());
    // Note (optional), only includes Diagnoseerläuterung
    if (Helper.checkNonEmptyString(this.getDiagnoseerlaeuterung()))
      condition.addNote(this.getNote());
    return condition;
  }

  public Reference getSubject() {
    return FhirHelper.getMIIPatientReference(this.getPatNr());
  }

  public CodeableConcept getCode() {
    CodeableConcept code = new CodeableConcept();
    // ICD-10-GM (optional)
    if (Helper.checkNonEmptyString(this.getIcd_diagnosecode())) code.addCoding(this.getCodeIcd());
    // ALPHA-ID (optional)
    if (Helper.checkNonEmptyString(this.getAlpha_diagnosecode()))
      code.addCoding(this.getCodeAlpha());
    // SCT (optional)
    if (Helper.checkNonEmptyString(this.getSnomed_diagnosecode()))
      code.addCoding(this.getCodeSct());
    // ORPHANET (optional)
    if (Helper.checkNonEmptyString(this.getOrphanet_diagnosecode()))
      code.addCoding(this.getCodeOrphanet());
    // Weitere Kodesysteme (optional)
    if (Helper.checkNonEmptyString(this.getWeitere_diagnosecode()))
      code.addCoding(this.getCodeWeitere());
    // Text (optional)
    if (Helper.checkNonEmptyString(this.getFreitextbeschreibung()))
      code.setText(this.getFreitextbeschreibung());
    return code;
  }

  public Coding getCodeIcd() {
    String code = this.getIcd_diagnosecode();
    String system = CodingSystem.ICD_10_GM_DIMDI;
    Coding icd = FhirGenerator.coding(code, system);
    if (Helper.checkNonEmptyString(this.getIcd_diagnosesicherheit()))
      icd.addExtension(this.getCodeIcdDiagnosesicherheit());
    if (Helper.checkNonEmptyString(this.getIcd_seitenlokalisation()))
      icd.addExtension(this.getCodeIcdSeitenlokalisation());
    if (Helper.checkNonEmptyString(this.getIcd_ausrufezeichencode()))
      icd.addExtension(this.getCodeIcdAusrufezeichen());
    if (Helper.checkNonEmptyString(this.getIcd_manifestationscode()))
      icd.addExtension(this.getCodeIcdManifestationscode());
    if (Helper.checkNonEmptyString(this.getIcd_primaercode()))
      icd.addExtension(this.getCodeIcdPrimaercode());
    return icd;
  }

  public Extension getCodeIcdPrimaercode() {
    String code = this.getIcd_primaercode();
    String system = CodingSystem.ICD_10_GM_DIMDI;
    String display = Constants.EMPTY_DISPLAY;
    String version = Constants.VERSION_2020;
    Coding value = FhirGenerator.coding(code, system, display, version);
    String url = ExtensionUrl.ICD_10_GM_PRIMAERCODE;
    return FhirGenerator.extension(url, value);
  }

  public Extension getCodeIcdManifestationscode() {
    String code = this.getIcd_manifestationscode();
    String system = CodingSystem.ICD_10_GM_DIMDI;
    String display = Constants.EMPTY_DISPLAY;
    String version = Constants.VERSION_2020;
    Coding value = FhirGenerator.coding(code, system, display, version);
    String url = ExtensionUrl.ICD_10_GM_MANIFESTATIONSCODE;
    return FhirGenerator.extension(url, value);
  }

  public Extension getCodeIcdAusrufezeichen() {
    String code = this.getIcd_ausrufezeichencode();
    String system = CodingSystem.ICD_10_GM_DIMDI;
    String display = Constants.EMPTY_DISPLAY;
    String version = Constants.VERSION_2020;
    Coding value = FhirGenerator.coding(code, system, display, version);
    String url = ExtensionUrl.ICD_10_GM_AUSRUFEZEICHEN;
    return FhirGenerator.extension(url, value);
  }

  public Extension getCodeIcdSeitenlokalisation() {
    String code = this.getIcd_seitenlokalisation();
    ICD_Seitenlokalisation seitenlokalisation = ICD_Seitenlokalisation.fromCode(code);
    Coding value =
        FhirGenerator.coding(
            seitenlokalisation.getCode(),
            seitenlokalisation.getSystem(),
            seitenlokalisation.getDisplay());
    String url = ExtensionUrl.ICD_10_GM_SEITENLOKALISATION;
    return FhirGenerator.extension(url, value);
  }

  public Extension getCodeIcdDiagnosesicherheit() {
    String snomedCode = this.getIcd_diagnosesicherheit();
    ICD_Diagnosesicherheit diagnosesicherheit = ICD_Diagnosesicherheit.fromSnomedCode(snomedCode);
    Coding value =
        FhirGenerator.coding(
            diagnosesicherheit.getCode(),
            diagnosesicherheit.getSystem(),
            diagnosesicherheit.getDisplay());
    String url = ExtensionUrl.ICD_10_GM_DIAGNOSESEICHERHEIT;
    return FhirGenerator.extension(url, value);
  }

  public Coding getCodeAlpha() {
    String code = this.getAlpha_diagnosecode();
    String system = CodingSystem.ALPHA_ID_DIMDI;
    return FhirGenerator.coding(code, system);
  }

  public Coding getCodeSct() {
    String code = this.getSnomed_diagnosecode();
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    return FhirGenerator.coding(code, system);
  }

  public Coding getCodeOrphanet() {
    String code = this.getOrphanet_diagnosecode();
    String system = CodingSystem.ORPHANET;
    return FhirGenerator.coding(code, system);
  }

  public Coding getCodeWeitere() {
    String code = this.getWeitere_diagnosecode();
    // TODO: What is system of weiterer diagnosecode?
    String system = "";
    return FhirGenerator.coding(code, system);
  }

  public Annotation getNote() {
    Annotation note = new Annotation();
    note.setText(this.getDiagnoseerlaeuterung());
    return note;
  }

  public Date getRecordedDate() {
    Date recordedDate = Helper.getDateFromISO(this.getDokumentationsdatum());
    return recordedDate;
  }

  public Type getOnset() {
    DateTimeType start = new DateTimeType();
    if (Helper.checkNonEmptyString(this.getZeitraum_von())) {
      start.setPrecision(TemporalPrecisionEnum.SECOND);
      Date startDate = Helper.getDateFromISO(this.getZeitraum_von());
      start.setValue(startDate);
    }
    if (Helper.checkNonEmptyString(this.getLebensphase_von())) {
      start.addExtension(this.getLebensphaseVon());
    }
    DateTimeType end = new DateTimeType();
    if (Helper.checkNonEmptyString(this.getZeitraum_bis())) {
      end.setPrecision(TemporalPrecisionEnum.SECOND);
      Date endDate = Helper.getDateFromISO(this.getZeitraum_bis());
      end.setValue(endDate);
    }
    if (Helper.checkNonEmptyString(this.getLebensphase_bis())) {
      end.addExtension(this.getLebensphaseBis());
    }
    // Return DateTimeType if only startDate has been set. Otherwise return Period.
    return !start.hasExtension() && !end.hasValue() && !end.hasExtension()
        ? start
        : new Period().setStartElement(start).setEndElement(end);
  }

  public Extension getLebensphaseVon() {
    KBVBaseStageLife stage = KBVBaseStageLife.fromCode(this.getLebensphase_von());
    Coding lebensphase =
        FhirGenerator.coding(
            stage.getCode(), stage.getSystem(), stage.getDisplay(), stage.getVersion());
    CodeableConcept type = new CodeableConcept().addCoding(lebensphase);
    String url = ExtensionUrl.STAGE_LIFE;
    return FhirGenerator.extension(url, type);
  }

  public Extension getLebensphaseBis() {
    KBVBaseStageLife stage = KBVBaseStageLife.fromCode(this.getLebensphase_bis());
    Coding lebensphase =
        FhirGenerator.coding(
            stage.getCode(), stage.getSystem(), stage.getDisplay(), stage.getVersion());
    CodeableConcept type = new CodeableConcept().addCoding(lebensphase);
    String url = ExtensionUrl.STAGE_LIFE;
    return FhirGenerator.extension(url, type);
  }

  public CodeableConcept getBodySite() {
    String code = this.getKoerperstelle();
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    Coding bodySite = FhirGenerator.coding(code, system);
    return new CodeableConcept().addCoding(bodySite);
  }

  public CodeableConcept getClinicalStatus() {
    String code = this.getKlinischer_status();
    String system = CodingSystem.CONDITION_CLINICAL_STATUS;
    Coding clinicalStatus = FhirGenerator.coding(code, system);
    return new CodeableConcept().addCoding(clinicalStatus);
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
