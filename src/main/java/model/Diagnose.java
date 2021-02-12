package model;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import com.opencsv.bean.CsvBindByName;
import constants.Constants;
import constants.*;
import enums.ICD_Diagnosesicherheit;
import enums.ICD_Seitenlokalisation;
import enums.KBVBaseStageLife;
import helper.*;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.util.Date;
import java.util.List;

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
    return FhirHelper.getMIIPatientReference(this.getPatNr());
  }

  public CodeableConcept getCode() {
    String icdCode = this.getIcd_diagnosecode();
    String alphaCode = this.getAlpha_diagnosecode();
    String snomedCode = this.getSnomed_diagnosecode();
    String orphanetCode = this.getOrphanet_diagnosecode();
    String weitererCode = this.getWeitere_diagnosecode();
    if (Helper.checkAllEmptyString(icdCode, alphaCode, snomedCode, orphanetCode, weitererCode)) {
      throw new IllegalStateException("There has to be at least 1 Diagnosecode in Diagnose!");
    }
    CodeableConcept code = new CodeableConcept();
    // ICD-10-GM (optional)
    code.addCoding(this.getCodeIcd());
    // ALPHA-ID (optional)
    code.addCoding(this.getCodeAlpha());
    // SCT (optional)
    code.addCoding(this.getCodeSct());
    // ORPHANET (optional)
    code.addCoding(this.getCodeOrphanet());
    // Weitere Kodesysteme (optional)
    code.addCoding(this.getCodeWeitere());
    // Text (optional)
    code.setText(this.getText());
    return code;
  }

  public String getText() {
    return this.getFreitextbeschreibung();
  }

  public Coding getCodeIcd() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getIcd_diagnosecode());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = CodingSystem.ICD_10_GM_DIMDI;
    String display = parsedCode.getDisplay();
    Coding icd = FhirGenerator.coding(code, system, display);
    icd.addExtension(this.getCodeIcdDiagnosesicherheit());
    icd.addExtension(this.getCodeIcdSeitenlokalisation());
    icd.addExtension(this.getCodeIcdAusrufezeichen());
    icd.addExtension(this.getCodeIcdManifestationscode());
    icd.addExtension(this.getCodeIcdPrimaercode());
    return icd;
  }

  public Extension getCodeIcdPrimaercode() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getIcd_primaercode());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = CodingSystem.ICD_10_GM_DIMDI;
    String display = parsedCode.getDisplay();
    String version = Constants.VERSION_2020;
    Coding value = FhirGenerator.coding(code, system, display, version);
    String url = ExtensionUrl.ICD_10_GM_PRIMAERCODE;
    return FhirGenerator.extension(url, value);
  }

  public Extension getCodeIcdManifestationscode() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getIcd_manifestationscode());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = CodingSystem.ICD_10_GM_DIMDI;
    String display = parsedCode.getDisplay();
    String version = Constants.VERSION_2020;
    Coding value = FhirGenerator.coding(code, system, display, version);
    String url = ExtensionUrl.ICD_10_GM_MANIFESTATIONSCODE;
    return FhirGenerator.extension(url, value);
  }

  public Extension getCodeIcdAusrufezeichen() {
    // TODO: Is Ausrufezeichen code or count?
    ParsedCode parsedCode = ParsedCode.fromString(this.getIcd_ausrufezeichencode());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = CodingSystem.ICD_10_GM_DIMDI;
    String display = parsedCode.getDisplay();
    String version = Constants.VERSION_2020;
    Coding value = FhirGenerator.coding(code, system, display, version);
    String url = ExtensionUrl.ICD_10_GM_AUSRUFEZEICHEN;
    return FhirGenerator.extension(url, value);
  }

  public Extension getCodeIcdSeitenlokalisation() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getIcd_seitenlokalisation());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(this.getIcd_seitenlokalisation())) {
      return Constants.getEmptyValue();
    }
    Coding value =
        ICD_Seitenlokalisation.fromCode(code)
            .map(FhirGenerator::coding)
            .orElse(LOGGER.error("getCodeIcdSeitenlokalisation", "icd_seitenlokalisation", code));
    String url = ExtensionUrl.ICD_10_GM_SEITENLOKALISATION;
    return FhirGenerator.extension(url, value);
  }

  public Extension getCodeIcdDiagnosesicherheit() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getIcd_diagnosesicherheit());
    String snomedCode = parsedCode.getCode();
    if (Helper.checkEmptyString(snomedCode)) {
      return Constants.getEmptyValue();
    }
    Coding value =
        ICD_Diagnosesicherheit.fromSnomedCode(snomedCode)
            .map(FhirGenerator::coding)
            .orElse(
                LOGGER.error("getCodeIcdDiagnosesicherheit", "icd_diagnosesicherheit", snomedCode));
    String url = ExtensionUrl.ICD_10_GM_DIAGNOSESEICHERHEIT;
    return FhirGenerator.extension(url, value);
  }

  public Coding getCodeAlpha() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getAlpha_diagnosecode());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = CodingSystem.ALPHA_ID_DIMDI;
    String display = parsedCode.getDisplay();
    return FhirGenerator.coding(code, system, display);
  }

  public Coding getCodeSct() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getSnomed_diagnosecode());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    String display = parsedCode.getDisplay();
    return FhirGenerator.coding(code, system, display);
  }

  public Coding getCodeOrphanet() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getOrphanet_diagnosecode());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = CodingSystem.ORPHANET;
    String display = parsedCode.getDisplay();
    return FhirGenerator.coding(code, system, display);
  }

  public Coding getCodeWeitere() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getWeitere_diagnosecode());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = parsedCode.getSystem();
    String display = parsedCode.getDisplay();
    return FhirGenerator.coding(code, system, display);
  }

  public Annotation getNote() {
    String erlaeuterung = this.getDiagnoseerlaeuterung();
    if (Helper.checkEmptyString(erlaeuterung)) {
      return Constants.getEmptyValue();
    }
    return new Annotation().setText(erlaeuterung);
  }

  public Date getRecordedDate() {
    String dokumentationsdatum = this.getDokumentationsdatum();
    return Helper.getDateFromISO(dokumentationsdatum)
        .orElse(LOGGER.error("getRecordedDate", "dokumentationsdatum", dokumentationsdatum));
  }

  public Type getOnset() {
    String zeitraumVon = this.getZeitraum_von();
    String zeitraumBis = this.getZeitraum_bis();
    String lebensphaseVon = this.getLebensphase_von();
    String lebensphaseBis = this.getLebensphase_bis();
    if (Helper.checkAllEmptyString(zeitraumVon, zeitraumBis, lebensphaseVon, lebensphaseBis)) {
      return Constants.getEmptyValue();
    }
    DateTimeType start = new DateTimeType();
    if (Helper.checkNonEmptyString(zeitraumVon)) {
      start.setPrecision(TemporalPrecisionEnum.SECOND);
      Date startDate =
          Helper.getDateFromISO(zeitraumVon)
              .orElse(LOGGER.error("getOnset", "zeitraum_von", zeitraumVon));
      start.setValue(startDate);
    }
    start.addExtension(this.getLebensphaseVon());
    DateTimeType end = new DateTimeType();
    if (Helper.checkNonEmptyString(zeitraumBis)) {
      end.setPrecision(TemporalPrecisionEnum.SECOND);
      Date endDate =
          Helper.getDateFromISO(zeitraumBis)
              .orElse(LOGGER.error("getOnset", "zeitraum_bis", zeitraumBis));
      end.setValue(endDate);
    }
    end.addExtension(this.getLebensphaseBis());
    // Return DateTimeType if only start has been set. Otherwise return Period.
    return !end.hasValue() && !end.hasExtension()
        ? start
        : new Period().setStartElement(start).setEndElement(end);
  }

  public Extension getLebensphaseVon() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getLebensphase_von());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    CodeableConcept type =
        KBVBaseStageLife.fromCode(code)
            .map(FhirGenerator::coding)
            .map(FhirGenerator::codeableConcept)
            .orElse(LOGGER.error("getLebensphaseVon", "lebensphase_von", code));
    String url = ExtensionUrl.STAGE_LIFE;
    return FhirGenerator.extension(url, type);
  }

  public Extension getLebensphaseBis() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getLebensphase_bis());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    CodeableConcept type =
        KBVBaseStageLife.fromCode(code)
            .map(FhirGenerator::coding)
            .map(FhirGenerator::codeableConcept)
            .orElse(LOGGER.error("getLebensphaseBis", "lebensphase_bis", code));
    String url = ExtensionUrl.STAGE_LIFE;
    return FhirGenerator.extension(url, type);
  }

  public CodeableConcept getBodySite() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getKoerperstelle());
    String code = parsedCode.getCode();
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    String display = parsedCode.getDisplay();
    Coding bodySite = FhirGenerator.coding(code, system, display);
    return FhirGenerator.codeableConcept(bodySite);
  }

  public CodeableConcept getClinicalStatus() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getKlinischer_status());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return Constants.getEmptyValue();
    }
    String system = CodingSystem.CONDITION_CLINICAL_STATUS;
    String display = parsedCode.getDisplay();
    Coding clinicalStatus = FhirGenerator.coding(code, system, display);
    return FhirGenerator.codeableConcept(clinicalStatus);
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
