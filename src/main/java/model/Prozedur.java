package model;

import com.opencsv.bean.CsvBindByName;
import constants.Constants;
import constants.*;
import enums.DurchfuehrungsabsichtCode;
import enums.ProcedureCategorySnomedMapping;
import enums.SeitenlokalisationCode;
import helper.FhirGenerator;
import helper.FhirHelper;
import helper.Helper;
import helper.ParsedCode;
import interfaces.Datablock;
import org.hl7.fhir.r4.model.*;

import java.util.Date;
import java.util.List;

public class Prozedur implements Datablock {
  @CsvBindByName private String patNr;
  // OPS Prozedur kodiert
  @CsvBindByName private String OPS_Vollst_Prozedurenkode;
  @CsvBindByName private String OPS_Seitenlokalisation;
  // SNOMED Prozedur kodiert
  @CsvBindByName private String SNOMED_Vollst_Prozedurenkode;
  @CsvBindByName private String durchfuehrungsabsicht;
  @CsvBindByName private String durchfuehrungsdatum;
  @CsvBindByName private String dokumentationsdatum;
  @CsvBindByName private String freitextbeschreibung;
  @CsvBindByName private String koerperstelle;

  @Override
  public List<Resource> toFhirResources() {
    return Helper.listOf(this.getProcedure());
  }

  public Procedure getProcedure() {
    Procedure procedure = new Procedure();
    // Meta
    procedure.setMeta(this.getMeta());
    // Status
    procedure.setStatus(this.getStatus());
    // Category, can only be set if OPS is used.
    procedure.setCategory(this.getCategory());
    // Code
    procedure.setCode(this.getCode());
    // Performed
    procedure.setPerformed(this.getPerformed());
    // BodySites (optional), currently includes Koerperstelle
    procedure.addBodySite(this.getBodySite());
    // Notes (optional), currently includes Freitextbeschreibung
    procedure.addNote(this.getNote());
    // Extension: RecordedDate (optional), only needed if Dokumentationsdatum != Durchfuehrungsdatum
    procedure.addExtension(this.getRecordedDate());
    // Extension: Durchfuehrungsabsicht (optional)
    procedure.addExtension(this.getDurchfuehrungsabsicht());
    // Subject
    procedure.setSubject(this.getSubject());
    return procedure;
  }

  public Procedure.ProcedureStatus getStatus() {
    return Procedure.ProcedureStatus.COMPLETED;
  }

  public Reference getSubject() {
    String patientenNummer = this.getPatNr();
    if (Helper.checkEmptyString(patientenNummer)) {
      return null;
    }
    Reference assignerRef = FhirHelper.getUKUAssignerReference();
    Identifier subjectId =
        FhirGenerator.identifier(patientenNummer, IdentifierSystem.LOCAL_PID, assignerRef);
    return FhirGenerator.reference(subjectId);
  }

  public Meta getMeta() {
    return FhirGenerator.meta(
        MetaProfile.PROZEDUR_PROCEDURE,
        MetaSource.PROZEDUR_PROCEDURE,
        MetaVersionId.PROZEDUR_PROCEDURE);
  }

  /** @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/81934/" */
  public CodeableConcept getCategory() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getOPS_Vollst_Prozedurenkode());
    String ops = parsedCode.getCode();
    if (Helper.checkEmptyString(ops)) {
      return null;
    }
    ProcedureCategorySnomedMapping mapping = ProcedureCategorySnomedMapping.fromOpsCode(ops);
    Coding categoryCode = FhirGenerator.coding(mapping);
    return new CodeableConcept().addCoding(categoryCode);
  }

  public CodeableConcept getCode() {
    CodeableConcept code = new CodeableConcept();
    code.addCoding(this.getCodingSnomed());
    code.addCoding(this.getCodingOps());
    return code;
  }

  public Coding getCodingOps() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getOPS_Vollst_Prozedurenkode());
    String opsCode = parsedCode.getCode();
    if (Helper.checkEmptyString(opsCode)) {
      return null;
    }
    String system = CodingSystem.OPS_DIMDI;
    String display = parsedCode.getDisplay();
    String version = Constants.VERSION_2020;
    Coding ops = FhirGenerator.coding(opsCode, system, display, version);
    ops.addExtension(this.getSeitenlokalisation());
    return ops;
  }

  /** @see "https://simplifier.net/basisprofil-de-r4/extension-seitenlokalisation" */
  public Extension getSeitenlokalisation() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getOPS_Seitenlokalisation());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return null;
    }
    SeitenlokalisationCode seitenCode = SeitenlokalisationCode.fromCode(code);
    Coding value = FhirGenerator.coding(seitenCode);
    return FhirGenerator.extension(ExtensionUrl.OPS_SEITENLOKALISATION, value);
  }

  public Coding getCodingSnomed() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getSNOMED_Vollst_Prozedurenkode());
    String snomed = parsedCode.getCode();
    if (Helper.checkEmptyString(snomed)) {
      return null;
    }
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    String display = parsedCode.getDisplay();
    return FhirGenerator.coding(snomed, system, display);
  }

  public DateTimeType getPerformed() {
    Date date = Helper.getDateFromISO(this.getDurchfuehrungsdatum());
    return FhirGenerator.dateTimeType(date);
  }

  /** @see "https://simplifier.net/packages/hl7.fhir.r4.core/4.0.1/files/80349/" */
  public CodeableConcept getBodySite() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getKoerperstelle());
    String code = parsedCode.getCode();
    if (Helper.checkEmptyString(code)) {
      return null;
    }
    String system = CodingSystem.SNOMED_CLINICAL_TERMS;
    String display = parsedCode.getDisplay();
    Coding coding = FhirGenerator.coding(code, system, display);
    return new CodeableConcept().addCoding(coding);
  }

  public Annotation getNote() {
    String text = this.getFreitextbeschreibung();
    if (Helper.checkEmptyString(text)) {
      return null;
    }
    return new Annotation().setText(text);
  }

  public Extension getRecordedDate() {
    String dokuDatum = this.getDokumentationsdatum();
    if (Helper.checkEmptyString(dokuDatum) || dokuDatum.equals(this.getDurchfuehrungsdatum())) {
      return null;
    }
    Date recorded = Helper.getDateFromISO(dokuDatum);
    DateTimeType date = FhirGenerator.dateTimeType(recorded);
    return FhirGenerator.extension(ExtensionUrl.RECORDED_DATE, date);
  }

  /**
   * @see "https://simplifier.net/medizininformatikinitiative-modulprozeduren/durchfuehrungsabsicht"
   */
  public Extension getDurchfuehrungsabsicht() {
    ParsedCode parsedCode = ParsedCode.fromString(this.getKernDurchfuehrungsabsicht());
    String absichtCode = parsedCode.getCode();
    if (Helper.checkEmptyString(absichtCode)) {
      return null;
    }
    DurchfuehrungsabsichtCode durchfuehrungsabsichtCode =
        DurchfuehrungsabsichtCode.fromCode(absichtCode);
    Coding code = FhirGenerator.coding(durchfuehrungsabsichtCode);
    return FhirGenerator.extension(ExtensionUrl.DURCHFUEHRUNGSABSICHT, code);
  }

  public void setDurchfuehrungsabsicht(String durchfuehrungsabsicht) {
    this.durchfuehrungsabsicht = durchfuehrungsabsicht;
  }

  public String getPatNr() {
    return patNr;
  }

  public void setPatNr(String patNr) {
    this.patNr = patNr;
  }

  public String getOPS_Vollst_Prozedurenkode() {
    return OPS_Vollst_Prozedurenkode;
  }

  public void setOPS_Vollst_Prozedurenkode(String OPS_Vollst_Prozedurenkode) {
    this.OPS_Vollst_Prozedurenkode = OPS_Vollst_Prozedurenkode;
  }

  public String getOPS_Seitenlokalisation() {
    return OPS_Seitenlokalisation;
  }

  public void setOPS_Seitenlokalisation(String OPS_Seitenlokalisation) {
    this.OPS_Seitenlokalisation = OPS_Seitenlokalisation;
  }

  public String getSNOMED_Vollst_Prozedurenkode() {
    return SNOMED_Vollst_Prozedurenkode;
  }

  public void setSNOMED_Vollst_Prozedurenkode(String SNOMED_Vollst_Prozedurenkode) {
    this.SNOMED_Vollst_Prozedurenkode = SNOMED_Vollst_Prozedurenkode;
  }

  public String getKernDurchfuehrungsabsicht() {
    return durchfuehrungsabsicht;
  }

  public String getDurchfuehrungsdatum() {
    return durchfuehrungsdatum;
  }

  public void setDurchfuehrungsdatum(String durchfuehrungsdatum) {
    this.durchfuehrungsdatum = durchfuehrungsdatum;
  }

  public String getDokumentationsdatum() {
    return dokumentationsdatum;
  }

  public void setDokumentationsdatum(String dokumentationsdatum) {
    this.dokumentationsdatum = dokumentationsdatum;
  }

  public String getFreitextbeschreibung() {
    return freitextbeschreibung;
  }

  public void setFreitextbeschreibung(String freitextbeschreibung) {
    this.freitextbeschreibung = freitextbeschreibung;
  }

  public String getKoerperstelle() {
    return koerperstelle;
  }

  public void setKoerperstelle(String koerperstelle) {
    this.koerperstelle = koerperstelle;
  }
}
