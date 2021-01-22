package model;

import constants.CodingSystem;
import constants.MetaProfile;
import constants.MetaSource;
import constants.MetaVersionId;
import helper.FhirHelper;
import helper.Helper;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

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
    // BodySite, only includes Koerperstelle
    condition.addBodySite(this.getBodySite());
    return condition;
  }

  public CodeableConcept getBodySite() {
    String code = this.getKoerperstelle();
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    Coding bodySite = FhirHelper.generateCoding(code, system);
    return new CodeableConcept().addCoding(bodySite);
  }

  public CodeableConcept getClinicalStatus() {
    String code = this.getKlinischer_status();
    // TODO: Clinical status system is still missing
    String system = "";
    Coding clinicalStatus = FhirHelper.generateCoding(code, system);
    return new CodeableConcept().addCoding(clinicalStatus);
  }

  public Meta getMeta() {
    String profile = MetaProfile.DIAGNOSE_CONDITION;
    String source = MetaSource.DIAGNOSE_CONDITION;
    String versionId = MetaVersionId.DIAGNOSE_CONDITION;
    return FhirHelper.generateMeta(profile, source, versionId);
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
